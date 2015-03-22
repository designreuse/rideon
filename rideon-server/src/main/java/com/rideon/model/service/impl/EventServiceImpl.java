/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.service.impl;

import com.rideon.model.dao.EventDao;
import com.rideon.model.dao.PracticeDao;
import com.rideon.model.dao.RouteDao;
import com.rideon.model.dao.UserDao;
import com.rideon.model.dto.EventDto;
import com.rideon.model.dto.MessageDto;
import com.rideon.model.dto.MultimediaDto;
import com.rideon.model.dto.PracticeDto;
import com.rideon.model.dto.RouteDto;
import com.rideon.model.dto.SegmentDto;
import com.rideon.model.dto.SegmentPracticeDto;
import com.rideon.model.dto.UserDto;
import com.rideon.model.dto.list.BaseListDto;
import com.rideon.model.dto.list.EventListDto;
import com.rideon.model.dto.list.UserListDto;
import com.rideon.common.enums.EventStatus;
import com.rideon.common.enums.EventType;
import com.rideon.common.enums.Privacy;
import com.rideon.model.domain.Event;
import com.rideon.model.domain.Message;
import com.rideon.model.domain.Multimedia;
import com.rideon.model.domain.Practice;
import com.rideon.model.domain.Route; 
import com.rideon.model.domain.Segment;
import com.rideon.model.domain.SegmentPractice;
import com.rideon.model.domain.User;
import com.rideon.segmentdetector.util.GisUtils;
import com.rideon.model.service.EventService;
import com.rideon.util.Constants;
import com.rideon.util.FilePaths;
import com.rideon.util.IOHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import ma.glasnost.orika.MapperFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Fer
 */
