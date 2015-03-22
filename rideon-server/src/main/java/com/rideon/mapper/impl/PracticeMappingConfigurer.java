/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.mapper.impl;

import com.rideon.model.dto.PracticeDto;
import com.rideon.mapper.MappingConfigurer;
import com.rideon.model.domain.Practice;
import ma.glasnost.orika.MapperFactory;

/**
 *
 * @author Fer
 */
public class PracticeMappingConfigurer implements MappingConfigurer {

    @Override
    public void configure(MapperFactory factory) {
        factory.classMap(Practice.class, PracticeDto.class)
                .byDefault()
                .register();
    }
}
