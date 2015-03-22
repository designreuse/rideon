/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.dto;

import org.codehaus.jackson.map.annotate.JsonRootName;

/**
 *
 * @author Fer
 */
@JsonRootName(value = "lineStringDto")
public class LineStringDto {

    private String gpx;

    public String getGpx() {
        return gpx;
    }

    public void setGpx(String gpx) {
        this.gpx = gpx;
    }
}
