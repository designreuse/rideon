/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.web.controller;

import com.rideon.web.util.JSPWebPaths;
import com.rideon.model.dto.LineStringDto;
import com.rideon.model.dto.PracticeDto;
import com.rideon.model.dto.RouteDto;
import com.rideon.common.enums.Privacy;
import com.rideon.common.util.RestCommonPaths;
import com.rideon.web.vo.GPXForm;
import com.rideon.web.util.UrlConstructorSW;
import com.rideon.web.util.UrlConstructorSWImpl;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
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
public class PracticesController extends GenericAbstractController {

    @RequestMapping(value = "/practices", method = RequestMethod.GET)
    public ModelAndView getPracticesPage() {
        ModelAndView model = new ModelAndView(JSPWebPaths.PRACTICES);
        GPXForm form = new GPXForm();
        model.addObject("gpxForm", form);
        model.addObject("userId", getCurrentUser().getUsername());
        return model;
    }

    @RequestMapping(value = "/{" + RestCommonPaths.USER_ID + "}/practices", method = RequestMethod.GET)
    public ModelAndView getPracticesUserPage(@PathVariable(RestCommonPaths.USER_ID) String userId) {
        ModelAndView model;
        if (userId.equals(getCurrentUser().getUsername())) {
            model = new ModelAndView(JSPWebPaths.PRACTICES);
        } else {
            model = new ModelAndView(JSPWebPaths.PRACTICES_NOPRIV);
        }
        GPXForm form = new GPXForm();
        model.addObject("gpxForm", form);
        model.addObject("userId", userId);
        return model;
    }

    @RequestMapping(value = "/practices", method = RequestMethod.POST)
    public String addPractice(GPXForm form) throws IOException {
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.USER_PRACTICES);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.USER_ID, getCurrentUser().getUsername());

        String gpx = new String(form.getFile().getBytes());
        LineStringDto dto = new LineStringDto();
        dto.setGpx(gpx);
        PracticeDto dtp = getRestTemplate().postForEntity(url.getUrl(), dto, PracticeDto.class, args).getBody();

        return "redirect:/practices/" + dtp.getId();
    }

    @RequestMapping(value = "/practices/{" + RestCommonPaths.PRACTICE_ID + "}/routes", method = RequestMethod.GET)
    public @ResponseBody
    RouteDto getPracticeRoute(@PathVariable(RestCommonPaths.PRACTICE_ID) String practiceId) {
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.PRACTICE_ROUTE_BY_ID);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.USER_ID, getCurrentUser().getUsername());
        args.put(RestCommonPaths.PRACTICE_ID, practiceId);

        String s = getRestTemplate().getForEntity(url.getUrl(), String.class, args).getBody();
        RouteDto route = RouteDto.parseGeoJsonString(s);
        return route;
    }

    @RequestMapping(value = "/practices/{" + RestCommonPaths.PRACTICE_ID + "}", method = {RequestMethod.POST, RequestMethod.PUT})
    public String updatePractice(@PathVariable(RestCommonPaths.PRACTICE_ID) String practiceId, @RequestBody PracticeDto practice) {
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.PRACTICE_BY_ID);

        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.USER_ID, getCurrentUser().getUsername());
        args.put(RestCommonPaths.PRACTICE_ID, practiceId);

        practice.setId(Long.valueOf(practiceId));

        getRestTemplate().put(url.getUrl(), practice, args);

        return "redirect:/practices/" + practiceId;
    }

    @RequestMapping(value = "/practices/{" + RestCommonPaths.PRACTICE_ID + "}", method = RequestMethod.GET)
    public ModelAndView getPractice(@PathVariable(RestCommonPaths.PRACTICE_ID) String practiceId) {
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.PRACTICE_BY_ID);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.USER_ID, getCurrentUser().getUsername());
        args.put(RestCommonPaths.PRACTICE_ID, practiceId);

        PracticeDto pr = getRestTemplate().getForEntity(url.getUrl(), PracticeDto.class, args).getBody();
        ModelAndView model;
        if (pr.getOwner().getUsername().equals(getCurrentUser().getUsername()) || getCurrentUser().getUsername().equals("admin")) {
            model = new ModelAndView(JSPWebPaths.PRACTICE);
        } else {
            model = new ModelAndView(JSPWebPaths.PRACTICE_NOPRIV);
        }
        model.addObject("practice", pr);
        model.addObject("gpxForm", new GPXForm());
        model.addObject("userId", getCurrentUser().getUsername());
        return model;
    }

    @RequestMapping(value = "/{" + RestCommonPaths.USER_ID + "}/practices/{" + RestCommonPaths.PRACTICE_ID + "}", method = RequestMethod.GET)
    public ModelAndView getFriendPractice(@PathVariable(RestCommonPaths.PRACTICE_ID) String practiceId,
            @PathVariable(RestCommonPaths.USER_ID) String userId) {
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.PRACTICE_BY_ID);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.USER_ID, getCurrentUser().getUsername());
        args.put(RestCommonPaths.PRACTICE_ID, practiceId);

        PracticeDto pr = getRestTemplate().getForEntity(url.getUrl(), PracticeDto.class, args).getBody();
        ModelAndView model;
        if (pr.getOwner().getUsername().equals(getCurrentUser().getUsername()) || getCurrentUser().getUsername().equals("admin")) {
            model = new ModelAndView(JSPWebPaths.PRACTICE);
        } else {
            model = new ModelAndView(JSPWebPaths.PRACTICE_NOPRIV);
        }
        model.addObject("practice", pr);
        model.addObject("gpxForm", new GPXForm());
        model.addObject("userId", userId);
        return model;
    }

    @RequestMapping(value = "/practices/{" + RestCommonPaths.PRACTICE_ID + "}.gpx", method = RequestMethod.GET)
    public void getPracticeGPX(@PathVariable(RestCommonPaths.PRACTICE_ID) String practiceId, HttpServletResponse response) throws IOException {
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.PRACTICE_GPX);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.USER_ID, getCurrentUser().getUsername());
        args.put(RestCommonPaths.PRACTICE_ID, practiceId);

        String r = getRestTemplate().getForEntity(url.getUrl(), String.class, args).getBody();
        byte[] data = r.getBytes();
        response.setContentType("application/xlm");
        response.setHeader("Content-Disposition", "attachment; filename=\""
                + practiceId + ".gpx\"");
        response.getOutputStream().write(data);
        response.getOutputStream().flush();
    }

    @RequestMapping(value = "/practices/{" + RestCommonPaths.PRACTICE_ID + "}", method = RequestMethod.DELETE)
    public @ResponseBody
    String removePractice(@PathVariable(RestCommonPaths.PRACTICE_ID) String practiceId) {

        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.PRACTICE_BY_ID);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.PRACTICE_ID, practiceId);
        args.put(RestCommonPaths.USER_ID, getCurrentUser().getUsername());

        getRestTemplate().delete(url.getUrl(), args);

        return "redirect:/practices/";
    }
}
