/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.web.controller;

import com.rideon.model.dto.EventDto;
import com.rideon.model.dto.GroupDto;
import com.rideon.model.dto.PracticeDto;
import com.rideon.model.dto.UserDto;
import com.rideon.model.dto.list.BaseListDto;
import com.rideon.model.dto.list.EventListDto;
import com.rideon.model.dto.list.PracticeListDto;
import com.rideon.model.dto.list.RouteListDto;
import com.rideon.model.dto.list.SegmentListDto;
import com.rideon.common.util.RestCommonPaths;
import com.rideon.web.vo.CalendarEvent;
import com.rideon.web.vo.CalendarPracticeEvent;
import com.rideon.web.util.UrlConstructorSW;
import com.rideon.web.util.UrlConstructorSWImpl;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Fer
 */
@Controller
public class SearchController extends GenericAbstractController {

    @RequestMapping(value = "/search/user", method = RequestMethod.GET)
    public @ResponseBody
    List<UserDto> searchUsers(@RequestParam String query) {

        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.SEARCH_USER_QUERY);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.QUERY, query);

        BaseListDto<UserDto> list = getRestTemplate().getForEntity(url.getUrl(), BaseListDto.class, args).getBody();
        List<UserDto> userList = list.getData();

        return userList;
    }

    @RequestMapping(value = "/search/groups", method = RequestMethod.GET)
    public @ResponseBody
    List<GroupDto> searchGroups(@RequestParam String query) {

        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.SEARCH_GROUP_QUERY);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.QUERY, query);

        BaseListDto<GroupDto> list = getRestTemplate().getForEntity(url.getUrl(), BaseListDto.class, args).getBody();
        List<GroupDto> userList = list.getData();

        return userList;
    }

    @RequestMapping(value = "/search/practices{" + RestCommonPaths.USER_ID + "}", method = RequestMethod.GET)
    public @ResponseBody
    List<CalendarPracticeEvent> searchPractices(@RequestParam String start,
            @RequestParam String end,
            @PathVariable(RestCommonPaths.USER_ID) String userId) throws ParseException {
        Long fromDate = Long.valueOf(start) * 1000;
        Long toDate = Long.valueOf(end) * 1000;

        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.SEARCH_USER_PRACTICES_BY_DATE_RANGE);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.FROM_DATE, fromDate.toString());
        args.put(RestCommonPaths.TO_DATE, toDate.toString());
        args.put(RestCommonPaths.USER_ID, userId);

        PracticeListDto list = getRestTemplate().getForEntity(url.getUrl(), PracticeListDto.class, args).getBody();
        List<PracticeDto> practices = list.getData();

        List<CalendarPracticeEvent> calendar = new ArrayList<>();
        for (PracticeDto pr : practices) {
            calendar.add(new CalendarPracticeEvent(pr));
        }

        return calendar;
    }

    @RequestMapping(value = "/search/events", method = RequestMethod.GET)
    public @ResponseBody
    List<CalendarEvent> searchEvents(
            @RequestParam(value = "start", required = false) String start,
            @RequestParam(value = "end", required = false) String end,
            @RequestParam(value = "query", required = false) String query) throws ParseException {


        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        Map<String, String> args = new HashMap<>();
//        args.put(RestCommonPaths.USER_ID, getCurrentUser().getUsername());

        if (start != null && end != null) {
            url.addParameter(RestCommonPaths.SEARCH_EVENTS_BY_DATE_RANGE);
            Long fromDate = Long.valueOf(start) * 1000;
            args.put(RestCommonPaths.FROM_DATE, fromDate.toString());
            Long toDate = Long.valueOf(end) * 1000;
            args.put(RestCommonPaths.TO_DATE, toDate.toString());
        }
        if (query != null) {
            url.addParameter(RestCommonPaths.SEARCH_EVENT_BY_NAME);
            args.put(RestCommonPaths.QUERY, query);
        }

        EventListDto list = getRestTemplate().getForEntity(url.getUrl(), EventListDto.class, args).getBody();
        List<EventDto> events = list.getData();

        List<CalendarEvent> calendar = new ArrayList<>();
        for (EventDto ev : events) {
            CalendarEvent ce = new CalendarEvent(ev);
            calendar.add(ce);
        }

        return calendar;
    }

    @RequestMapping(value = "/search/routes", method = RequestMethod.GET)
    public @ResponseBody
    RouteListDto searchRoutes(@RequestParam(value = "bbox") String bbox) throws ParseException {
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.SEARCH_ROUTE_BY_GEOM);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.BBOX, bbox);

        String s = getRestTemplate().getForEntity(url.getUrl(), String.class, args).getBody();

        RouteListDto list = RouteListDto.parseGeoJsonString(s);
        return list;
    }

    @RequestMapping(value = "/search/segments", method = RequestMethod.GET)
    public @ResponseBody
    SegmentListDto searchSegments(@RequestParam(value = "bbox") String bbox) throws ParseException {
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.SEARCH_SEGMENTS_BY_GEOM);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.BBOX, bbox);

        String s = getRestTemplate().getForEntity(url.getUrl(), String.class, args).getBody();

        SegmentListDto list = SegmentListDto.parseGeoJsonString(s);
        return list;
    }
}
