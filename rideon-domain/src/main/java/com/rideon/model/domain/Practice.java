/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.domain;

import com.rideon.common.enums.Privacy;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author Fer
 */
@Entity(name = "practices")
public class Practice implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private User owner;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "id.practiceId", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private Set<SegmentPractice> segmentsResults = new HashSet<>();
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Route route;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date practiceDate;
    private Date[] times;
    private double mediumspeed;
    private long duration;
    private double maxspeed;
    private Privacy privacy = Privacy.PRIVATE;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Date getPracticeDate() {
        return practiceDate;
    }

    public void setPracticeDate(Date practiceDate) {
        this.practiceDate = practiceDate;
    }

    public Date[] getTimes() {
        return times;
    }

    public void setTimes(Date[] times) {
        this.times = times;
    }

    public Set<SegmentPractice> getSegmentsResults() {
        return segmentsResults;
    }

    public void setSegmentsResults(Set<SegmentPractice> segmentsResults) {
        this.segmentsResults = segmentsResults;
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

    public Privacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Practice) {
            return this.id.equals(((Practice) obj).getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.id);
        return hash;
    }
}
