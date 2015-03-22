/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.dao;

import com.rideon.common.enums.Privacy;
import com.rideon.model.domain.Event;
import com.rideon.model.domain.Group;
import com.rideon.model.domain.Message;
import com.rideon.model.domain.Multimedia;
import com.rideon.model.domain.User;
import java.util.List;

/**
 *
 * @author Fer
 */
public interface GroupDao extends BaseDao<Group> {

    public Multimedia setImage(Long groupId, Multimedia image);

    public Multimedia setThumbnail(Long groupId, Multimedia image);

    public Multimedia getImage(Long groupId);

    public List<User> getMembers(Long groupId);

    public List<User> addMembers(Long groupId, List<String> userList);

    public void removeMember(Long groupId, String userId);

    public Message getMessage(Long groupId, Long messageId);

    public Message addMessage(Long groupId, Message message);

    public Message editMessage(Long groupId, Message message);

    public void removeMessage(Long groupId, Long messageId);

    public List<Message> getMessages(Long groupId);

    public Multimedia getThumbnail(Long valueOf);

    public List<Event> getEvents(Long valueOf);

    public void addEvent(Long groupId, Event event);

    public List<Group> search(String[] query);

    public String getOwnerUsername(Long valueOf);

    public Privacy getPrivacy(Long valueOf);
}
