/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.web.controller;

import com.rideon.web.util.JSPWebPaths;
import com.rideon.model.dto.SegmentDto;
import com.rideon.model.dto.list.BaseListDto;
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
public class SegmentController extends GenericAbstractController {

    @RequestMapping(value = "/segments", method = RequestMethod.GET)
    public ModelAndView getSegmentsPage() {
        ModelAndView model = new ModelAndView(JSPWebPaths.SEGMENTS);
        return model;
    }

    @RequestMapping(value = "/segments/{" + RestCommonPaths.SEGMENT_ID + "}", method = RequestMethod.GET)
    public ModelAndView getSegmentPage(@PathVariable(RestCommonPaths.SEGMENT_ID) String segmentId) {
        ModelAndView model = new ModelAndView(JSPWebPaths.SEGMENT);
        model.addObject("sementId", segmentId);

        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.SEGMENT_RESULTS);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.SEGMENT_ID, segmentId);

        BaseListDto results = getRestTemplate().getForEntity(url.getUrl(), BaseListDto.class, args).getBody();

        model.addObject("results", results);

        return model;
    }

    @RequestMapping(value = "/segments/{" + RestCommonPaths.SEGMENT_ID + "}.json", method = RequestMethod.GET)
    public @ResponseBody
    SegmentDto getJson(@PathVariable(RestCommonPaths.SEGMENT_ID) String segmentId) {
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.SEGMENT_BY_ID);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.SEGMENT_ID, segmentId);

        String s = getRestTemplate().getForEntity(url.getUrl(), String.class, args).getBody();
        SegmentDto segment = SegmentDto.parseGeoJsonString(s);
        return segment;
    }

    @RequestMapping(value = "/segments/{" + RestCommonPaths.SEGMENT_ID + "}.gpx", method = RequestMethod.GET)
    public void getGpx(@PathVariable(RestCommonPaths.SEGMENT_ID) String segmentId, HttpServletResponse response) {
        try {
            UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
            url.addParameter(RestCommonPaths.SEGMENT_GPX);
            Map<String, String> args = new HashMap<>();
            args.put(RestCommonPaths.SEGMENT_ID, segmentId);

            String gpx = getRestTemplate().getForEntity(url.getUrl(), String.class, args).getBody();

            response.setContentType("application/xlm");
            response.setHeader("Content-Disposition", "attachment; filename=\""
                    + segmentId + ".gpx\"");

            InputStream is = new ByteArrayInputStream(gpx.getBytes());
            IOUtils.copy(is, response.getOutputStream());

            response.flushBuffer();
        } catch (IOException ex) {
            Logger.getLogger(RoutesController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
