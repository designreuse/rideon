/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.mapper.impl;

import com.rideon.model.dto.GroupDto;
import com.rideon.mapper.MappingConfigurer;
import com.rideon.model.domain.Group;
import ma.glasnost.orika.MapperFactory;

/**
 *
 * @author Fer
 */
public class GroupMappingConfigurer implements MappingConfigurer {

    @Override
    public void configure(MapperFactory factory) {
        factory.classMap(Group.class, GroupDto.class)
                .byDefault()
                .register();
    }
}
