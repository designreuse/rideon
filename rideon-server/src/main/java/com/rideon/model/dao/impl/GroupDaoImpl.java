/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.dao.impl;

import com.rideon.model.dao.EventDao;
import com.rideon.model.dao.GroupDao;
import com.rideon.model.dao.MessageDao;
import com.rideon.model.dao.UserDao;
import com.rideon.common.enums.Privacy;
import com.rideon.model.domain.Event;
import com.rideon.model.domain.Group;
import com.rideon.model.domain.Message;
import com.rideon.model.domain.Multimedia;
import com.rideon.model.domain.User;
import java.util.ArrayList;
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
@Repository(value = "groupDao")
public class GroupDaoImpl extends BaseDaoImpl<Group> implements GroupDao {

    @Autowired
    private UserDao userDao;
    @Autowired
    private MessageDao messageDao;
    @Autowired
    private EventDao eventDao;

    @Override
    @Transactional(readOnly = true)
    public Group getById(Object objectId) {
        Group group = entityManager.find(Group.class, objectId);
        return group;
    }

    @Override
    @Transactional
    public Group update(Group object) {
        Group group = getById(object.getId());
        if (object.getName() != null && !object.getName().isEmpty()) {
            group.setName(object.getName());
        }
        group.setDescription(object.getDescription());
        if (object.getPrivacy() != null) {
            group.setPrivacy(object.getPrivacy());
        }
        if (object.getOwner() != null) {
            group.setOwner(object.getOwner());
        }
        entityManager.merge(group);
        return group;
    }

    @Override
    @Transactional
    public Multimedia setImage(Long groupId, Multimedia image) {
        Group group = getById(groupId);
        group.setImage(image);
        entityManager.merge(group);
        return image;
    }

    @Override
    @Transactional
    public Multimedia setThumbnail(Long groupId, Multimedia image) {
        Group group = getById(groupId);
        group.setThumbnail(image);
        entityManager.merge(group);
        return image;
    }

    @Override
    @Transactional(readOnly = true)
    public Multimedia getImage(Long groupId) {
        Group group = getById(groupId);
        Multimedia img = null;
        if (group != null) {
            img = group.getImage();
            if (img != null) {
                img = entityManager.find(Multimedia.class, img.getId());
            }
        }
        return img;
    }

