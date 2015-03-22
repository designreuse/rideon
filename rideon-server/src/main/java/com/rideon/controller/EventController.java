/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rideon.model.dto.EventDto;
import com.rideon.model.dto.MessageDto;
import com.rideon.model.dto.MultimediaDto;
import com.rideon.model.dto.PracticeDto;
import com.rideon.model.dto.RouteDto;
import com.rideon.model.dto.SegmentDto;
import com.rideon.model.dto.SegmentPracticeDto;
import com.rideon.model.dto.list.BaseListDto;
import com.rideon.model.dto.list.UserListDto;
import com.rideon.model.service.EventService;
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
public class EventController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);
    @Autowired
    EventService eventService;

    @RequestMapping(value = RestCommonPaths.EVENT, method = RequestMethod.POST)
    public ResponseEntity<EventDto> addEvent(@RequestBody EventDto event) {
        EventDto createdEvent = (EventDto) eventService.add(event);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    @PreAuthorize("@eventPermisions.isOwner(authentication.name, #event.id)")
    @RequestMapping(value = RestCommonPaths.EVENT_BY_ID, method = RequestMethod.PUT)
    @ResponseBody
    public EventDto updateEvent(@RequestBody EventDto event) {
        return (EventDto) eventService.update(event);
    }

    @PreAuthorize("@eventPermisions.isOwner(authentication.name, #eventId)")
    @RequestMapping(value = RestCommonPaths.EVENT_BY_ID, method = RequestMethod.DELETE)
    public ResponseEntity remove(@PathVariable(RestCommonPaths.EVENT_ID) String eventId) {
        try {
            eventService.remove(eventId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("@eventPermisions.isAllowed(authentication.name, #eventId)")
    @RequestMapping(value = RestCommonPaths.EVENT_BY_ID, method = RequestMethod.GET)
    public ResponseEntity<EventDto> getEvent(@PathVariable(RestCommonPaths.EVENT_ID) String eventId) {
        EventDto event = (EventDto) eventService.getById(eventId);
        if (event != null) {
            return new ResponseEntity<>(event, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("@userPermisions.isFriendOf(authentication.name, #userId)")
    @RequestMapping(value = RestCommonPaths.USER_EVENTS, method = RequestMethod.GET)
    public @ResponseBody
    BaseListDto<EventDto> getByUser(@PathVariable(RestCommonPaths.USER_ID) String userId) {
        BaseListDto list = eventService.getByUser(userId);
        return list;
    }

    @PreAuthorize("@eventPermisions.isMember(authentication.name, #eventId)")
    @RequestMapping(value = RestCommonPaths.EVENT_ROUTE_PRACTICES, method = RequestMethod.POST)
    public @ResponseBody
    EventDto addPractice(@PathVariable(RestCommonPaths.EVENT_ID) String eventId, @RequestBody PracticeDto practice) {
        return eventService.addPractice(eventId, practice);
    }

    @PreAuthorize("@eventPermisions.isAllowed(authentication.name, #eventId)")
    @RequestMapping(value = RestCommonPaths.EVENT_ROUTE_PRACTICES, method = RequestMethod.GET)
    public @ResponseBody
    BaseListDto<Object> getRouteResults(@PathVariable(RestCommonPaths.EVENT_ID) String eventId) {
        return eventService.getRouteResults(eventId);
    }

    @PreAuthorize("@eventPermisions.isAllowed(authentication.name, #eventId)")
    @RequestMapping(value = RestCommonPaths.EVENT_SEGMENTS_PRACTICES, method = RequestMethod.GET)
    public @ResponseBody
    BaseListDto<SegmentPracticeDto> getSegmentPractice(@PathVariable(RestCommonPaths.EVENT_ID) String eventId) {
        return eventService.getSegmentsResults(eventId);
    }

    @PreAuthorize("@eventPermisions.isOwner(authentication.name, #eventId)")
    @RequestMapping(value = RestCommonPaths.EVENT_ROUTE, method = RequestMethod.POST)
    public @ResponseBody
    EventDto setRoute(@PathVariable(RestCommonPaths.EVENT_ID) String eventId, @RequestBody String routeId) {
        eventService.setRoute(routeId, eventId);
        return eventService.getById(eventId);
    }

    @PreAuthorize("@eventPermisions.isOwner(authentication.name, #eventId)")
    @RequestMapping(value = RestCommonPaths.EVENT_SEGMENT, method = RequestMethod.POST)
    public @ResponseBody
    EventDto setSegment(@PathVariable(RestCommonPaths.EVENT_ID) String eventId, @RequestBody String segmentId) {
        eventService.setSegment(segmentId, eventId);
        return eventService.getById(eventId);
    }

    @PreAuthorize("@eventPermisions.isAllowed(authentication.name, #eventId)")
    @RequestMapping(value = RestCommonPaths.EVENT_ROUTE, method = RequestMethod.GET)
    public @ResponseBody
    String getRoute(@PathVariable(RestCommonPaths.EVENT_ID) String eventId) throws JsonProcessingException {
        RouteDto r = eventService.getRoute(eventId);
        if (r != null) {
            return r.toString();
        } else {
            return null;
        }
    }

    @PreAuthorize("@eventPermisions.isAllowed(authentication.name, #eventId)")
    @RequestMapping(value = RestCommonPaths.EVENT_SEGMENT, method = RequestMethod.GET)
    public @ResponseBody
    String getSegment(@PathVariable(RestCommonPaths.EVENT_ID) String eventId) {
        SegmentDto s = eventService.getSegment(eventId);
        if (s != null) {
            return s.toString();
        } else {
            return null;
        }
    }

    @PreAuthorize("@eventPermisions.isAllowed(authentication.name, #eventId)")
    @RequestMapping(value = RestCommonPaths.EVENT_MEMBERS, method = RequestMethod.GET)
    public @ResponseBody
    UserListDto getMembers(@PathVariable(RestCommonPaths.EVENT_ID) String eventId) {
        UserListDto list = eventService.getMembers(eventId);
        return list;
    }

    @PreAuthorize("@eventPermisions.isOwner(authentication.name, #eventId)")
    @RequestMapping(value = RestCommonPaths.EVENT_MEMBERS, method = {RequestMethod.POST, RequestMethod.PUT})
    public @ResponseBody
    UserListDto addMembers(@PathVariable(RestCommonPaths.EVENT_ID) String eventId, @RequestBody UserListDto userList) {
        UserListDto list = eventService.addMembers(eventId, userList);
        return list;
    }

    @PreAuthorize("@eventPermisions.isMember(authentication.name, #eventId) or @userPermisions.isAllowed(authentication.name, #userId)")
    @RequestMapping(value = RestCommonPaths.EVENT_MEMBERS_BY_ID, method = RequestMethod.DELETE)
    public ResponseEntity removeMembers(@PathVariable(RestCommonPaths.EVENT_ID) String eventId,
            @PathVariable(RestCommonPaths.USER_ID) String userId) {
        try {
            eventService.removeMember(eventId, userId);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error("", ex);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("@eventPermisions.isAllowed(authentication.name, #eventId)")
    @RequestMapping(value = RestCommonPaths.EVENT_IMAGE_BY_ID, method = RequestMethod.GET)
    public ResponseEntity<MultimediaDto> getImage(@PathVariable(RestCommonPaths.EVENT_ID) String eventId) {
        MultimediaDto image = eventService.getImage(eventId);
        if (image != null) {
            return new ResponseEntity<>(image, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = RestCommonPaths.EVENT_THUMBNAIL_BY_ID, method = RequestMethod.GET)
    public ResponseEntity<MultimediaDto> getThumbnail(@PathVariable(RestCommonPaths.EVENT_ID) String eventId) {
        MultimediaDto image = eventService.getThumbnail(eventId);
        if (image != null) {
            return new ResponseEntity<>(image, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("@eventPermisions.isOwner(authentication.name, #eventId)")
    @RequestMapping(value = RestCommonPaths.EVENT_IMAGE_BY_ID, method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<MultimediaDto> setImage(@PathVariable(RestCommonPaths.EVENT_ID) String eventId, @RequestBody MultimediaDto img) {
        MultimediaDto image = eventService.setImage(eventId, img);
        if (image != null) {
            return new ResponseEntity<>(image, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("@eventPermisions.isAllowed(authentication.name, #eventId)")
    @RequestMapping(value = RestCommonPaths.EVENT_MESSAGES, method = RequestMethod.GET)
    public @ResponseBody
    BaseListDto<MessageDto> getMessages(@PathVariable(RestCommonPaths.EVENT_ID) String eventId) {
        BaseListDto<MessageDto> list = eventService.getMessages(eventId);
        return list;
    }

    @PreAuthorize("@eventPermisions.isMember(authentication.name, #eventId)")
    @RequestMapping(value = RestCommonPaths.EVENT_MESSAGES, method = RequestMethod.POST)
    public @ResponseBody
    MessageDto addMessages(@PathVariable(RestCommonPaths.EVENT_ID) String eventId, @RequestBody MessageDto message) {
        MessageDto mess = eventService.addMessage(eventId, message);
        return mess;
    }

    @PreAuthorize("@eventPermisions.isMessageOwner(authentication.name, #messageId)")
    @RequestMapping(value = RestCommonPaths.EVENT_MESSAGES_BY_ID, method = RequestMethod.DELETE)
    public ResponseEntity removeMessage(@PathVariable(RestCommonPaths.EVENT_ID) String eventId, @PathVariable(RestCommonPaths.MESSAGE_ID) String messageId) {
        try {
            eventService.removeMessage(eventId, messageId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
