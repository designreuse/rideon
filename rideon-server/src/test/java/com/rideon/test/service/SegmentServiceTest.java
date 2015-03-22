/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.test.service;

import com.rideon.model.service.SegmentService;
import com.rideon.test.BaseTest;
import ma.glasnost.orika.MapperFacade;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Fer
 */
public class SegmentServiceTest extends BaseTest {

    @Autowired
    SegmentService segmentService;
    @Autowired
    MapperFacade mapper;

    @Test
    public void addPractice() {
//        segmentService.getResults("4440");
//        SegmentDto segment = segmentService.get("1017");
//        Segment s = mapper.map(segment, Segment.class);
//        Assert.notNull(segment.getGeom().getJsonGeom());
//        Assert.notNull(s.getGeom().getCoordinates());
    }
}