    @Override
    @Transactional(readOnly = true)
    public Multimedia getThumbnail(Long groupId) {
        Group group = getById(groupId);
        Multimedia img = null;
        if (group != null) {
            img = group.getThumbnail();
            if (img != null) {
                img = entityManager.find(Multimedia.class, img.getId());
            }
        }
        return img;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getMembers(Long groupId) {
        Group group = getById(groupId);
        List<User> mbr = new ArrayList<>();
        for (User member : group.getMembers()) {
            User user = userDao.getById(member.getUsername());
            if (user != null) {
                mbr.add(user);
            }
        }
        return mbr;
    }

    @Override
    @Transactional
    public List<User> addMembers(Long groupId, List<String> userList) {
        Group group = getById(groupId);
        if (group != null) {
            List<User> usersToAdd = new ArrayList<>();
            for (String userId : userList) {
                User user = entityManager.find(User.class, userId);
                if (user != null) {
                    usersToAdd.add(user);
                }
            }
            Set<User> currentList = new HashSet<>();
            currentList.addAll(group.getMembers());
            currentList.addAll(usersToAdd);
            group.setMembers(currentList);
            entityManager.merge(group);
            //Esto está mal, habría que releer toda la lista
            return getMembers(groupId);
        }
        return null;
    }

    @Override
    @Transactional
    public void removeMember(Long groupId, String userId) {
        Group group = getById(groupId);
        User u = new User();
        u.setUsername(userId);

        if (group != null) {
            if (group.getMembers() != null && group.getMembers().contains(u)) {
                group.getMembers().remove(u);

                if (group.getOwner().equals(u)) {
                    if (group.getMembers().size() > 0) {
                        User owner = (new ArrayList<>(group.getMembers())).get(0);
                        group.setOwner(owner);
                    } else {
                        remove(groupId);
                    }
                } else {
                    removeMemberReferences(groupId, userId);
                    entityManager.merge(group);
                }
            }
        }
    }

    @Transactional
    public void removeMemberReferences(Long groupId, String userId) {
        //Messages
        Group group = entityManager.find(Group.class, groupId);
        Hibernate.initialize(group.getMessages());
        User user = new User();
        user.setUsername(userId);
        List<Message> messageList = new ArrayList(group.getMessages());
        for (Message m : messageList) {
            if (m.getOwner().equals(user)) {
                removeMessage(groupId, m.getId());
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Message getMessage(Long groupId, Long messageId) {
        Group group = entityManager.find(Group.class, groupId);
        if (group != null) {
            List<Message> list = group.getMessages();
            for (Message mes : list) {
                if (mes.getId().equals(messageId)) {
                    return messageDao.getById(messageId);
                }
            }
        }
        return null;
    }

    @Override
    @Transactional
    public Message addMessage(Long groupId, Message message) {
        Group group = entityManager.find(Group.class, groupId);
        if (group != null) {
            Hibernate.initialize(group.getMessages());
            List<Message> list = group.getMessages();
            list.add(message);
            entityManager.merge(group);
            return message;
        }
        return null;
    }

    @Override
    @Transactional
    public Message editMessage(Long groupId, Message message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Transactional
    public void removeMessage(Long groupId, Long messageId) {
        Group group = entityManager.find(Group.class, groupId);
        if (group != null) {
            Hibernate.initialize(group.getMessages());
            Message alt = new Message();
            alt.setId(messageId);
            if (group.getMessages().contains(alt)) {
                group.getMessages().remove(alt);
                entityManager.merge(group);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Message> getMessages(Long groupId) {
        Group group = entityManager.find(Group.class, groupId);
        List<Message> list = new ArrayList<>();
        if (group != null) {
            for (Message m : group.getMessages()) {
                list.add(messageDao.getById(m.getId()));
            }
            return list;
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> getEvents(Long groupId) {
        Group group = entityManager.find(Group.class, groupId);
        List<Event> list = new ArrayList<>();
        if (group != null) {
            for (Event e : group.getEvents()) {
                list.add(eventDao.getById(e.getId()));
            }
            return list;
        }
        return null;
    }

    @Override
    @Transactional
    public void addEvent(Long groupId, Event event) {
        Group group = entityManager.find(Group.class, groupId);
        event = entityManager.find(Event.class, event.getId());
        if (group != null && event != null) {
            event.setGroup(group);
            group.getEvents().add(event);
            entityManager.merge(event);
            entityManager.merge(group);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Group> search(String[] query) {
        Set<Group> set = new HashSet<>();
        for (int i = 0; i < query.length; i++) {
            Query q = entityManager.createQuery("Select g.id From groups g Where g.name like '%" + query[i] + "%'");
            List res = q.getResultList();
            Iterator it = res.iterator();
            while (it.hasNext()) {
                Long id = (Long) it.next();
                Group g = getById(id);
                if (isPublicOrMember(g)) {
                    set.add(g);
                }
            }
        }
        return new ArrayList<>(set);
    }

    private Boolean isPublicOrMember(Group g) {
        if (g.getPrivacy() == Privacy.PUBLIC) {
            return true;
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User user = entityManager.find(User.class, name);
        return getMembers(g.getId()).contains(user);
    }

    @Override
    public String getOwnerUsername(Long valueOf) {
        Query q = entityManager.createQuery("Select g.owner.username From groups g Where g.id =" + valueOf);
        return (String) q.getSingleResult();
    }

    @Override
    public Privacy getPrivacy(Long valueOf) {
        Query q = entityManager.createQuery("Select g.privacy From groups g Where g.id =" + valueOf);
        return (Privacy) q.getSingleResult();
    }
}
