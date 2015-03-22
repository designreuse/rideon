/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.dao.impl;

import com.rideon.model.dao.EventDao;
import com.rideon.model.dao.RouteDao;
import com.rideon.model.dao.SegmentDao;
import com.rideon.model.dao.UserDao;
import com.rideon.common.enums.Privacy;
import com.rideon.model.domain.Event;
import com.rideon.model.domain.Group;
import com.rideon.model.domain.Message;
import com.rideon.model.domain.Multimedia;
import com.rideon.model.domain.Practice;
import com.rideon.model.domain.Route;
import com.rideon.model.domain.Segment;
import com.rideon.model.domain.SegmentPractice;
import com.rideon.model.domain.User;
import com.rideon.util.PracticeComparator;
import com.rideon.util.SegmentPracticeComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.Query;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Fer
 */
@Repository(value = "challengeDao")
public class EventDaoImpl extends BaseDaoImpl<Event> implements EventDao {

    @Autowired
    UserDao userDao;
    @Autowired
    RouteDao routeDao;
    @Autowired
    SegmentDao segmentDao;

    @Override
    @Transactional
    public Event update(Event object) {
        Event event = getById(object.getId());
        if (object.getName() != null && !object.getName().isEmpty()) {
            event.setName(object.getName());
        }
        if (object.getDescription() != null) {
            event.setDescription(object.getDescription());
        }
        if (object.getOwner() != null) {
            event.setOwner(object.getOwner());
        }
        if (object.getEventDate() != null) {
            event.setEventDate(object.getEventDate());
        }
        if (object.getPractices().isEmpty() && object.getRoute() != null) {
            Route route = routeDao.getById(object.getRoute().getId());
            if (route != null) {
                event.setRoute(route);
            }
        }
        if (object.getPractices().isEmpty() && object.getSegment() != null) {
            Segment segment = segmentDao.getById(object.getSegment().getId());
            if (segment != null) {
                event.setSegment(segment);
            }
        }
        if (object.getStatus() != null) {
            event.setStatus(object.getStatus());
        }
        if (event.getGroup() == null && object.getPrivacy() != null) {
            event.setPrivacy(object.getPrivacy());
        }
        if (object.getUploadAfterDate() != null) {
            event.setUploadAfterDate(object.getUploadAfterDate());
        }

        entityManager.merge(event);
        return event;
    }

    @Override
    @Transactional
    public Multimedia setImage(Long eventId, Multimedia image) {
        Event event = getById(eventId);
        event.setImage(image);
        entityManager.merge(event);
        return image;
    }

