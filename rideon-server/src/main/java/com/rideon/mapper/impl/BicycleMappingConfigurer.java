/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.mapper.impl;

import com.rideon.model.dto.BicycleDto;
import com.rideon.mapper.MappingConfigurer;
import com.rideon.model.domain.Bicycle;
import ma.glasnost.orika.MapperFactory;

/**
 *
 * @author Fer
 */
public class BicycleMappingConfigurer implements MappingConfigurer {

    @Override
    public void configure(MapperFactory factory) {
        factory.classMap(Bicycle.class, BicycleDto.class)
                .byDefault()
                .register();
    }
}
