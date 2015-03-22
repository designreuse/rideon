/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.mapper.impl;

import com.rideon.model.dto.list.BaseListDto;
import com.rideon.mapper.MappingConfigurer;
import java.util.List;
import ma.glasnost.orika.MapperFactory;

/**
 *
 * @author Fer
 */
public class BaseListMappingConfigurer implements MappingConfigurer {

    @Override
    public void configure(MapperFactory factory) {
        factory.classMap(List.class, BaseListDto.class)
                .byDefault()
                .register();
    }
}
