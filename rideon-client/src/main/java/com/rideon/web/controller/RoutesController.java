/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.web.controller;

import com.rideon.web.util.JSPWebPaths;
import com.rideon.model.dto.LineStringDto;
import com.rideon.model.dto.RouteDto;
import com.rideon.model.dto.list.SegmentListDto;
import com.rideon.common.util.RestCommonPaths;
import com.rideon.web.util.UrlConstructorSW;
import com.rideon.web.util.UrlConstructorSWImpl;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Fer
 */
@Controller
public class RoutesController extends GenericAbstractController {

    @RequestMapping(value = "/routes", method = RequestMethod.GET)
    public ModelAndView getRoutesPage() {
        ModelAndView model = new ModelAndView(JSPWebPaths.ROUTES);
        return model;
    }

    @RequestMapping(value = "/routes/{" + RestCommonPaths.ROUTE_ID + "}", method = RequestMethod.GET)
    public ModelAndView getRoutePage(@PathVariable(RestCommonPaths.ROUTE_ID) String routeId) {

        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.ROUTE_SEGMENTS);

        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.ROUTE_ID, routeId);
        
        String json = getRestTemplate().getForEntity(url.getUrl(), String.class, args).getBody();
        SegmentListDto list = SegmentListDto.parseGeoJsonString(json);
        ModelAndView model = new ModelAndView(JSPWebPaths.ROUTE);
        model.addObject("routeId", routeId);
        model.addObject("segments", list);
        return model;
    }

    @RequestMapping(value = "/routes/{" + RestCommonPaths.ROUTE_ID + "}.json", method = RequestMethod.GET)
    public @ResponseBody
    RouteDto getRouteJson(@PathVariable(RestCommonPaths.ROUTE_ID) String routeId) {
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.ROUTE_BY_ID);

        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.ROUTE_ID, routeId);
        String json = getRestTemplate().getForEntity(url.getUrl(), String.class, args).getBody();
        RouteDto routeDto = RouteDto.parseGeoJsonString(json);
        return routeDto;
    }

//    @RequestMapping(value = "/routes/{" + RestCommonPaths.ROUTE_ID + "}.gpx", method = RequestMethod.GET)
//    @ResponseBody
//    public String getGpx(@PathVariable(RestCommonPaths.ROUTE_ID) String routeId) {
//        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
//        url.addParameter(RestCommonPaths.ROUTE_GPX);
//
//        Map<String, String> args = new HashMap<>();
//        args.put(RestCommonPaths.ROUTE_ID, routeId);
//        String gpx = getRestTemplate().getForEntity(url.getUrl(), String.class, args).getBody();
//        return gpx;
//
//    }

    @RequestMapping(value = "/routes/{" + RestCommonPaths.ROUTE_ID + "}.gpx", method = RequestMethod.GET)
    public void getDownloadGpx(@PathVariable(RestCommonPaths.ROUTE_ID) String routeId, HttpServletResponse response) {
        try {
            UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
            url.addParameter(RestCommonPaths.ROUTE_GPX);

            Map<String, String> args = new HashMap<>();
            args.put(RestCommonPaths.ROUTE_ID, routeId);

            String gpx = getRestTemplate().getForEntity(url.getUrl(), String.class, args).getBody();

            response.setContentType("application/xlm");
            response.setHeader("Content-Disposition", "attachment; filename=\""
                    + routeId + ".gpx\"");

            InputStream is = new ByteArrayInputStream(gpx.getBytes());
            IOUtils.copy(is, response.getOutputStream());

            response.flushBuffer();
            
        } catch (IOException ex) {
            Logger.getLogger(RoutesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
