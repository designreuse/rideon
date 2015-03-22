/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.service;

import com.rideon.model.dto.EventDto;
import com.rideon.model.dto.MessageDto;
import com.rideon.model.dto.MultimediaDto;
import com.rideon.model.dto.PracticeDto;
import com.rideon.model.dto.RouteDto;
import com.rideon.model.dto.SegmentDto;
import com.rideon.model.dto.SegmentPracticeDto;
import com.rideon.model.dto.list.BaseListDto;
import com.rideon.model.dto.list.EventListDto;
import com.rideon.model.dto.list.UserListDto;
import com.rideon.common.enums.Privacy;

/**
 *
 * @author Fer
 */
public interface EventService {

    public EventDto add(EventDto event);

    public EventDto update(EventDto event);

    public void remove(String eventId);

    public EventDto getById(String eventId);

    public RouteDto setRoute(String routeId, String eventId);

    public SegmentDto setSegment(String segmentId, String eventId);

    public RouteDto getRoute(String eventId);

    public SegmentDto getSegment(String eventId);

    public EventListDto getByUser(String userId);

    public MultimediaDto setImage(String eventId, MultimediaDto image);

    public MultimediaDto getImage(String eventId);

    public MultimediaDto getThumbnail(String eventId);

    public EventDto addPractice(String eventId, PracticeDto practice);

    public UserListDto addMembers(String eventId, UserListDto members);

    public UserListDto getMembers(String eventId);

    public void removeMember(String eventId, String memberId);

    public MessageDto getMessage(String eventId, String messageId);

    public MessageDto addMessage(String eventId, MessageDto message);

    public void removeMessage(String eventId, String messageId);

    public BaseListDto<MessageDto> getMessages(String eventId);

    public BaseListDto<Object> getRouteResults(String eventId);

    public BaseListDto<SegmentPracticeDto> getSegmentsResults(String eventId);

    public EventListDto search(String query);

    public EventListDto search(String from, String to);

    public String getOwnerUsername(String groupId);

    public Privacy getPrivacy(String groupId);
}
