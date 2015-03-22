/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.dto.list;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rideon.model.dto.RouteDto;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Fer
 */
public class RouteListDto implements Iterable<RouteDto> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteListDto.class);
    private String type = "FeatureCollection";
    private List<RouteDto> features = new ArrayList<>();

    public String getType() {
        return "FeatureCollection";
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<RouteDto> getFeatures() {
        return features;
    }

    public void setFeatures(List<RouteDto> features) {
        this.features = features;
    }

    public RouteListDto add(RouteDto feature) {
        features.add(feature);
        return this;
    }

    public void addAll(Collection<RouteDto> features) {
        this.features.addAll(features);
    }

    @Override
    public Iterator<RouteDto> iterator() {
        return features.iterator();
    }

    public static RouteListDto parseGeoJsonString(String json) {
        try {
            return new ObjectMapper().readValue(json, RouteListDto.class);
        } catch (IOException ex) {
            LOGGER.error("", ex);
            return null;
        }
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            LOGGER.error("", ex);
            return null;
        }
    }
}
