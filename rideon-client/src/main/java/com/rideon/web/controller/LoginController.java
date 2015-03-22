/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.web.controller;

import com.rideon.web.util.JSPWebPaths;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author fer
 */
@Controller
public class LoginController extends GenericAbstractController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String showLoginPage(HttpServletRequest request) {
        return JSPWebPaths.LOGIN;
    }

    @RequestMapping(value = "/loginfailed", method = RequestMethod.GET)
    public ModelAndView showErrorLoginPage(HttpServletRequest request) {
        ModelAndView model = new ModelAndView(JSPWebPaths.LOGIN);
        model.addObject("loginFailed", true);
        return model;
    }
}
