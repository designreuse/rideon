/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.mapper.impl;

import com.rideon.model.dto.MessageDto;
import com.rideon.mapper.MappingConfigurer;
import com.rideon.model.domain.Message;
import ma.glasnost.orika.MapperFactory;

/**
 *
 * @author Fer
 */
public class MessageMappingConfigurer implements MappingConfigurer {

    @Override
    public void configure(MapperFactory factory) {
        factory.classMap(Message.class, MessageDto.class)
                .byDefault()
                .register();
    }
}
