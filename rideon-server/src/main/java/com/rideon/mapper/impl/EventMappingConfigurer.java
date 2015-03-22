/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.mapper.impl;

import com.rideon.model.dto.EventDto;
import com.rideon.mapper.MappingConfigurer;
import com.rideon.model.domain.Event;
import ma.glasnost.orika.MapperFactory;

/**
 *
 * @author Fer
 */
public class EventMappingConfigurer implements MappingConfigurer {

    @Override
    public void configure(MapperFactory factory) {
        factory.classMap(EventDto.class, Event.class)
                .byDefault()
                .register();
    }
}
