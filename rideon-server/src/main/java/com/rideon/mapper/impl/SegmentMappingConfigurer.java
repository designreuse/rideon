/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.mapper.impl;

import com.rideon.model.dto.SegmentDto;
import com.rideon.mapper.MappingConfigurer;
import com.rideon.model.domain.Segment;
import ma.glasnost.orika.MapperFactory;

/**
 *
 * @author Fer
 */
public class SegmentMappingConfigurer implements MappingConfigurer {

    @Override
    public void configure(MapperFactory factory) {
        factory.classMap(Segment.class, SegmentDto.class)
                .byDefault()
                .register();
    }
}
