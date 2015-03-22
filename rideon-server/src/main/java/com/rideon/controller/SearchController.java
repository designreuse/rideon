/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.controller;

import com.rideon.model.dto.GroupDto;
import com.rideon.model.dto.PracticeDto;
import com.rideon.model.dto.UserDto;
import com.rideon.model.dto.list.BaseListDto;
import com.rideon.model.dto.list.EventListDto;
import com.rideon.model.dto.list.RouteListDto;
import com.rideon.model.dto.list.SegmentListDto;
import com.rideon.model.service.EventService;
import com.rideon.model.service.GroupService;
import com.rideon.model.service.PracticeService;
import com.rideon.model.service.RouteService;
import com.rideon.model.service.SegmentService;
import com.rideon.model.service.UserService;
import com.rideon.util.Constants;
import com.rideon.common.util.RestCommonPaths;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.ParseException;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SearchController {

    @Autowired
    UserService userService;
    @Autowired
    GroupService groupService;
    @Autowired
    PracticeService practiceService;
    @Autowired
    EventService eventService;
    @Autowired
    RouteService routeService;
    @Autowired
    SegmentService segmentService;

    @RequestMapping(value = RestCommonPaths.SEARCH_USER, method = RequestMethod.GET)
    public @ResponseBody
    BaseListDto<UserDto> searchUsers(@RequestParam String query) {
        return userService.search(query);
    }

    @RequestMapping(value = RestCommonPaths.SEARCH_GROUP, method = RequestMethod.GET)
    public @ResponseBody
    BaseListDto<GroupDto> searchGroups(@RequestParam String query) {
        return groupService.search(query);
    }

    @RequestMapping(value = RestCommonPaths.SEARCH_USER_PRACTICE, method = RequestMethod.GET)
    @ResponseBody
    public BaseListDto<PracticeDto> search(@PathVariable(RestCommonPaths.USER_ID) String userId,
            @RequestParam String from, @RequestParam String to) {
        Date fromDate = new Date(Long.valueOf(from));
        Date toDate = new Date(Long.valueOf(to));

        return practiceService.search(fromDate, toDate, userId);
    }

    @RequestMapping(value = RestCommonPaths.SEARCH_ROUTE, method = RequestMethod.GET)
    public @ResponseBody
    String searchRouteB(@RequestParam String bbox) throws ParseException {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), Constants.SRID);
        String[] values = bbox.split(",");
        double x1 = Double.valueOf(values[0]);
        double x2 = Double.valueOf(values[2]);
        double y1 = Double.valueOf(values[1]);
        double y2 = Double.valueOf(values[3]);
        Envelope e = new Envelope(x1, x2, y1, y2);
        Geometry b = geometryFactory.toGeometry(e);

        RouteListDto list = routeService.search(b);
        String listString = list.toString();
        return listString;
    }

    @RequestMapping(value = RestCommonPaths.SEARCH_SEGMENTS, method = RequestMethod.GET)
    public @ResponseBody
    String searchSegment(@RequestParam String bbox) throws ParseException {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), Constants.SRID);
        String[] values = bbox.split(",");
        double x1 = Double.valueOf(values[0]);
        double x2 = Double.valueOf(values[2]);
        double y1 = Double.valueOf(values[1]);
        double y2 = Double.valueOf(values[3]);
        Envelope e = new Envelope(x1, x2, y1, y2);
        Geometry b = geometryFactory.toGeometry(e);
        SegmentListDto list = segmentService.search(b);
        String listString = list.toString();
        return listString;
    }

    @RequestMapping(value = RestCommonPaths.SEARCH_EVENT, method = RequestMethod.GET)
    @ResponseBody
    public EventListDto searchEvent(
            @RequestParam(value = "from", required = false) String from,
            @RequestParam(value = "to", required = false) String to,
            @RequestParam(value = "query", required = false) String query) {
        if (from != null && to != null) {
            return eventService.search(from, to);
        }
        if (query != null) {
            return eventService.search(query);
        }
        return new EventListDto();
    }
}
