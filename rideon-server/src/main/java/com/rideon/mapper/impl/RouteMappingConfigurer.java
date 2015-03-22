/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.mapper.impl;

import com.rideon.model.dto.RouteDto;
import com.rideon.mapper.MappingConfigurer;
import com.rideon.model.domain.Route;
import ma.glasnost.orika.MapperFactory;

/**
 *
 * @author Fer
 */
public class RouteMappingConfigurer implements MappingConfigurer {

    @Override
    public void configure(MapperFactory factory) {
        factory.classMap(Route.class, RouteDto.class)
                .byDefault()
                .register();
    }
}
