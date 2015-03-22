/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.controller;

import com.rideon.model.dto.UserDto;
import com.rideon.model.service.UserService;
import com.rideon.common.util.RestCommonPaths;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Fer
 */
@Controller
public class AuthenticationController {

    @Autowired
    UserService userService;

    @RequestMapping(value = RestCommonPaths.AUTHENTICATION, method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDto> authenticate(HttpServletRequest request) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
