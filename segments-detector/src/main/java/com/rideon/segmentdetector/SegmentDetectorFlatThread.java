/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.segmentdetector;

import com.rideon.model.domain.Practice;
import org.opengis.referencing.operation.TransformException;

/**
 *
 * @author Fer
 */
public class SegmentDetectorFlatThread extends SegmentDetectorThread implements Runnable{

    public SegmentDetectorFlatThread(Practice practice) {
        super(practice);
    }

    @Override
    public void run() {
        try {
            result = SegmentDetector.detectFlats(practice);
        } catch (TransformException ex) {
            LOGGER.error("", ex);
        }
    }
    
    
}
