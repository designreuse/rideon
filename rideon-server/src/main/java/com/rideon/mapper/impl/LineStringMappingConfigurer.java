/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.mapper.impl;

import com.rideon.model.dto.LineStringDto;
import com.rideon.mapper.MappingConfigurer;
import com.rideon.mapper.custom.LineStringCustomMapper;
import com.rideon.mapper.factory.LineStringFactory;
import com.vividsolutions.jts.geom.LineString;
import ma.glasnost.orika.MapperFactory;

/**
 *
 * @author Fer
 */
public class LineStringMappingConfigurer implements MappingConfigurer {

    @Override
    public void configure(MapperFactory factory) {
        factory.classMap(LineString.class, LineStringDto.class).customize(new LineStringCustomMapper()).register();
        factory.registerObjectFactory(new LineStringFactory(), LineString.class);
    }
}

