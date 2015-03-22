/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.mapper.impl;

import com.rideon.mapper.MappingConfigurer;
import com.rideon.mapper.custom.GeoJsonObjectCustomConverter;
import com.rideon.mapper.custom.LinestringJsonObjectCustomConverter;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;

/**
 *
 * @author Fer
 */
public class GeometryMappingConfigurer implements MappingConfigurer {

    @Override
    public void configure(MapperFactory factory) {
//        factory.classMap(LineString.class, GeoJsonObject.class).customize(new GeometryCustomConverter()).register();
//        factory.registerObjectFactory(new LineStringFactory(), LineString.class);
//        factory.registerObjectFactory(new GeoJsonObjectFactory(), GeoJsonObject.class);
//        
        ConverterFactory converterFactory = factory.getConverterFactory();
        converterFactory.registerConverter(new GeoJsonObjectCustomConverter());
        converterFactory.registerConverter(new LinestringJsonObjectCustomConverter());
    }
}
