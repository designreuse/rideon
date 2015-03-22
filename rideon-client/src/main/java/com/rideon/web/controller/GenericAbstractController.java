/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.web.controller;

import com.rideon.web.vo.SessionUser;
import com.rideon.web.security.ClientAuthenticator;
import com.rideon.web.service.ConfigurationService;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Fer
 */
public class GenericAbstractController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ConfigurationService configurationService;
    private List<MediaType> acceptableMediaTypes;
    private HttpHeaders headers;

    public RestTemplate getRestTemplate() {
        SecurityContext sc = SecurityContextHolder.getContext();
        Authentication auth = sc.getAuthentication();
        if (auth != null) {
            if (auth instanceof RememberMeAuthenticationToken) {
                SessionUser principal = (SessionUser) auth.getPrincipal();
                ClientAuthenticator.setAuthentication(restTemplate, principal.getUsername(), principal.getPassword());
            } else {
                ClientAuthenticator.setAuthentication(restTemplate, auth.getName(), auth.getCredentials().toString());
            }
        }

        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ConfigurationService getConfigurationService() {
        return configurationService;
    }

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    public List<MediaType> getAcceptableMediaTypes() {
        if (this.acceptableMediaTypes == null) {
            acceptableMediaTypes = new ArrayList<>();
            acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
        }
        return acceptableMediaTypes;
    }

    public HttpHeaders getHeaders() {
        if (this.headers == null) {
            headers = new HttpHeaders();
            headers.setAccept(this.getAcceptableMediaTypes());
        }
        return headers;
    }

    public HttpHeaders getAuthenticationHeaders() throws IllegalStateException {
        SecurityContext sc = SecurityContextHolder.getContext();
        Authentication a = sc.getAuthentication();
        if (a != null) {
            return creatHeaders(a.getName(), a.getCredentials().toString());
        } else {
            throw new IllegalStateException("No authentication present");
        }
    }

    public String getUrlBase() {
        return configurationService.getUrlBase();
    }

    public HttpHeaders creatHeaders(String user, String password) {
        HttpHeaders hdrs = getHeaders();
        String str = "Basic " + encodeAuthorizationKey(user, password);
        hdrs.set("Authorization", str);
        return hdrs;
    }

    public String encodeAuthorizationKey(final String username, final String password) {
        final String authorizationString = username + ":" + password;
        return new String(Base64.encodeBase64(authorizationString.getBytes(Charset.forName("US-ASCII"))));
    }

    public SessionUser getCurrentUser() {
        return (SessionUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
