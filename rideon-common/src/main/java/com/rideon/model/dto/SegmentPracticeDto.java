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
@JsonRootName(value = "segmentPracticeDto")
public class SegmentPracticeDto {

    private SegmentPracticePkDto id;
    private UserDto owner;
    private double mediumspeed;
    private long duration;
    private double maxspeed;

    public SegmentPracticePkDto getId() {
        return id;
    }

    public void setId(SegmentPracticePkDto id) {
        this.id = id;
    }

    public double getMediumspeed() {
        return mediumspeed;
    }

    public void setMediumspeed(double mediumspeed) {
        this.mediumspeed = mediumspeed;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public double getMaxspeed() {
        return maxspeed;
    }

    public void setMaxspeed(double maxspeed) {
        this.maxspeed = maxspeed;
    }

    public UserDto getOwner() {
        return owner;
    }

    public void setOwner(UserDto owner) {
        this.owner = owner;
    }
}
