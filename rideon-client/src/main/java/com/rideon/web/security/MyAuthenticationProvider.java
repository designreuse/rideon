/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.web.security;

import com.rideon.model.dto.UserDto;
import com.rideon.common.util.RestCommonPaths;
import com.rideon.web.vo.SessionUser;
import com.rideon.web.controller.GenericAbstractController;
import com.rideon.web.util.UrlConstructorSW;
import com.rideon.web.util.UrlConstructorSWImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.client.HttpClientErrorException;

/**
 *
 * @author Fer
 */
public class MyAuthenticationProvider extends GenericAbstractController implements AuthenticationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyAuthenticationProvider.class);

    @Override
    public Authentication authenticate(Authentication a) throws AuthenticationException {
        try {
            UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
            url.addParameter(RestCommonPaths.USER_BY_ID);
//            url.addParameter(RestCommonPaths.AUTHENTICATION);
            Map<String, String> args = new HashMap<>();
            args.put(RestCommonPaths.USER_ID, a.getName());

            HttpEntity entity = new HttpEntity(creatHeaders(a.getName(), a.getCredentials().toString()));
            ResponseEntity<UserDto> response = getRestTemplate().exchange(url.getUrl(), HttpMethod.GET, entity, UserDto.class, args);

            if (response.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new BadCredentialsException("Bad credentials");
            } else {

                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

                UserDetails userDetails = new SessionUser(response.getBody(),a.getName(), a.getCredentials().toString(), authorities);
                return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword());
            }
        } catch (HttpClientErrorException e) {
            LOGGER.error("", e);
            LOGGER.error("", e.getResponseBodyAsString());
            throw new BadCredentialsException("Bad credentials");
        } catch (Exception e) {
            LOGGER.error("", e);
            throw new BadCredentialsException("Bad credentials");
        }
    }

    @Override
    public boolean supports(Class<?> type) {
        return true;
    }
}
