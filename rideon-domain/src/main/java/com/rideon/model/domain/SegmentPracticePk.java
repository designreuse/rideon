/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;

/**
 *
 * @author Fer
 */
@Embeddable
public class SegmentPracticePk implements Serializable {

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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(this.practiceId);
        hash = 43 * hash + Objects.hashCode(this.segmentId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SegmentPracticePk other = (SegmentPracticePk) obj;
        if (!Objects.equals(this.practiceId, other.practiceId)) {
            return false;
        }
        if (!Objects.equals(this.segmentId, other.segmentId)) {
            return false;
        }
        return true;
    }
    
    

}
