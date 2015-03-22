/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.mapper;

import ma.glasnost.orika.MapperFactory;

/**
 *
 * @author Fer
 */
public interface MappingConfigurer {
    
    void configure(MapperFactory factory);
    
}
