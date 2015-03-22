/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.mapper.impl;

import com.rideon.model.dto.FriendshipRequestDto;
import com.rideon.mapper.MappingConfigurer;
import com.rideon.model.domain.FriendshipRequest;
import ma.glasnost.orika.MapperFactory;

/**
 *
 * @author Fer
 */
public class FriendshipRequestMappingConfigurer implements MappingConfigurer {

    @Override
    public void configure(MapperFactory factory) {
        factory.classMap(FriendshipRequest.class, FriendshipRequestDto.class)
                .byDefault()
                .register();
    }
}
