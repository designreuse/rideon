/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.segmentdetector;

import com.rideon.model.domain.Practice;
import com.rideon.model.domain.Segment;
import com.rideon.model.domain.SegmentPractice;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Fer
 */
public class SegmentDetectorThread {

    protected Practice practice;
    protected Map<Segment, SegmentPractice> result = new HashMap<>();
    protected static final Logger LOGGER = LoggerFactory.getLogger(SegmentDetectorThread.class);

    public SegmentDetectorThread(Practice practice) {
        this.practice = practice;
    }



    public Map<Segment, SegmentPractice> getResult() {
        return result;
    }
}
