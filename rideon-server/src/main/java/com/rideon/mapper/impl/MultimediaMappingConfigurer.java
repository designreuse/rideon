/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.mapper.impl;

import com.rideon.model.dto.MultimediaDto;
import com.rideon.mapper.MappingConfigurer;
import com.rideon.model.domain.Multimedia;
import ma.glasnost.orika.MapperFactory;

/**
 *
 * @author Fer
 */
public class MultimediaMappingConfigurer implements MappingConfigurer {

    @Override
    public void configure(MapperFactory factory) {
        factory.classMap(Multimedia.class, MultimediaDto.class)
                .byDefault()
                .register();
    }
}
