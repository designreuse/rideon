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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.client.RestClientException;

/**
 *
 * @author Fer
 */
public class MyUserDetailService extends GenericAbstractController implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyUserDetailService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        try {
            UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
            url.addParameter(RestCommonPaths.USER_BY_ID);
            Map<String, String> args = new HashMap<>();
            args.put(RestCommonPaths.USER_ID, username);

            ResponseEntity<UserDto> response = getRestTemplate().getForEntity(url.getUrl(), UserDto.class, args);

            if (response.getStatusCode().equals(HttpStatus.OK)) {
                UserDto user = response.getBody();
                List< GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
//                return new org.springframework.security.core.userdetails.User(user.getUsername(), "123", authorities);
                    return new SessionUser(user, user.getUsername(), null, authorities);
            } else {
                LOGGER.error("User not found, response status : {}", response.getStatusCode());
                throw new UsernameNotFoundException("User not found");
            }

        } catch (IllegalStateException | RestClientException | UsernameNotFoundException e) {
            LOGGER.error("", e);
            throw new UsernameNotFoundException("User not found");
        }
    }
}
