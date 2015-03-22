/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.dto;

import com.rideon.common.enums.Privacy;
import java.util.Date;
import java.util.List;
import org.codehaus.jackson.map.annotate.JsonRootName;

/**
 *
 * @author Fer
 */
@JsonRootName(value = "practiceDto")
public class PracticeDto {

    private Long id;
    private UserDto owner;
    private Privacy privacy;
    private Date practiceDate;
    private double mediumspeed;
    private long duration;
    private double maxspeed;
    private List<SegmentPracticeDto> segmentsResults;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDto getOwner() {
        return owner;
    }

    public void setOwner(UserDto owner) {
        this.owner = owner;
    }

    public Privacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
    }

    public Date getPracticeDate() {
        return practiceDate;
    }

    public void setPracticeDate(Date practiceDate) {
        this.practiceDate = practiceDate;
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

    public List<SegmentPracticeDto> getSegmentsResults() {
        return segmentsResults;
    }

    public void setSegmentsResults(List<SegmentPracticeDto> segmentsResults) {
        this.segmentsResults = segmentsResults;
    }
}
