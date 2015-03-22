/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.mapper.impl;

import com.rideon.model.dto.SignFormDto;
import com.rideon.model.dto.UserDto;
import com.rideon.mapper.MappingConfigurer;
import com.rideon.model.domain.User;
import ma.glasnost.orika.MapperFactory;

/**
 *
 * @author Fer
 */
public class UserMappingConfigurer implements MappingConfigurer {

    @Override
    public void configure(MapperFactory factory) {
        factory.classMap(User.class, UserDto.class)
                .byDefault()
                .register();

        factory.classMap(User.class, SignFormDto.class)
                .byDefault()
                .register();
    }
}
