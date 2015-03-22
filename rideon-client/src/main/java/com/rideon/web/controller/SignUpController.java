/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.web.controller;

import com.rideon.web.util.JSPWebPaths;
import com.rideon.model.dto.MultimediaDto;
import com.rideon.model.dto.SignFormDto;
import com.rideon.model.dto.UserDto;
import com.rideon.common.util.RestCommonPaths;
import com.rideon.web.util.UrlConstructorSW;
import com.rideon.web.util.UrlConstructorSWImpl;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author fer
 */
@Controller
@RequestMapping(value = "/sign")
public class SignUpController extends GenericAbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignUpController.class);
    @Autowired
    private RestTemplate restTemplate;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        SignFormValidator validator = new SignFormValidator();
        binder.setValidator(validator);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView showRegisterPage() {
        ModelAndView model = new ModelAndView(JSPWebPaths.SIGNUP);
        SignFormDto form = new SignFormDto();
        model.addObject("signForm", form);
        return model;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String processForm(@Valid SignFormDto form, BindingResult result, Map model) {

        if (result.hasFieldErrors()) {
            return JSPWebPaths.SIGNUP;
        } else {
            LOGGER.info("NO errors found in form");
        }

        UrlConstructorSW url = new UrlConstructorSWImpl(getConfigurationService().getUrlBase());
        url.addParameter(RestCommonPaths.USER);

        LOGGER.info("URL exchange : {}", url.getUrl());
        try {
            ResponseEntity<UserDto> response = restTemplate.postForEntity(url.getUrl(), form, UserDto.class);
            if (response.getStatusCode() == HttpStatus.CREATED) {
                LOGGER.info("User created: {} ", response.getBody().getEmail());
            }

        } catch (DuplicateKeyException e) {
            LOGGER.error("", e);
            result.rejectValue("email", "UserAlreadyExist");
            return "sign";
        } catch (RestClientException e) {
            LOGGER.error("", e);
            return "redirect:/" + JSPWebPaths.SIGNUP;
        }

        return "redirect:/"+ JSPWebPaths.LOGIN;
    }
}
