/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.filter;

import com.rideon.model.dao.UserDao;
import com.rideon.model.domain.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author Fer
 */
@Service("userDetailsService")
public class MyUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyUserDetailsService.class);
    @Autowired
    private UserDao userDao;
    private static final List<GrantedAuthority> authorities = new ArrayList<>(
        Arrays.asList(new GrantedAuthority[]{new SimpleGrantedAuthority("ROLE_USER")})
    );

    
    @Override
    public UserDetails loadUserByUsername(String string) throws UsernameNotFoundException {
        try {
            User user = userDao.getById(string);

            if (user != null) {
//                this.authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
                return userDetails;
            } else {
                LOGGER.info("User not found");
                throw new UsernameNotFoundException("User not found");
            }
        } catch (IllegalArgumentException e) {
            LOGGER.error("", e);
            throw new UsernameNotFoundException("User not found");
        }
    }
}
