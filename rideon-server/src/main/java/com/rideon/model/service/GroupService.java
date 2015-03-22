/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.service;

import com.rideon.model.dto.EventDto;
import com.rideon.model.dto.GroupDto;
import com.rideon.model.dto.MessageDto;
import com.rideon.model.dto.MultimediaDto;
import com.rideon.model.dto.list.BaseListDto;
import com.rideon.model.dto.list.UserListDto;
import com.rideon.common.enums.Privacy;

/**
 *
 * @author Fer
 */
public interface GroupService {

    public GroupDto getById(String groupId);

    public BaseListDto<GroupDto> getByUser(String userId);

    public GroupDto add(GroupDto group);

    public void remove(String groupId);

    public GroupDto update(GroupDto group);

    public MultimediaDto setImage(String groupId, MultimediaDto image);

    public MultimediaDto getImage(String groupId);

    public String getOwnerUsername(String groupId);

    public UserListDto getMembers(String groupId);

    public UserListDto addMembers(String groupId, BaseListDto<String> userBaseListDto);

    public void removeMembers(String groupId, String userId);

    public MessageDto getMessage(String groupId, String messageId);

    public MessageDto addMessage(String groupId, MessageDto message);

    public void removeMessage(String groupId, String messageId);

    public BaseListDto<MessageDto> getMessages(String groupId);

    public MultimediaDto getThumbnail(String groupId);

    public EventDto addEvent(String groupId, EventDto eventDto);

    public BaseListDto<EventDto> getEvents(String groupId);

    public BaseListDto<GroupDto> search(String query);

    public Privacy getPrivacy(String groupId);
}
