/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.mapper.impl;

import com.rideon.model.dto.SegmentPracticeDto;
import com.rideon.mapper.MappingConfigurer;
import com.rideon.model.domain.SegmentPractice;
import ma.glasnost.orika.MapperFactory;

/**
 *
 * @author Fer
 */
public class SegmentPracticeMappingConfigurer implements MappingConfigurer {

    @Override
    public void configure(MapperFactory factory) {
        factory.classMap(SegmentPractice.class, SegmentPracticeDto.class)
                .byDefault()
                .register();
    }
}
