/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.service.impl;

import com.rideon.common.enums.EventStatus;
import com.rideon.model.dao.EventDao;
import com.rideon.model.dao.GroupDao;
import com.rideon.model.dao.UserDao;
import com.rideon.model.dto.EventDto;
import com.rideon.model.dto.GroupDto;
import com.rideon.model.dto.MessageDto;
import com.rideon.model.dto.MultimediaDto;
import com.rideon.model.dto.UserDto;
import com.rideon.model.dto.list.BaseListDto;
import com.rideon.model.dto.list.UserListDto;
import com.rideon.common.enums.EventType;
import com.rideon.common.enums.Privacy;
import com.rideon.model.domain.Event;
import com.rideon.model.domain.Group;
import com.rideon.model.domain.Message;
import com.rideon.model.domain.Multimedia;
import com.rideon.model.domain.User;
import com.rideon.model.service.GroupService;
import com.rideon.util.Constants;
import com.rideon.util.FilePaths;
import com.rideon.util.IOHelper;
import java.io.IOException;
import java.util.List;
import ma.glasnost.orika.MapperFacade;  
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Fer
 */
@Service(value = "groupService")
public class GroupServiceImpl implements GroupService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupServiceImpl.class);
    @Autowired
    GroupDao groupDao;
    @Autowired
    UserDao userDao;
    @Autowired
    EventDao eventDao;
    @Autowired
    MapperFacade mapper;

    @Override
    public GroupDto getById(String groupId) {
        Group group = groupDao.getById(Long.valueOf(groupId));
        if (group != null) {
            GroupDto groupDto = mapper.map(group, GroupDto.class);
            return groupDto;
        } else {
            return null;
        }
    }

    @Override
    public GroupDto add(GroupDto groupDto) {
        Group group = mapper.map(groupDto, Group.class);


        User owner = userDao.getById(groupDto.getOwner().getUsername());
        if (owner != null) {
            group.setOwner(owner);
            group.getMembers().add(owner);
            groupDao.add(group);
            groupDto = mapper.map(group, GroupDto.class);

            //Multimedia creation
            String url = getClass().getResource(FilePaths.GROUP_IMAGE).getFile();
            try {
                Multimedia image = IOHelper.readMultimediaFromFile(url);
                setResizedImage(group.getId(), image);
            } catch (IOException e) {
                LOGGER.error("", e);
            }
            return groupDto;
        } else {
            throw new IllegalArgumentException("Groups must have an owner");
        }
    }

    @Override
    public void remove(String groupId) {
        groupDao.remove(Long.valueOf(groupId));
    }

    @Override
    public GroupDto update(GroupDto group) {
        Group g = mapper.map(group, Group.class);
        groupDao.update(g);
        group = mapper.map(g, GroupDto.class);
        return group;
    }

    @Override
    public BaseListDto<GroupDto> getByUser(String userId) {
        List<Group> groupList = userDao.getGroups(userId);

        BaseListDto<GroupDto> list = new BaseListDto<>();
        List<GroupDto> data = list.getData();
        if (groupList != null) {
            for (Group group : groupList) {
                data.add(mapper.map(group, GroupDto.class));
            }
        }
        return list;
    }

    @Override
    public MultimediaDto setImage(String groupId, MultimediaDto image) {
        Multimedia img = mapper.map(image, Multimedia.class);
        img = setResizedImage(Long.valueOf(groupId), img);
        return mapper.map(img, MultimediaDto.class);
    }

    private Multimedia setResizedImage(Long groupId, Multimedia image) {
        groupDao.setImage(Long.valueOf(groupId), image);
        try {
            Multimedia thumbnail = IOHelper.resizeImage(image, Constants.THUMBNAIL_SIZE_PX);
            groupDao.setThumbnail(Long.valueOf(groupId), thumbnail);
        } catch (IOException ex) {
            LOGGER.error("", ex);
        }
        return image;
    }

    @Override
    public MultimediaDto getImage(String groupId) {
        Multimedia img = groupDao.getImage(Long.valueOf(groupId));
        return mapper.map(img, MultimediaDto.class);
    }

    @Override
    public MultimediaDto getThumbnail(String groupId) {
        Multimedia img = groupDao.getThumbnail(Long.valueOf(groupId));
        return mapper.map(img, MultimediaDto.class);
    }

    @Override
    public UserListDto addMembers(String groupId, BaseListDto<String> members) {
        List<User> users = groupDao.addMembers(Long.valueOf(groupId), members.getData());
        UserListDto listDto = new UserListDto();
        for (User u : users) {
            listDto.add(mapper.map(u, UserDto.class));
        }
        return listDto;
    }

    @Override
    public UserListDto getMembers(String groupId) {
        List<User> list = groupDao.getMembers(Long.valueOf(groupId));
        UserListDto bList = new UserListDto();
        for (User u : list) {
            bList.add(mapper.map(u, UserDto.class));
        }
        return bList;
    }

    @Override
    public void removeMembers(String groupId, String userId) {
        groupDao.removeMember(Long.valueOf(groupId), userId);
    }

    @Override
    public MessageDto getMessage(String groupId, String messageId) {
        Message mess = groupDao.getMessage(Long.valueOf(groupId), Long.valueOf(messageId));
        return mapper.map(mess, MessageDto.class);
    }

    @Override
    public MessageDto addMessage(String groupId, MessageDto message) {
        Message mess = groupDao.addMessage(Long.valueOf(groupId), mapper.map(message, Message.class));
        return mapper.map(mess, MessageDto.class);
    }

    @Override
    public void removeMessage(String groupId, String messageId) {
        groupDao.removeMessage(Long.valueOf(groupId), Long.valueOf(messageId));
    }

    @Override
    public BaseListDto<MessageDto> getMessages(String groupId) {
        List<Message> list = groupDao.getMessages(Long.valueOf(groupId));
        BaseListDto<MessageDto> listDto = new BaseListDto<>();
        for (Message m : list) {
            listDto.add(mapper.map(m, MessageDto.class));
        }
        return listDto;
    }

    @Override
    public EventDto addEvent(String groupId, EventDto eventDto) {
        Event event = mapper.map(eventDto, Event.class);
        Group group = groupDao.getById(Long.valueOf(groupId));
        if (group != null) {
            event.setPrivacy(Privacy.PRIVATE);
            event.setStatus(EventStatus.OPEN);
//            event.setGroup(group);
            event = eventDao.add(event);
            groupDao.addEvent(Long.valueOf(groupId), event);
            eventDao.addMembers(event.getId(), groupDao.getMembers(group.getId()));

            String url;
            if (event.getEventType() == EventType.CHALLENGE) {
                url = getClass().getResource(FilePaths.CHALLENGE_IMAGE).getFile();
            } else {
                url = getClass().getResource(FilePaths.HANGOUT_IMAGE).getFile();
            }
            try {
                Multimedia image = IOHelper.readMultimediaFromFile(url);
                setEventResizedImage(event.getId(), image);
            } catch (IOException e) {
                LOGGER.error("", e);
            }


            return mapper.map(event, EventDto.class);
        }
        return null;
    }

    private Multimedia setEventResizedImage(Long eventId, Multimedia image) {
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
    public BaseListDto<EventDto> getEvents(String groupId) {
        List<Event> events = groupDao.getEvents(Long.valueOf(groupId));
        BaseListDto<EventDto> dtoList = new BaseListDto<>();
        for (Event e : events) {
            dtoList.add(mapper.map(e, EventDto.class));
        }
        return dtoList;
    }

    @Override
    public BaseListDto<GroupDto> search(String query) {
        String[] querys = query.split(" ");
        List<Group> list = groupDao.search(querys);
        BaseListDto<GroupDto> listDto = new BaseListDto<>();
        for (Group g : list) {
            listDto.add(mapper.map(g, GroupDto.class));
        }
        return listDto;
    }

    @Override
    public String getOwnerUsername(String groupId) {
        return groupDao.getOwnerUsername(Long.valueOf(groupId));
    }

    @Override
    public Privacy getPrivacy(String groupId) {
        return groupDao.getPrivacy(Long.valueOf(groupId));
    }
}
