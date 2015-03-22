/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.dao;

import com.rideon.model.dto.NotificationDto;
import com.rideon.model.domain.Bicycle;
import com.rideon.model.domain.Event;
import com.rideon.model.domain.Group;
import com.rideon.model.domain.Multimedia;
import com.rideon.model.domain.Practice;
import com.rideon.model.domain.User;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Fer
 */
public interface UserDao extends BaseDao<User> {

    void removeUserReference(String userId);

    Bicycle addBicycle(String userId, Bicycle bike);

    boolean removeBicycle(String userId, Long bikeId);

    Bicycle updateBicycle(String userId, Bicycle bike);

    List<Bicycle> getBycicles(String userId);

    Bicycle getBycicle(String userId, Long bikeId);

    public Bicycle getPrincipalBycicle(String userId);

    Multimedia getBycicleImage(String userId, Long bikeId);

    Multimedia setBycicleImage(String userId, Long bikeId, Multimedia image);

    List<Group> getGroups(String userId);

    Multimedia setImage(String userId, Multimedia image);

    Multimedia setThumbnail(String userId, Multimedia image);

    Multimedia getImage(String userId);

    List<User> getFriends(String userId);

    List<NotificationDto> getNotifications(String userId);

    List<User> addFriend(String petitionerId, String targetId);

    List<User> search(String[] query);

    public void removeFriend(String userId, String friendId);

    public Set<Practice> getPractices(String userId);

    public Set<Event> getEvents(String userId);

    public Multimedia getThumbnail(String userId);
}
