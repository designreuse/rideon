/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.test.service;

import com.rideon.model.dao.SegmentDao;
import com.rideon.model.dto.PracticeDto;
import com.rideon.common.enums.Privacy;
import com.rideon.segmentdetector.util.GisUtils;
import com.rideon.model.service.PracticeService;
import com.rideon.model.service.RouteService;
import com.rideon.test.BaseTest;
import com.rideon.util.FilePaths;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import ma.glasnost.orika.MapperFacade;
import org.alternativevision.gpx.beans.GPX;
import org.junit.Test;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.xml.sax.SAXException;

/**
 *
 * @author Fer
 */
public class PracticeServiceTest extends BaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PracticeServiceTest.class);
    @Autowired
    PracticeService practiceService;
    @Autowired
    SegmentDao segmentDao;
    @Autowired
    RouteService routeService;
    @Autowired
    MapperFacade mapper;

    @BeforeTransaction
    public void addOnePractice() throws FileNotFoundException, ParserConfigurationException, SAXException, IOException, TransformException {
        ArrayList<String> list = new ArrayList<>();
        list.add(FilePaths.ROUTE_1);
        list.add(FilePaths.ROUTE_2);
        list.add(FilePaths.ROUTE_3);
        list.add(FilePaths.ROUTE_4);
        list.add(FilePaths.ROUTE_5);


        for (String url : list) {
            GPX gpx = GisUtils.parseGpx(getClass().getResource(url).getFile());
            try {
                PracticeDto pr = practiceService.add(gpx, "vilas");
                pr.setPrivacy(Privacy.PUBLIC);
                practiceService.changePrivacy(pr);
            } catch (IllegalArgumentException ex) {
                LOGGER.error("Duplicate practice");
            }
        }

        list = new ArrayList<>();
        list.add(FilePaths.ROUTE_6);
        list.add(FilePaths.ROUTE_7);
        list.add(FilePaths.ROUTE_8);
        list.add(FilePaths.ROUTE_9);
        list.add(FilePaths.ROUTE_10);
        list.add(FilePaths.ROUTE_VM_1);
        list.add(FilePaths.ROUTE_VM_2);
        list.add(FilePaths.ROUTE_VM_3);
        for (String url : list) {
            GPX gpx = GisUtils.parseGpx(getClass().getResource(url).getFile());
            try {
                PracticeDto pr = practiceService.add(gpx, "artur");
                pr.setPrivacy(Privacy.PUBLIC);
                practiceService.changePrivacy(pr);
            } catch (IllegalArgumentException ex) {
                LOGGER.error("Duplicate practice");
            }
        }

        list = new ArrayList<>();
        list.add(FilePaths.ROUTE_EVENT);
        for (String url : list) {
            GPX gpx = GisUtils.parseGpx(getClass().getResource(url).getFile());
            try {
                PracticeDto pr = practiceService.add(gpx, "admin");
                pr.setPrivacy(Privacy.PUBLIC);
                practiceService.changePrivacy(pr);
            } catch (IllegalArgumentException ex) {
                LOGGER.error("Duplicate practice");
            }
        }
    }

    @Test
    public void addPractice() throws FileNotFoundException, ParserConfigurationException, SAXException, IOException, TransformException {
//        PracticeDto practice = addPractice(getClass().getResource(FilePaths.ROUTE_5).getFile());
//
//        practice = practiceService.getById(practice.getId().toString());
//
//        Assert.assertNotNull(practice.getRoute().getGeom().getPoints());
//        Assert.assertEquals(practice.getSegments().size(), 3);
//        Assert.assertEquals(practice.getSegments().size(), practice.getRoute().getSegments().size());
//        Assert.assertNotNull(practice.getRoute());
//        Assert.assertEquals(practice.getRoute().getGeom().getPoints().toCoordinateArray().length, practice.getTimes().length);
//
//        SegmentDto segmentDto = (SegmentDto) practice.getRoute().getSegments().toArray()[0];
//        Segment s = mapper.map(segmentDto, Segment.class);
//        Set<Segment> setSeg = segmentDao.searchSegment(s.getGeom());
//        if (!setSeg.isEmpty()) {
//            LOGGER.debug("Found {} segments", setSeg.size());
//        }
//        Assert.assertEquals(setSeg.size(), 1);
//        Assert.assertEquals(((Segment) setSeg.toArray()[0]).getType(), s.getType());
//        PracticeDto practice4 = addPractice(getClass().getResource(FilePaths.ROUTE_4).getFile());
//        PracticeDto practice41 = addPractice(getClass().getResource(FilePaths.ROUTE_4_1).getFile());
    }

    public PracticeDto addPractice(String url) throws FileNotFoundException, ParserConfigurationException, SAXException, IOException, TransformException {
        GPX gpx = GisUtils.parseGpx(url);
//            Set<PracticeDto> set = practiceService.getByUser("vilas");
        return practiceService.add(gpx, "mati");

    }
}