    @Override
    @Transactional(readOnly = true)
    public Multimedia getImage(Long eventId) {
        Event event = getById(eventId);
        if (event != null && event.getImage() != null) {
            return entityManager.find(Multimedia.class, event.getImage().getId());
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Multimedia getThumbnail(Long eventId) {
        Event event = getById(eventId);
        if (event != null && event.getThumbnail() != null) {
            return entityManager.find(Multimedia.class, event.getThumbnail().getId());
        }
        return null;
    }

    @Override
    @Transactional
    public Multimedia setThumbnail(Long eventId, Multimedia thumbnail) {
        Event event = getById(eventId);
        event.setThumbnail(thumbnail);
        entityManager.merge(event);
        return thumbnail;
    }

    @Override
    @Transactional
    public List<User> addMembers(Long eventId, List<User> members) {
        Event event = getById(eventId);
        if (event != null) {
            List<User> usersToAdd = new ArrayList<>();
            for (User u : members) {
                User user = entityManager.find(User.class, u.getUsername());
                if (user != null) {
                    usersToAdd.add(user);
                }
            }
            Set<User> currentList = new HashSet<>();
            currentList.addAll(event.getMembers());
            currentList.addAll(usersToAdd);
            event.setMembers(new HashSet(currentList));
            entityManager.merge(event);
            //Esto está mal, habría que releer toda la lista
            return new ArrayList(currentList);
        }
        return null;
    }

    @Override
    @Transactional
    public void removeMember(Long eventId, String memberId) {
        Event event = getById(eventId);
        if (event != null) {
            User member = entityManager.find(User.class, memberId);
            if (member != null) {
                if (event.getMembers() != null && event.getMembers().contains(member)) {
                    event.getMembers().remove(member);

                    if (event.getOwner().equals(member)) {
                        if (event.getMembers().size() > 0) {
                            event.setOwner((new ArrayList<>(event.getMembers())).get(0));
                        } else {
                            remove(eventId);
                        }
                    } else {
                        removeMemberReferences(eventId, memberId);
                        entityManager.merge(event);
                    }

                }
            }
        }
    }

    @Transactional
    public void removeMemberReferences(Long eventId, String userId) {
        //Messages
        Event event = entityManager.find(Event.class, eventId);
        Hibernate.initialize(event.getMessages());
        User messageOwner = entityManager.find(User.class, userId);
        for (Message m : event.getMessages()) {
            if (m.getOwner().equals(messageOwner)) {
                removeMessage(eventId, m.getId());
            }
        }
        //Multimedia
        //Eventos
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getMembers(Long eventId) {
        Event event = getById(eventId);
        Set<User> members = event.getMembers();
        List<User> mbr = new ArrayList<>();
        if (members != null) {
            for (User member : members) {
                User user = userDao.getById(member.getUsername());
                if (user != null) {
                    mbr.add(user);
                }
            }
        }
        return mbr;
    }

    @Override
    @Transactional
    public Message addMessage(Long eventId, Message message) {
        Event event = entityManager.find(Event.class, eventId);
        if (event != null) {
            Hibernate.initialize(event.getMessages());
            Set<Message> list = event.getMessages();
            list.add(message);
            entityManager.merge(event);
            return message;
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Message getMessage(Long eventId, Long messageId) {
        Event event = entityManager.find(Event.class, eventId);
        if (event != null && event.getMessages() != null) {
            Message m = entityManager.find(Message.class, messageId);
            if (m != null && event.getMessages().contains(m)) {
                return m;
            }
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Message> getMessages(Long eventId) {
        Event event = entityManager.find(Event.class, eventId);
        if (event != null && event.getMessages() != null) {
            List<Message> list = new ArrayList<>();
            for (Message m : event.getMessages()) {
                list.add(entityManager.find(Message.class, m.getId()));
            }
            return list;
        }
        return null;
    }

    @Override
    @Transactional
    public void removeMessage(Long eventId, Long messageId) {
        Event event = entityManager.find(Event.class, eventId);
        if (event != null && event.getMessages() != null) {
            Message m = entityManager.find(Message.class, messageId);
            if (m != null && event.getMessages().contains(m)) {
                event.getMessages().remove(m);
                entityManager.merge(event);
            }
        }
    }

    @Override
    @Transactional
    public Route setRoute(Long route, Long eventId) {
        Event event = entityManager.find(Event.class, eventId);
        if (event != null) {
            Route r = entityManager.find(Route.class, route);
            if (r != null) {
                event.setSegment(null);
                event.setRoute(r);
                entityManager.merge(event);
                return r;
            }
        }
        return null;
    }

    @Override
    @Transactional
    public Segment setSegment(Long segment, Long eventId) {
        Event event = entityManager.find(Event.class, eventId);
        if (event != null) {
            Segment s = entityManager.find(Segment.class, segment);
            if (s != null) {
                event.setRoute(null);
                event.setSegment(s);
                entityManager.merge(event);
                return s;
            }
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Object getRoute(Long eventId) {
        Event event = entityManager.find(Event.class, eventId);
        Object ret = null;
        if (event != null) {
            if (event.getRoute() != null) {
                Long id = event.getRoute().getId();
                ret = entityManager.find(Route.class, id);
            } else if (event.getSegment() != null) {
                ret = entityManager.find(Segment.class, event.getSegment().getId());
            }
        }
        return ret;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Practice> getRouteResults(Long eventId) {
        Event event = entityManager.find(Event.class, eventId);
        if (event != null && event.getRoute() != null) {
            ArrayList<Practice> results = new ArrayList<>(event.getPractices());
            Collections.sort(results, new PracticeComparator());
            return results;
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SegmentPractice> getSegmentResults(Long valueOf) {
        Event event = entityManager.find(Event.class, valueOf);
        if (event != null && event.getSegment() != null) {
            ArrayList<Practice> results = new ArrayList<>(event.getPractices());
            List<SegmentPractice> sp = new ArrayList<>();

            for (Practice pr : results) {
                for (SegmentPractice spr : pr.getSegmentsResults()) {
                    if (spr.getId().getSegmentId().equals(event.getSegment().getId())) {
                        sp.add(spr);
                    }
                }
            }
            Collections.sort(sp, new SegmentPracticeComparator());
            return sp;
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> search(String[] query) {
        Set<Event> set = new HashSet<>();
        for (int i = 0; i < query.length; i++) {
            Query q = entityManager.createQuery("Select e.id From events e Where e.name like '%" + query[i] + "%' OR e.description like '%" + query[i] + "%'");
            List res = q.getResultList();
            Iterator it = res.iterator();
            while (it.hasNext()) {
                Long id = (Long) it.next();
                set.add(getById(id));
            }
        }
        return new ArrayList<>(set);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> search(Date f, Date t) {
        String query = "select e.id from events e where e.eventDate >= '" + f + "' and e.eventDate <= '" + t + "'";
        Query q = entityManager.createQuery(query);
        List resultList = q.getResultList();
        Iterator itr = resultList.iterator();
        List<Event> events = new ArrayList<>();
        while (itr.hasNext()) {
            Long id = (Long) itr.next();
            Event event = entityManager.find(Event.class, id);
            if (isMemberOrPublic(event)) {
                events.add(event);
            }
        }
        return events;
    }

    private boolean isMemberOrPublic(Event e) {
        if (e.getPrivacy() == Privacy.PUBLIC) {
            return true;
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User user = entityManager.find(User.class, name);
        return getMembers(e.getId()).contains(user);
    }

    @Override
    @Transactional
    public Practice addPractice(Long eventId, Practice practice) {
        Event event = entityManager.find(Event.class, eventId);
        practice = entityManager.find(Practice.class, practice.getId());
        if (event != null && practice != null) {
            event.getPractices().add(practice);
            entityManager.merge(event);
        }
        return practice;
    }

    @Override
    @Transactional
    public void remove(Object objectId) {
        Event e = entityManager.find(Event.class, objectId);
        if (e.getGroup() != null) {
            Group g = entityManager.find(Group.class, e.getGroup().getId());
            g.getEvents().remove(e);
            entityManager.merge(g);
        }
        super.remove(objectId); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Transactional
    public void removePractice(Long practiceId) {
        Practice pr = entityManager.find(Practice.class, practiceId);
        String query = "select e.id from events e";
        Query q = entityManager.createQuery(query);
        List resultList = q.getResultList();
        Iterator itr = resultList.iterator();
        while (itr.hasNext()) {
            Long id = (Long) itr.next();
            Event event = entityManager.find(Event.class, id);
            if (event.getPractices().contains(pr)) {
                event.getPractices().remove(pr);
                entityManager.merge(event);
            }
        }
    }

    @Override
    public String getOwnerUsername(Long valueOf) {
        Query q = entityManager.createQuery("Select e.owner.username From events e Where e.id =" + valueOf);
        return (String) q.getSingleResult();
    }

    @Override
    public Privacy getPrivacy(Long valueOf) {
        Query q = entityManager.createQuery("Select e.privacy From events e Where e.id =" + valueOf);
        return (Privacy) q.getSingleResult();
    }
}
