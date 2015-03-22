/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.util;

import com.rideon.model.domain.SegmentPractice;
import java.util.Comparator;

/**
 *
 * @author Fer
 */
public class SegmentPracticeComparator implements Comparator<SegmentPractice> {

    @Override
    public int compare(SegmentPractice o1, SegmentPractice o2) {
        return (int)(o1.getDuration() - o2.getDuration());
    }
}
