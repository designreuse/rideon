/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.service.impl;

import com.rideon.model.service.ConfigurationService;
import org.springframework.stereotype.Service;

/**
 *
 * @author Fer
 */
@Service(value = "configurationService")
public class ConfigurationServiceImpl implements ConfigurationService {

    private String urlBase;

    @Override
    public void setUrlBase(String urlBase) {
        this.urlBase = urlBase;
    }

    @Override
    public String getUrlBase() {
        return this.urlBase;
    }
}
