/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.dto.list;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rideon.model.dto.SegmentDto;
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
public class SegmentListDto implements Iterable<SegmentDto> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteListDto.class);
    private String type = "FeatureCollection";
    private List<SegmentDto> features = new ArrayList<>();

    public String getType() {
        return "FeatureCollection";
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<SegmentDto> getFeatures() {
        return features;
    }

    public void setFeatures(List<SegmentDto> features) {
        this.features = features;
    }

    public SegmentListDto add(SegmentDto feature) {
        features.add(feature);
        return this;
    }

    public void addAll(Collection<SegmentDto> features) {
        this.features.addAll(features);
    }

    @Override
    public Iterator<SegmentDto> iterator() {
        return features.iterator();
    }

    public static SegmentListDto parseGeoJsonString(String json) {
        try {
            return new ObjectMapper().readValue(json, SegmentListDto.class);
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