@Service(value = "eventService")
public class EventServiceImpl implements EventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventServiceImpl.class);
    @Autowired
    EventDao eventDao;
    @Autowired
    RouteDao routeDao;
    @Autowired
    PracticeDao practiceDao;
    @Autowired
    UserDao userDao;
    @Autowired
    MapperFacade mapper;

    @Override
    public EventDto add(EventDto eventDto) {
        if (eventDto.getEventDate() != null) {
            Date now = new Date();
            Date ed = eventDto.getEventDate();
            int c = ed.compareTo(now);
            if (c >= 0) {
                User owner = userDao.getById(eventDto.getOwner().getUsername());

                if (owner != null) {
                    Event event = mapper.map(eventDto, Event.class);
                    //Set Owner
                    event.setOwner(owner);
                    //Set default Status
                    event.setStatus(EventStatus.OPEN);
                    //Set default privacy
                    if (event.getPrivacy() == null) {
                        event.setPrivacy(Privacy.PUBLIC);
                    }

                    event.getMembers().add(owner);
                    eventDao.add(event);

                    String url;
                    if (event.getEventType() == EventType.CHALLENGE) {
                        url = getClass().getResource(FilePaths.CHALLENGE_IMAGE).getFile();
                    } else {
                        url = getClass().getResource(FilePaths.HANGOUT_IMAGE).getFile();
                    }
                    try {
                        Multimedia image = IOHelper.readMultimediaFromFile(url);
                        setResizedImage(event.getId(), image);
                    } catch (IOException e) {
                        LOGGER.error("", e);
                    }

                    return mapper.map(event, EventDto.class);
                } else {
                    throw new IllegalArgumentException("Events must have an owner");
                }
            } else {
                throw new IllegalArgumentException("Invalid event date");
            }

        }
        throw new IllegalArgumentException("Invalid event date");
    }

    @Override
    public EventDto update(EventDto eventDto) {
        Event event = mapper.map(eventDto, Event.class);
        eventDao.update(event);
        return mapper.map(event, EventDto.class);
    }

    @Override
    public EventDto addPractice(String eventId, PracticeDto practice) {
        Event event = (Event) eventDao.getById(Long.valueOf(eventId));
        if (event != null) {
            if (event.getEventDate() != null) {
                Date now = new Date();
                Date ed = event.getEventDate();
                int c = ed.compareTo(now);
                if (c >= 0
                        || (c < 0 && event.getUploadAfterDate())) {
                    Practice pr = practiceDao.getById(practice.getId());
                    //Se el evento contiene una ruta
                    if (event.getRoute() != null) {
                        Route eventRoute = (Route) eventDao.getRoute(Long.valueOf(eventId));
                        Route practiceRoute = (Route) practiceDao.getRoute(practice.getId());
                        Boolean validPractice = GisUtils.compareWith(eventRoute.getGeometry(), practiceRoute.getGeometry());
                        if (validPractice) {
                            eventDao.addPractice(Long.valueOf(eventId), pr);
                            return mapper.map(event, EventDto.class);
                        }
                    }
                    //Si el evento contiene un segmento
                    if (event.getSegment() != null) {
                        Segment eventSegment = (Segment) eventDao.getRoute(event.getId());
                        Set<SegmentPractice> practiceSegmentsResutls = practiceDao.getSegment(pr.getId());
                        for (SegmentPractice sp : practiceSegmentsResutls) {
                            if (sp.getId().getSegmentId().equals(eventSegment.getId())) {
                                eventDao.addPractice(event.getId(), pr);
//                            eventDao.update(event);
                                return mapper.map(event, EventDto.class);
                            }
                        }
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("Events must have an owner");
        }
        return mapper.map(event, EventDto.class);
    }

    @Override
    public RouteDto setRoute(String routeId, String eventId) {
        eventDao.setRoute(Long.valueOf(routeId), Long.valueOf(eventId));
        return null;
    }

    @Override
    public SegmentDto setSegment(String segmentId, String eventId) {
        eventDao.setSegment(Long.valueOf(segmentId), Long.valueOf(eventId));
        return null;
    }

    @Override
    public RouteDto getRoute(String eventId) {
        Object ob = eventDao.getRoute(Long.valueOf(eventId));
        if (ob instanceof Route) {
            return mapper.map(ob, RouteDto.class);
        }
        return null;
    }

    @Override
    public SegmentDto getSegment(String eventId) {
        Object ob = eventDao.getRoute(Long.valueOf(eventId));
        if (ob instanceof Segment) {
            return mapper.map(ob, SegmentDto.class);
        }
        return null;
    }

    @Override
    public BaseListDto<Object> getRouteResults(String eventId) {
        BaseListDto<Object> dtoList = new BaseListDto<>();

        List<Practice> list = eventDao.getRouteResults(Long.valueOf(eventId));
        if (list != null) {
            for (Practice pr : list) {
                dtoList.add(mapper.map(pr, PracticeDto.class));
            }
        }
        List<SegmentPractice> list2 = eventDao.getSegmentResults(Long.valueOf(eventId));
        if (list2 != null) {
            for (SegmentPractice pr : list2) {
                User owner = practiceDao.getOwner(pr.getPractice());
                SegmentPracticeDto sp = mapper.map(pr, SegmentPracticeDto.class);
                sp.setOwner(mapper.map(owner, UserDto.class));
                dtoList.add(sp);
            }
        }

        return dtoList;
    }

    @Override
    public BaseListDto<SegmentPracticeDto> getSegmentsResults(String eventId) {
        List<SegmentPractice> list = eventDao.getSegmentResults(Long.valueOf(eventId));
        BaseListDto<SegmentPracticeDto> dtoList = new BaseListDto<>();
        for (SegmentPractice pr : list) {
            User owner = practiceDao.getOwner(pr.getPractice());
            SegmentPracticeDto sp = mapper.map(pr, SegmentPracticeDto.class);
            sp.setOwner(mapper.map(owner, UserDto.class));
            dtoList.add(sp);
        }
        return dtoList;
    }

    @Override
    public EventListDto search(String query) {
        String[] querys = query.split(" ");
        List<Event> chList = eventDao.search(querys);
        EventListDto dtolist = new EventListDto();
        for (Event ch : chList) {
            dtolist.add(mapper.map(ch, EventDto.class));
        }
        return dtolist;
    }

    @Override
    public void remove(String eventId) {
        eventDao.remove(Long.valueOf(eventId));
    }

    @Override
    public EventDto getById(String eventId) {
        Event e = (Event) eventDao.getById(Long.valueOf(eventId));
        if (e != null) {
            EventDto edto = mapper.map(e, EventDto.class);
            Object ob = eventDao.getRoute(Long.valueOf(eventId));
            if (ob != null) {
                if (ob instanceof Route) {
                    Route obr = (Route) ob;
                    edto.setRouteId(obr.getId().toString());
                }
                if (ob instanceof Segment) {
                    Segment obr = (Segment) ob;
                    edto.setSegmentId(obr.getId().toString());
                }
            }
            return edto;
        }
        return null;
    }

    @Override
    public EventListDto getByUser(String userId) {
        Set<Event> events = userDao.getEvents(userId);
        EventListDto returnList = new EventListDto();
        for (Event h : events) {
            EventDto hdto = mapper.map(h, EventDto.class);
            returnList.add(hdto);
        }
        return returnList;
    }

    @Override
    public MultimediaDto setImage(String eventId, MultimediaDto image) {
        Multimedia img = mapper.map(image, Multimedia.class);
        img = setResizedImage(Long.valueOf(eventId), img);
        return mapper.map(img, MultimediaDto.class);
    }

    private Multimedia setResizedImage(Long eventId, Multimedia image) {
        eventDao.setImage(Long.valueOf(eventId), image);
        try {
            Multimedia thumbnail = IOHelper.resizeImage(image, Constants.THUMBNAIL_SIZE_PX);
            eventDao.setThumbnail(Long.valueOf(eventId), thumbnail);
        } catch (IOException ex) {
            LOGGER.error("", ex);
        }
        return image;
    }

    @Override
    public MultimediaDto getImage(String eventId) {
        Multimedia img = eventDao.getImage(Long.valueOf(eventId));
        return mapper.map(img, MultimediaDto.class);
    }

    @Override
    public MultimediaDto getThumbnail(String eventId) {
        Multimedia img = eventDao.getThumbnail(Long.valueOf(eventId));
        return mapper.map(img, MultimediaDto.class);
    }

    @Override
    public UserListDto addMembers(String eventId, UserListDto members) {
        List<User> memberList = new ArrayList<>();
        UserListDto listDto = null;
        Iterator it = members.iterator();
        while (it.hasNext()) {
            UserDto userDto = (UserDto) it.next();
            memberList.add(mapper.map(userDto, User.class));
        }
        if (!memberList.isEmpty()) {
            List<User> users = eventDao.addMembers(Long.valueOf(eventId), memberList);
            listDto = new UserListDto();
            for (User u : users) {
                listDto.add(mapper.map(u, UserDto.class));
            }
        }
        return listDto;
    }

    @Override
    public UserListDto getMembers(String eventId) {
        List<User> list = eventDao.getMembers(Long.valueOf(eventId));
        UserListDto bList = new UserListDto();
        for (User u : list) {
            bList.add(mapper.map(u, UserDto.class));
        }
        return bList;
    }

    @Override
    public void removeMember(String eventId, String memberId) {
        eventDao.removeMember(Long.valueOf(eventId), memberId);
    }

    @Override
    public MessageDto getMessage(String eventId, String messageId) {
        Message mess = eventDao.getMessage(Long.valueOf(eventId), Long.valueOf(messageId));
        return mapper.map(mess, MessageDto.class);
    }

    @Override
    public MessageDto addMessage(String eventId, MessageDto message) {
        Message mess = eventDao.addMessage(Long.valueOf(eventId), mapper.map(message, Message.class));
        return mapper.map(mess, MessageDto.class);
    }

    @Override
    public void removeMessage(String eventId, String messageId) {
        eventDao.removeMessage(Long.valueOf(eventId), Long.valueOf(messageId));
    }

    @Override
    public BaseListDto<MessageDto> getMessages(String eventId) {
        List<Message> list = eventDao.getMessages(Long.valueOf(eventId));
        BaseListDto<MessageDto> listDto = new BaseListDto<>();
        for (Message m : list) {
            listDto.add(mapper.map(m, MessageDto.class));
        }
        return listDto;
    }

    /**
     * Comprobar si son privados, en tal caso que el usuario este dentro del
     * evento
     */
    @Override
    public EventListDto search(String from, String to) {
        Date f = new Date(Long.valueOf(from));
        Date t = new Date(Long.valueOf(to));
        List<Event> list = eventDao.search(f, t);
        EventListDto dtoList = new EventListDto();
        for (Event e : list) {
            dtoList.add(mapper.map(e, EventDto.class));
        }
        return dtoList;
    }

    @Override
    public String getOwnerUsername(String groupId) {
        return eventDao.getOwnerUsername(Long.valueOf(groupId));
    }

    @Override
    public Privacy getPrivacy(String groupId) {
        return eventDao.getPrivacy(Long.valueOf(groupId));
    }
}
