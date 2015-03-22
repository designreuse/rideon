/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.web.controller;

import com.rideon.web.util.JSPWebPaths;
import com.rideon.model.dto.EventDto;
import com.rideon.model.dto.LineStringDto;
import com.rideon.model.dto.MessageDto;
import com.rideon.model.dto.MultimediaDto;
import com.rideon.model.dto.PracticeDto;
import com.rideon.model.dto.RouteDto;
import com.rideon.model.dto.SegmentDto;
import com.rideon.model.dto.UserDto;
import com.rideon.model.dto.list.BaseListDto;
import com.rideon.model.dto.list.UserListDto;
import com.rideon.common.enums.EventType;
import com.rideon.common.util.RestCommonPaths;
import com.rideon.web.vo.EventForm;
import com.rideon.web.vo.GPXForm;
import com.rideon.web.util.UrlConstructorSW;
import com.rideon.web.util.UrlConstructorSWImpl;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Fer
 */
@Controller
public class EventsController extends GenericAbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventsController.class);

    @RequestMapping(value = "/events", method = RequestMethod.GET)
    public ModelAndView getPracticesPage(HttpServletRequest request) {
        ModelAndView model = new ModelAndView(JSPWebPaths.EVENTS);

        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.USER_EVENTS);
        Map<String, String> args = new HashMap<>();

        args.put(RestCommonPaths.USER_ID, request.getUserPrincipal().getName());
        BaseListDto<EventDto> response = getRestTemplate().getForEntity(url.getUrl(), BaseListDto.class, args).getBody();
        List<EventDto> events = response.getData();

        model.addObject("events", events);
        return model;
    }

    @RequestMapping(value = "/events/{" + RestCommonPaths.EVENT_ID + "}", method = RequestMethod.GET)
    public ModelAndView getEventPage(@PathVariable(RestCommonPaths.EVENT_ID) String eventId) {

        EventDto event = getEvent(eventId);
        UserListDto members = getMembers(eventId);
        UserDto currentUser = new UserDto();
        currentUser.setUsername(getCurrentUser().getUsername());

        ModelAndView model;
        if (event.getEventType() == EventType.CHALLENGE) {

            if (event.getOwner().equals(currentUser) || getCurrentUser().getUsername().equals("admin")) {
                model = new ModelAndView(JSPWebPaths.CHALLENGE_ADMIN);
            } else if (members.contains(currentUser)) {
                model = new ModelAndView(JSPWebPaths.CHALLENGE_MEMBER);
            } else {
                model = new ModelAndView(JSPWebPaths.CHALLENGE_NOMEMBER);
            }
            List<Object> results = null;
            results = getRouteResutls(eventId);
            model.addObject("results", results);
        } else {
            if (event.getOwner().equals(currentUser) || getCurrentUser().getUsername().equals("admin")) {
                model = new ModelAndView(JSPWebPaths.HANGOUT_ADMIN);
            } else if (members.contains(currentUser)) {
                model = new ModelAndView(JSPWebPaths.HANGOUT_MEMBER);
            } else {
                model = new ModelAndView(JSPWebPaths.HANGOUT_NOMEMBER);
            }
        }
        model.addObject("event", event);
        model.addObject("members", getMembers(eventId));
        model.addObject("message", new MessageDto());
        model.addObject("messages", getMessages(eventId));
        model.addObject("gpxForm", new GPXForm());


        return model;
    }

    @RequestMapping(value = "/events/{" + RestCommonPaths.EVENT_ID + "}/routes.json", method = RequestMethod.GET)
    public @ResponseBody
    Object getEventRouteJson(@PathVariable(RestCommonPaths.EVENT_ID) String eventId) {
        return getRoute(eventId);
    }

    @RequestMapping(value = "/events/{" + RestCommonPaths.EVENT_ID + "}/routes", method = RequestMethod.GET)
    public ModelAndView addRoutePage(@PathVariable(RestCommonPaths.EVENT_ID) String eventId) {
        return new ModelAndView(JSPWebPaths.EVENT_SELECT_ROUTE);
    }

    @RequestMapping(value = "/events/{" + RestCommonPaths.EVENT_ID + "}/segments", method = RequestMethod.GET)
    public ModelAndView addSegmentPage(@PathVariable(RestCommonPaths.EVENT_ID) String eventId) {
        return new ModelAndView(JSPWebPaths.EVENT_SELECT_SEGMENT);
    }

    @RequestMapping(value = "/events/{" + RestCommonPaths.EVENT_ID + "}/routes", method = RequestMethod.POST)
    public String addRoute(@PathVariable(RestCommonPaths.EVENT_ID) String eventId, String routeId) {
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.EVENT_ROUTE);

        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.EVENT_ID, eventId);

        getRestTemplate().postForEntity(url.getUrl(), routeId, EventDto.class, args);

        return "redirect:/events/" + eventId;
    }

    @RequestMapping(value = "/events/{" + RestCommonPaths.EVENT_ID + "}/segments", method = RequestMethod.POST)
    public String addSegment(@PathVariable(RestCommonPaths.EVENT_ID) String eventId, String segmentId) {
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.EVENT_SEGMENT);

        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.EVENT_ID, eventId);

        getRestTemplate().postForEntity(url.getUrl(), segmentId, EventDto.class, args);

        return "redirect:/events/" + eventId;
    }

    @RequestMapping(value = "/events/new", method = RequestMethod.GET)
    public ModelAndView getAddEventPage() {
        ModelAndView model = new ModelAndView(JSPWebPaths.EVENT_ADD);
        model.addObject("event", new EventForm());
        return model;
    }

    @RequestMapping(value = "/events/new", method = RequestMethod.POST)
    public String addEvent(EventForm form, HttpServletRequest request) throws IOException, ParseException {

        UserDto owner = new UserDto();
        owner.setUsername(request.getUserPrincipal().getName());
        form.setOwner(owner);

        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.EVENT);
        EventDto eventDto = form.toEventDto();

        ResponseEntity<EventDto> response = getRestTemplate().postForEntity(url.getUrl(), eventDto, EventDto.class);
        eventDto = (EventDto) response.getBody();

        if (response.getStatusCode() == HttpStatus.CREATED) {
            if (form.getImage() != null) {
                MultimediaDto image = new MultimediaDto();
                image.setDataArray(form.getImage().getBytes());
                url = new UrlConstructorSWImpl(getUrlBase());
                url.addParameter(RestCommonPaths.EVENT_IMAGE_BY_ID);

                Map<String, String> args = new HashMap<>();
                args.put(RestCommonPaths.EVENT_ID, eventDto.getId().toString());
                getRestTemplate().put(url.getUrl(), image, args);
            }

            return "redirect:/events/" + response.getBody().getId();
        } else {
            return "redirect:/events";
        }
    }

    @RequestMapping(value = "/events/{" + RestCommonPaths.EVENT_ID + "}/members", method = RequestMethod.GET)
    public ModelAndView getAddMembersPage(@PathVariable(RestCommonPaths.EVENT_ID) String eventId, HttpServletRequest request) {

        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.EVENT_MEMBERS);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.EVENT_ID, eventId);
        BaseListDto<UserDto> members = getRestTemplate().getForEntity(url.getUrl(), BaseListDto.class, args).getBody();

        ModelAndView model = new ModelAndView(JSPWebPaths.EVENT_ADD_MEMBERS);
        model.addObject("event", getEvent(eventId));
        model.addObject("members", members);
        return model;
    }

    @RequestMapping(value = "/events/{" + RestCommonPaths.EVENT_ID + "}/practices", method = RequestMethod.POST)
    public String addPractice(@PathVariable(RestCommonPaths.EVENT_ID) String eventId, GPXForm form) throws IOException {
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.USER_PRACTICES);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.USER_ID, getCurrentUser().getUsername());

        String gpx = new String(form.getFile().getBytes());
        LineStringDto dto = new LineStringDto();
        dto.setGpx(gpx);
        PracticeDto dtp = getRestTemplate().postForEntity(url.getUrl(), dto, PracticeDto.class, args).getBody();

        url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.EVENT_ROUTE_PRACTICES);
        args = new HashMap<>();
        args.put(RestCommonPaths.EVENT_ID, eventId);
        getRestTemplate().postForEntity(url.getUrl(), dtp, EventDto.class, args);

        return "redirect:/events/" + eventId;
    }

    @RequestMapping(value = "/events/{" + RestCommonPaths.EVENT_ID + "}/members/add", method = RequestMethod.POST)
    public ResponseEntity addMembers(@PathVariable(RestCommonPaths.EVENT_ID) String eventId, @RequestBody BaseListDto<String> usersId) {
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.EVENT_MEMBERS);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.EVENT_ID, eventId);

        BaseListDto<UserDto> userList = new BaseListDto<>();
        for (String s : usersId) {
            UserDto u = new UserDto();
            u.setUsername(s);
            userList.add(u);
        }

        ResponseEntity<BaseListDto> res = getRestTemplate().postForEntity(url.getUrl(), userList, BaseListDto.class, args);
        return res;
    }

    @RequestMapping(value = "/events/{" + RestCommonPaths.EVENT_ID + "}/members/{" + RestCommonPaths.USER_ID + "}", method = RequestMethod.DELETE)
    public @ResponseBody
    String removeMembers(@PathVariable(RestCommonPaths.EVENT_ID) String eventId,
            @PathVariable(RestCommonPaths.USER_ID) String userId) {
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.EVENT_MEMBERS_BY_ID);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.EVENT_ID, eventId);
        args.put(RestCommonPaths.USER_ID, userId);

        getRestTemplate().delete(url.getUrl(), args);
        return "User Removed";
    }

    @RequestMapping(value = "/events/{" + RestCommonPaths.EVENT_ID + "}/edit", method = RequestMethod.GET)
    public ModelAndView getEditEventPage(@PathVariable(RestCommonPaths.EVENT_ID) String eventId) {

        ModelAndView model = new ModelAndView(JSPWebPaths.EVENT_EDIT);
        model.addObject("eventForm", new EventForm(getEvent(eventId)));
        model.addObject("members", getMembers(eventId));
        return model;
    }

    @RequestMapping(value = "/events/{" + RestCommonPaths.EVENT_ID + "}/edit", method = RequestMethod.POST)
    public String editEvent(@PathVariable(RestCommonPaths.EVENT_ID) String eventId, EventForm eventFrom) throws IOException, ParseException {

        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.EVENT_BY_ID);
        //Update bicycle info
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.EVENT_ID, eventId);
        getRestTemplate().put(url.getUrl(), eventFrom.toEventDto(), args);

        if (!eventFrom.getImage().isEmpty()) {
            MultimediaDto image = new MultimediaDto();
            image.setDataArray(eventFrom.getImage().getBytes());
            url = new UrlConstructorSWImpl(getUrlBase());
            url.addParameter(RestCommonPaths.EVENT_IMAGE_BY_ID);
            getRestTemplate().put(url.getUrl(), image, args);
        }
        return "redirect:/events/" + eventId;
    }

    @RequestMapping(value = "/events/{" + RestCommonPaths.EVENT_ID + "}/image", method = RequestMethod.GET)
    public void getEventImage(@PathVariable(RestCommonPaths.EVENT_ID) String eventId, HttpServletResponse response) throws IOException {
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.EVENT_IMAGE_BY_ID);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.EVENT_ID, eventId);

        MultimediaDto image = getRestTemplate().getForEntity(url.getUrl(), MultimediaDto.class, args).getBody();
        byte[] data = image.getDataArray();

        response.setContentType("image/jpeg");
        response.getOutputStream().write(data);
        response.getOutputStream().flush();
    }

    @RequestMapping(value = "/events/{" + RestCommonPaths.EVENT_ID + "}/thumbnail", method = RequestMethod.GET)
    public void getEventThumbnail(@PathVariable(RestCommonPaths.EVENT_ID) String eventId, HttpServletResponse response) throws IOException {
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.EVENT_THUMBNAIL_BY_ID);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.EVENT_ID, eventId);

        MultimediaDto image = getRestTemplate().getForEntity(url.getUrl(), MultimediaDto.class, args).getBody();
        byte[] data = image.getDataArray();

        response.setContentType("image/jpeg");
        response.getOutputStream().write(data);
        response.getOutputStream().flush();
    }

    @RequestMapping(value = "/events/{" + RestCommonPaths.EVENT_ID + "}/remove", method = RequestMethod.GET)
    public String removeEvent(@PathVariable(RestCommonPaths.EVENT_ID) String eventId) {

        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.EVENT_BY_ID);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.EVENT_ID, eventId);
        getRestTemplate().delete(url.getUrl(), args);

        return "redirect:/events";
    }

    @RequestMapping(value = "/events/{" + RestCommonPaths.EVENT_ID + "}/message", method = RequestMethod.POST)
    public String addMessage(@PathVariable(RestCommonPaths.EVENT_ID) String eventId, MessageDto message) {
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.EVENT_MESSAGES);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.EVENT_ID, eventId);

        message.setOwner(getCurrentUser().toUserDto());
        message.setWrittingDate(new Date());

        getRestTemplate().postForEntity(url.getUrl(), message, MessageDto.class, args).getBody();
        return "redirect:/events/" + eventId;
    }

    @RequestMapping(value = "/events/{" + RestCommonPaths.EVENT_ID + "}/message/{" + RestCommonPaths.MESSAGE_ID + "}", method = RequestMethod.DELETE)
    public @ResponseBody
    String removeMessage(@PathVariable(RestCommonPaths.EVENT_ID) String eventId, @PathVariable(RestCommonPaths.MESSAGE_ID) String messageId) {
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.EVENT_MESSAGES_BY_ID);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.EVENT_ID, eventId);
        args.put(RestCommonPaths.MESSAGE_ID, messageId);

        getRestTemplate().delete(url.getUrl(), args);
        return "redirect:/events/" + eventId;
    }

    private UserListDto getMembers(String eventId) {
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.EVENT_MEMBERS);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.EVENT_ID, eventId);
        UserListDto members = getRestTemplate().getForEntity(url.getUrl(), UserListDto.class, args).getBody();
        return members;
    }

    private BaseListDto<MessageDto> getMessages(String eventId) {
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.EVENT_MESSAGES);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.EVENT_ID, eventId);
        BaseListDto<MessageDto> messages = getRestTemplate().getForEntity(url.getUrl(), BaseListDto.class, args).getBody();
        return messages;
    }

    private EventDto getEvent(String eventId) {
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.EVENT_BY_ID);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.EVENT_ID, eventId);

        EventDto event = getRestTemplate().getForEntity(url.getUrl(), EventDto.class, args).getBody();
        return event;
    }

    private Object getRoute(String eventId) {
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.EVENT_ROUTE);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.EVENT_ID, eventId);
        try {
            String routeJson = getRestTemplate().getForEntity(url.getUrl(), String.class, args).getBody();
            if (routeJson != null) {
                RouteDto route = RouteDto.parseGeoJsonString(routeJson);
                return route;
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.EVENT_SEGMENT);
        String segmentJson = getRestTemplate().getForEntity(url.getUrl(), String.class, args).getBody();
        if (segmentJson != null) {
            SegmentDto segment = SegmentDto.parseGeoJsonString(segmentJson);

            return segment;
        }
        return null;
    }

    private List<Object> getRouteResutls(String eventId) {
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.EVENT_ROUTE_PRACTICES);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.EVENT_ID, eventId);

        BaseListDto<Object> list = getRestTemplate().getForEntity(url.getUrl(), BaseListDto.class, args).getBody();
        return list;
    }

    private List<Object> getSegmentResutls(String eventId) {
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.EVENT_SEGMENTS_PRACTICES);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.EVENT_ID, eventId);

        BaseListDto<Object> list = getRestTemplate().getForEntity(url.getUrl(), BaseListDto.class, args).getBody();
        return list;
    }
}
