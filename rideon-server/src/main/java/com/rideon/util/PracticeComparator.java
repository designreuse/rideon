/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.util;

import com.rideon.model.domain.Practice;
import java.util.Comparator;

/**
 *
 * @author Fer
 */
public class PracticeComparator implements Comparator<Practice> {

    @Override
    public int compare(Practice o1, Practice o2) {
        return (int)(o1.getDuration() - o2.getDuration());
    }
}
