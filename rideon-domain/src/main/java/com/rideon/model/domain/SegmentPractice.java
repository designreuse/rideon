/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Transient;

/**
 *
 * @author Fer
 */
@Entity(name = "segment_practice")
@AssociationOverrides({
    @AssociationOverride(name = "id.practice",
            joinColumns =
            @JoinColumn(name = "practice_id")),
    @AssociationOverride(name = "id.segment",
            joinColumns =
            @JoinColumn(name = "segment_id"))})
public class SegmentPractice implements Serializable {

    private SegmentPracticePk id = new SegmentPracticePk();
    private Date[] times;
    private double mediumspeed;
    private long duration;
    private double maxspeed;

    @EmbeddedId
    public SegmentPracticePk getId() {
        return id;
    }

    public void setId(SegmentPracticePk id) {
        this.id = id;
    }

    @Transient
    public Long getPractice() {
        return this.id.getPracticeId();
    }

    public void setPractice(Practice practice) {
        this.id.setPracticeId(practice.getId());
    }

    @Transient
    public Long getSegment() {
        return this.id.getSegmentId();
    }

    public void setSegment(Segment segment) {
        this.id.setSegmentId(segment.getId());
    }

    public Date[] getTimes() {
        return times;
    }

    public void setTimes(Date[] times) {
        this.times = times;
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
}
