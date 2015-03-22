/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.mapper;

import java.util.Set;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 *
 * @author Fer
 */
public class MapperFacadeFactoryBean implements FactoryBean<MapperFacade>, InitializingBean {

    private Set<MappingConfigurer> configurers;
    private MapperFacade mapper;

    @Override
    public MapperFacade getObject() throws Exception {

        return mapper;
    }

    @Override
    public Class<?> getObjectType() {
        return MapperFacade.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

        for (MappingConfigurer configurer : configurers) {
            configurer.configure(mapperFactory);
        }
        this.mapper = mapperFactory.getMapperFacade();
    }

    public Set<MappingConfigurer> getConfigurers() {
        return configurers;
    }

    public void setConfigurers(Set<MappingConfigurer> configurers) {
        this.configurers = configurers;
    }
    
    
}
