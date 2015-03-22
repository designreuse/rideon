/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.dao.impl;

import com.rideon.model.dao.FriendshipRequestDao;
import com.rideon.model.dao.GroupDao;
import com.rideon.model.dao.UserDao;
import com.rideon.model.dto.FriendshipRequestDto;
import com.rideon.model.dto.NotificationDto;
import com.rideon.model.dto.UserDto;
import com.rideon.model.domain.Bicycle;
import com.rideon.model.domain.FriendshipRequest;
import com.rideon.model.domain.Group;
import com.rideon.model.domain.Multimedia;
import com.rideon.model.domain.Practice;
import com.rideon.model.domain.User;
import com.rideon.common.enums.NotificationStatus;
import com.rideon.common.enums.RequestStatus;
import com.rideon.model.domain.Event;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.Query;
import ma.glasnost.orika.MapperFacade;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Fer
 */
@Repository(value = "userDao")
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {

    private Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);
    @Autowired
    GroupDao groupDao;
    @Autowired
    FriendshipRequestDao friendshipRequestDao;
    @Autowired
    MapperFacade mapper;

    @Override
    @Transactional
    public User getById(Object objectId) {
        User user = entityManager.find(User.class, objectId);
        if (user != null) {
            Hibernate.initialize(user.getThumbnail());
        }
        return user;
    }

    @Override
    @Transactional
    public User update(User object) {
        User user = entityManager.find(User.class, object.getUsername());
        user.setEmail(object.getEmail());
        user.setFullName(object.getFullName());
        user.setBornDate(object.getBornDate());
        user.setCountry(object.getCountry());
        user.setProvince(object.getProvince());
        user.setTown(object.getTown());
        entityManager.merge(user);
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bicycle> getBycicles(String userId) {
        User user = entityManager.find(User.class, userId);
        List<Bicycle> bikeList = new ArrayList<>();
        if (user != null) {
            for (Bicycle bike : user.getBicycles()) {
                Bicycle b = entityManager.find(Bicycle.class, bike.getId());
                if (b != null) {
                    bikeList.add(b);
                }
            }
        }
        return bikeList;
    }

    @Override
    @Transactional(readOnly = true)
    public Bicycle getPrincipalBycicle(String userId) {
        User user = entityManager.find(User.class, userId);
        if (user != null) {
            for (Bicycle bike : user.getBicycles()) {
                if (bike.getIsPrincipal()) {
                   return entityManager.find(Bicycle.class, bike.getId());
                }
            }
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Group> getGroups(String userId) {
        User user = entityManager.find(User.class, userId);
        List<Group> groupList = new ArrayList<>();
        if (user != null) {
            for (Group group : user.getGroups()) {
                Group g = groupDao.getById(group.getId());
                if (g != null) {
                    groupList.add(g);
                }
            }
        }
        return groupList;
    }

    @Override
    @Transactional
    public Bicycle addBicycle(String userId, Bicycle bike) {
        User user = entityManager.find(User.class, userId);
        if (bike.getIsPrincipal()) {
            for (Bicycle b : user.getBicycles()) {
                b.setIsPrincipal(Boolean.FALSE);
            }
        }
        entityManager.persist(bike);
        entityManager.flush();
        user.getBicycles().add(bike);
        entityManager.merge(user);
        return bike;
    }

    @Override
    @Transactional
    public Bicycle updateBicycle(String userId, Bicycle bike) {
        User user = entityManager.find(User.class, userId);
        Bicycle bikeSt = entityManager.find(Bicycle.class, bike.getId());
        Set<Bicycle> bikeList = user.getBicycles();

        if (bikeList.contains(bikeSt)) {

            if (bike.getIsPrincipal()) {
                for (Bicycle b : user.getBicycles()) {
                    if (!b.equals(bikeSt)) {
                        b.setIsPrincipal(Boolean.FALSE);
                    }
                }
                bikeSt.setIsPrincipal(Boolean.TRUE);
            }

            if (bike.getBrand() != null) {
                bikeSt.setBrand(bike.getBrand());
            }
            if (bike.getModel() != null) {
                bikeSt.setModel(bike.getModel());
            }
            if (bike.getBuyDate() != null) {
                bikeSt.setBuyDate(bike.getBuyDate());
            }
            if (bike.getKilometers() != null) {
                bikeSt.setKilometers(bike.getKilometers());
            }
            if (bike.getIsPrincipal() != null) {
                bikeSt.setIsPrincipal(bike.getIsPrincipal());
            }
            entityManager.merge(bikeSt);
            return bike;
        }
        return null;

    }

    @Override
    @Transactional
    public boolean removeBicycle(String userId, Long bikeId) {
        User user = entityManager.find(User.class, userId);
        Set<Bicycle> bikeList = user.getBicycles();
        Bicycle aux = new Bicycle();
        aux.setId(bikeId);
        if (bikeList.contains(aux)) {
            bikeList.remove(aux);
            entityManager.merge(user);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public Bicycle getBycicle(String userId, Long bikeId) {
        User user = entityManager.find(User.class, userId);
        Bicycle bike = entityManager.find(Bicycle.class, bikeId);
        if (user != null && bike != null) {
            if (user.getBicycles().contains(bike)) {
                return bike;
            }
        }

        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Multimedia getBycicleImage(String userId, Long bikeId) {
        User user = entityManager.find(User.class, userId);
        Bicycle bike = entityManager.find(Bicycle.class, bikeId);
        if (user != null && bike != null && user.getBicycles().contains(bike)) {
            return entityManager.find(Multimedia.class, bike.getImage().getId());
        }
        return null;
    }

    @Override
    @Transactional
    public Multimedia setBycicleImage(String userId, Long bikeId, Multimedia image) {
        User user = entityManager.find(User.class, userId);
        Bicycle bike = entityManager.find(Bicycle.class, bikeId);
        if (user != null && bike != null && user.getBicycles().contains(bike)) {
            bike = entityManager.find(Bicycle.class, bikeId);
            bike.setImage(image);
            entityManager.merge(bike);
            image = bike.getImage();
            return image;
        }
        return null;
    }

    @Override
    @Transactional
    public Multimedia setImage(String userId, Multimedia image) {
        User user = entityManager.find(User.class, userId);
        if (user != null) {
            user.setImage(image);
            entityManager.merge(user);
        }
        return image;
    }

    @Override
    @Transactional
    public Multimedia setThumbnail(String userId, Multimedia image) {
        User user = entityManager.find(User.class, userId);
        if (user != null) {
            user.setThumbnail(image);
            entityManager.merge(user);
        }
        return image;
    }

    @Override
    @Transactional
    public Multimedia getImage(String userId) {
        User user = entityManager.find(User.class, userId);
        if (user != null) {
            Multimedia img = entityManager.find(Multimedia.class, user.getImage().getId());
            return img;
        }
        return null;
    }

    @Override
    @Transactional
    public List<User> getFriends(String userId) {
        User user = entityManager.find(User.class, userId);
        List<User> friendList = new ArrayList<>();
        if (user != null) {
            for (User f : user.getFriends()) {
                User friend = getById(f.getUsername());
                friendList.add(friend);
            }
        }
        return friendList;
    }

    @Override
    @Transactional
    public List<User> addFriend(String petitionerId, String targetId) {
        User petitioner = getById(petitionerId);
        User target = getById(targetId);
        addFriendTo(petitioner, target);
        return null;
    }

    @Transactional
    private void addFriendTo(User user, User friend) {
        user.getFriendOf().add(friend);
        user.getFriends().add(friend);
        entityManager.merge(user);
    }

    @Override
    @Transactional
    public List<User> search(String[] query) {
        Set<User> set = new HashSet<>();
        for (int i = 0; i < query.length; i++) {
            Query q = entityManager.createQuery("Select u.username From users u Where u.username like '%" + query[i] + "%' OR u.fullName like '%" + query[i] + "%'");
            List res = q.getResultList();
            Iterator it = res.iterator();
            while (it.hasNext()) {
                String username = (String) it.next();
                set.add(getById(username));
            }
        }
        return new ArrayList<>(set);
    }

    @Override
    @Transactional
    public void remove(Object objectId) {
        User user = entityManager.find(User.class, objectId);
        if (user != null) {
            removeUserReference(user.getUsername());
            entityManager.remove(user);
        }
    }

    @Override
    @Transactional
    public void removeUserReference(String userId) {
        User user = entityManager.find(User.class, userId);
        //Grupos
        for (Group g : user.getGroups()) {
            groupDao.removeMember(g.getId(), userId);
        }
        //Eventos
        //Rutas
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDto> getNotifications(String userId) {
        String query = "Select r.id From friendship_request r Where r.target.username = '" + userId
                + "' and r.notificationStatus = '" + NotificationStatus.UNREADED.ordinal()
                + "' and requestStatus = '" + RequestStatus.NOT_ANSWERED.ordinal() + "'";
        Query q = entityManager.createQuery(query);
        List res = q.getResultList();
        Iterator it = res.iterator();
        Set<NotificationDto> set = new HashSet<>();
        while (it.hasNext()) {
            Long rId = (Long) it.next();
            FriendshipRequest request = friendshipRequestDao.getById(rId);
            NotificationDto notification = new NotificationDto();
            notification.setTarget(mapper.map(request.getTarget(), UserDto.class));
            notification.setRequest(mapper.map(request, FriendshipRequestDto.class));
            set.add(notification);
        }

        q = entityManager.createQuery("Select r.id From friendship_request r Where r.petitioner.username = '" + userId + "'"
                + "and r.notificationStatus = '" + NotificationStatus.UNREADED.ordinal() + "' and r.requestStatus = '" + RequestStatus.ACCEPTED.ordinal() + "'");
        res = q.getResultList();
        it = res.iterator();
        while (it.hasNext()) {
            Long rId = (Long) it.next();
            FriendshipRequest request = friendshipRequestDao.getById(rId);
            NotificationDto notification = new NotificationDto();
            notification.setTarget(mapper.map(request.getPetitioner(), UserDto.class));
            notification.setRequest(mapper.map(request, FriendshipRequestDto.class));
            set.add(notification);
        }
        return new ArrayList(set);
    }

    @Override
    @Transactional
    public void removeFriend(String userId, String friendId) {
        User user = entityManager.find(User.class, userId);
        User friend = entityManager.find(User.class, friendId);

        if (user != null && friend != null) {
            if (user.getFriendOf().contains(friend)) {
                user.getFriendOf().remove(friend);
            }
            if (user.getFriends().contains(friend)) {
                user.getFriends().remove(friend);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Practice> getPractices(String userId) {
        User user = entityManager.find(User.class, userId);
        Set<Practice> practices = user.getPractices();
        Set<Practice> returnSet = new HashSet<>();
        for (Practice pr : practices) {
            Practice p = entityManager.find(Practice.class, pr.getId());
            returnSet.add(p);
        }
//        Hibernate.initialize(user.getPractices());
        return user.getPractices();
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Event> getEvents(String userId) {
        User user = entityManager.find(User.class, userId);
        Set<Event> events = user.getEvents();
        Set<Event> chanllengues = new HashSet<>();
        for (Event e : events) {
            Event h = entityManager.find(Event.class, e.getId());
            if (h != null) {
                chanllengues.add(h);
            }
        }
        return chanllengues;
    }

    @Override
    @Transactional
    public Multimedia getThumbnail(String userId) {
        User user = entityManager.find(User.class, userId);
        if (user != null) {
            return entityManager.find(Multimedia.class, user.getThumbnail().getId());
        }
        return null;
    }
}
