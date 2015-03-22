/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rideon.common.enums.SegmentCategory;
import com.rideon.common.enums.SegmentType;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.geojson.Crs;
import org.geojson.LineString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Fer
 */
@JsonRootName(value = "segmentDto")
public class SegmentDto {

    private static final Logger LOGGER = LoggerFactory.getLogger(SegmentDto.class);
    private String type;
    private String id;
    private LineString geometry;
    private Crs crs;
    private double[] bbox;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> properties = new HashMap<>();

    public String getType() {
        return "Feature";
    }

    public void setType(String type) {
        this.type = type;
    }

    public LineString getGeometry() {
        return geometry;
    }

    public void setGeometry(LineString geometry) {
        this.geometry = geometry;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Crs getCrs() {
        return crs;
    }

    public void setCrs(Crs crs) {
        this.crs = crs;
    }

    public double[] getBbox() {
        return bbox;
    }

    public void setBbox(double[] bbox) {
        this.bbox = bbox;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public void setProperty(String key, Object value) {
        properties.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getProperty(String key) {
        return (T) properties.get(key);
    }

    public SegmentType getSegmentType() {
        return this.getProperty("segmentType");
    }

    public void setSegmentType(SegmentType type) {
        this.setProperty("segmentType", type);
    }

    public LineStringDto getGeom() {
        return this.getProperty("geom");
    }

    public void setGeom(LineStringDto geom) {
        this.setProperty("geom", geom);
    }

    public SegmentCategory getCategory() {
        return this.getProperty("category");
    }

    public void setCategory(SegmentCategory category) {
        this.setProperty("category", category);
    }

    public double getDistance() {
        return this.getProperty("distance");
    }

    public void setDistance(double distance) {
        this.setProperty("distance", distance);
    }

    public double getMinaltitude() {
        return this.getProperty("minaltitude");
    }

    public void setMinaltitude(double minaltitude) {
        this.setProperty("minaltitude", minaltitude);
    }

    public double getTotalclimb() {
        return this.getProperty("totalclimb");
    }

    public void setTotalclimb(double totalclimb) {
        this.setProperty("totalclimb", totalclimb);
    }

    public double getMaxaltitude() {
        return this.getProperty("maxaltitude");
    }

    public void setMaxaltitude(double maxaltitude) {
        this.setProperty("maxaltitude", maxaltitude);
    }

    public double getTotaldownhill() {
        return this.getProperty("totaldownhill");
    }

    public void setTotaldownhill(double totaldownhill) {
        this.setProperty("totaldownhill", totaldownhill);
    }

    public double getMaxInclination() {
        return this.getProperty("maxInclination");
    }

    public void setMaxInclination(double maxInclination) {
        this.setProperty("maxInclination", maxInclination);
    }

    public double getInclination() {
        return this.getProperty("inclination");
    }

    public void setInclination(double inclination) {
        this.setProperty("inclination", inclination);
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

    public static SegmentDto parseGeoJsonString(String json) {
        try {
            return new ObjectMapper().readValue(json, SegmentDto.class);
        } catch (IOException ex) {
            LOGGER.error("", ex);
            return null;
        }
    }
}
