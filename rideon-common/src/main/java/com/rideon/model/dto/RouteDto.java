/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@JsonRootName(value = "routeDto")
public class RouteDto {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteDto.class);
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

    public String getName() {
        return this.getProperty("name");
    }

    public void setName(String name) {
        this.setProperty("name", name);
    }

    public String getDescription() {
        return this.getProperty("description");
    }

    public void setDescription(String description) {
        this.setProperty("description", description);
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

    public double getDistance() {
        return this.getProperty("distance");
    }

    public void setDistance(double distance) {
        this.setProperty("distance", distance);
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

    public static RouteDto parseGeoJsonString(String json) {
        try {
            return new ObjectMapper().readValue(json, RouteDto.class);
        } catch (IOException ex) {
            LOGGER.error("", ex);
            return null;
        }
    }
}
