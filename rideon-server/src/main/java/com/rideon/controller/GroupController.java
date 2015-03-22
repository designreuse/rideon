/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.controller;

import com.rideon.model.dto.EventDto;
import com.rideon.model.dto.GroupDto;
import com.rideon.model.dto.MessageDto;
import com.rideon.model.dto.MultimediaDto;
import com.rideon.model.dto.list.BaseListDto;
import com.rideon.model.dto.list.UserListDto;
import com.rideon.model.service.GroupService;
import com.rideon.common.util.RestCommonPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Fer
 */
@Controller
public class GroupController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupController.class);
    @Autowired
    GroupService groupService;

    @RequestMapping(value = RestCommonPaths.GROUP, method = RequestMethod.POST)
    public ResponseEntity<GroupDto> addGroup(@RequestBody GroupDto group) {
        GroupDto createdGroup = groupService.add(group);
        return new ResponseEntity<>(createdGroup, HttpStatus.CREATED);
    }

    @PreAuthorize("@groupPermisions.isOwner(authentication.name, #group.id)")
    @RequestMapping(value = RestCommonPaths.GROUP_BY_ID, method = RequestMethod.PUT)
    public @ResponseBody
    GroupDto updateGroup(@RequestBody GroupDto group) {
        GroupDto createdGroup = groupService.update(group);
        return createdGroup;
    }

    @PreAuthorize("@groupPermisions.isAllowed(authentication.name, #groupId)")
    @RequestMapping(value = RestCommonPaths.GROUP_BY_ID, method = RequestMethod.GET)
    public ResponseEntity<GroupDto> getGroup(@PathVariable(RestCommonPaths.GROUP_ID) String groupId) {
        GroupDto group = groupService.getById(groupId);
        if (group != null) {
            return new ResponseEntity<>(group, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("@userPermisions.isFriendOf(authentication.name, #userId)")
    @RequestMapping(value = RestCommonPaths.USER_GROUPS, method = RequestMethod.GET)
    public @ResponseBody
    BaseListDto<GroupDto> getGroupsByUser(@PathVariable(RestCommonPaths.USER_ID) String userId) {
        BaseListDto<GroupDto> list = groupService.getByUser(userId);
        return list;
    }

    @PreAuthorize("@groupPermisions.isAllowed(authentication.name, #groupId)")
    @RequestMapping(value = RestCommonPaths.GROUP_MEMBERS, method = RequestMethod.GET)
    public @ResponseBody
    UserListDto getGroupsMembers(@PathVariable(RestCommonPaths.GROUP_ID) String groupId) {
        UserListDto list = groupService.getMembers(groupId);
        return list;
    }

    @PreAuthorize("@groupPermisions.isOwner(authentication.name, #groupId)")
    @RequestMapping(value = RestCommonPaths.GROUP_MEMBERS, method = {RequestMethod.POST, RequestMethod.PUT})
    public @ResponseBody
    UserListDto addGroupsMembers(@PathVariable(RestCommonPaths.GROUP_ID) String groupId, @RequestBody BaseListDto<String> userList) {
        UserListDto list = groupService.addMembers(groupId, userList);
        return list;
    }

    @PreAuthorize("@userPermisions.isAllowed(authentication.name, #userId) or @groupPermisions.isOwner(authentication.name, #groupId)")
    @RequestMapping(value = RestCommonPaths.GROUP_MEMBERS_BY_ID, method = RequestMethod.DELETE)
    public ResponseEntity removeGroupsMembers(@PathVariable(RestCommonPaths.GROUP_ID) String groupId,
            @PathVariable(RestCommonPaths.USER_ID) String userId) {
        try {
            groupService.removeMembers(groupId, userId);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("", ex);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("@groupPermisions.isAllowed(authentication.name, #groupId)")
    @RequestMapping(value = RestCommonPaths.GROUP_IMAGE_BY_ID, method = RequestMethod.GET)
    public ResponseEntity<MultimediaDto> getImage(@PathVariable(RestCommonPaths.GROUP_ID) String groupId) {
        MultimediaDto image = groupService.getImage(groupId);
        if (image != null) {
            return new ResponseEntity<>(image, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = RestCommonPaths.GROUP_THUMBNAIL_BY_ID, method = RequestMethod.GET)
    public ResponseEntity<MultimediaDto> getThumbnail(@PathVariable(RestCommonPaths.GROUP_ID) String groupId) {
        MultimediaDto image = groupService.getThumbnail(groupId);
        if (image != null) {
            return new ResponseEntity<>(image, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("@groupPermisions.isOwner(authentication.name, #groupId)")
    @RequestMapping(value = RestCommonPaths.GROUP_IMAGE_BY_ID, method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<MultimediaDto> setImage(@PathVariable(RestCommonPaths.GROUP_ID) String groupId, @RequestBody MultimediaDto img) {
        MultimediaDto image = groupService.setImage(groupId, img);
        if (image != null) {
            return new ResponseEntity<>(image, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("@groupPermisions.isOwner(authentication.name, #groupId)")
    @RequestMapping(value = RestCommonPaths.GROUP_BY_ID, method = RequestMethod.DELETE)
    public ResponseEntity deleteGroup(@PathVariable(RestCommonPaths.GROUP_ID) String groupId) {
        try {
            groupService.remove(groupId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("@groupPermisions.isAllowed(authentication.name, #groupId)")
    @RequestMapping(value = RestCommonPaths.GROUP_MESSAGES, method = RequestMethod.GET)
    public @ResponseBody
    BaseListDto<MessageDto> getGroupsMessages(@PathVariable(RestCommonPaths.GROUP_ID) String groupId) {
        BaseListDto<MessageDto> list = groupService.getMessages(groupId);
        return list;
    }

    @PreAuthorize("@groupPermisions.isMember(authentication.name, #groupId)")
    @RequestMapping(value = RestCommonPaths.GROUP_MESSAGES, method = RequestMethod.POST)
    public @ResponseBody
    MessageDto addGroupsMessages(@PathVariable(RestCommonPaths.GROUP_ID) String groupId, @RequestBody MessageDto message) {
        MessageDto mess = groupService.addMessage(groupId, message);
        return mess;
    }

    @PreAuthorize("@groupPermisions.isMessageOwner(authentication.name, #groupId, #messageId)")
    @RequestMapping(value = RestCommonPaths.GROUP_MESSAGES_BY_ID, method = RequestMethod.DELETE)
    public ResponseEntity deteleGroupsMessage(@PathVariable(RestCommonPaths.GROUP_ID) String groupId, @PathVariable(RestCommonPaths.MESSAGE_ID) String messageId) {
        try {
            groupService.removeMessage(groupId, messageId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = RestCommonPaths.GROUP_EVENTS_BY_ID, method = RequestMethod.POST)
    public @ResponseBody
    EventDto addEvent(@PathVariable(RestCommonPaths.GROUP_ID) String groupId, @RequestBody EventDto eventDto) {
        return groupService.addEvent(groupId, eventDto);
    }

    @PreAuthorize("@groupPermisions.isAllowed(authentication.name, #groupId)")
    @RequestMapping(value = RestCommonPaths.GROUP_EVENTS_BY_ID, method = RequestMethod.GET)
    public @ResponseBody
    BaseListDto<EventDto> getEvents(@PathVariable(RestCommonPaths.GROUP_ID) String groupId) {
        return groupService.getEvents(groupId);
    }
}
