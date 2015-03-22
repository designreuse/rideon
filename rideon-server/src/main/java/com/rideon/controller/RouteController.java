/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.controller;

import com.rideon.model.dto.RouteDto;
import com.rideon.model.dto.list.BaseListDto;
import com.rideon.model.dto.list.RouteListDto;
import com.rideon.model.dto.list.SegmentListDto;
import com.rideon.model.service.RouteService;
import com.rideon.common.util.RestCommonPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Fer
 */
@Controller
public class RouteController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteController.class);
    @Autowired
    RouteService routeService;

    @RequestMapping(value = RestCommonPaths.ROUTE_BY_ID, method = RequestMethod.GET)
    @ResponseBody
    public RouteDto get(@PathVariable(RestCommonPaths.ROUTE_ID) String routeId) {
        return routeService.get(routeId);
    }

    @RequestMapping(value = RestCommonPaths.ROUTE_GPX, method = RequestMethod.GET)
    @ResponseBody
    public String getGpx(@PathVariable(RestCommonPaths.ROUTE_ID) String routeId) {
        return routeService.getGpx(routeId);
    }

    @RequestMapping(value = RestCommonPaths.ROUTE_SEGMENTS, method = RequestMethod.GET)
    @ResponseBody
    public String getSegments(@PathVariable(RestCommonPaths.ROUTE_ID) String routeId) {
        SegmentListDto list = routeService.getSegments(routeId);
        return list.toString();
    }

    @RequestMapping(value = RestCommonPaths.ROUTE_LAST, method = RequestMethod.GET)
    @ResponseBody
    public BaseListDto<String> getLast() {
        BaseListDto<String> list = routeService.getLast("5");
        return list;
    }
}
