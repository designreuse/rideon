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
@JsonRootName(value = "segmentPracticePdDto")
public class SegmentPracticePkDto {

    private Long practiceId;
    private Long segmentId;

    public Long getPracticeId() {
        return practiceId;
    }

    public void setPracticeId(Long practiceId) {
        this.practiceId = practiceId;
    }

    public Long getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(Long segmentId) {
        this.segmentId = segmentId;
    }
}
