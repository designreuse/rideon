/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.web.controller;

import com.rideon.web.util.JSPWebPaths;
import com.rideon.model.dto.list.BaseListDto;
import com.rideon.common.util.RestCommonPaths;
import com.rideon.web.util.UrlConstructorSW;
import com.rideon.web.util.UrlConstructorSWImpl;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Fer
 */
@Controller
public class HomeController extends GenericAbstractController {

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ModelAndView getHome(HttpServletRequest request) {
        ModelAndView model = new ModelAndView(JSPWebPaths.HOME);

        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.ROUTE_LAST);
        BaseListDto routes = getRestTemplate().getForEntity(url.getUrl(), BaseListDto.class).getBody();
        model.addObject("routes", routes);

        url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.SEGMENT_LAST);
        BaseListDto segments = getRestTemplate().getForEntity(url.getUrl(), BaseListDto.class).getBody();
        model.addObject("segments", segments);
        return model;
    }
}
