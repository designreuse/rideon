/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.segmentdetector;

import com.rideon.model.domain.Practice;
import com.rideon.segmentdetector.util.FilePaths;
import com.rideon.segmentdetector.util.GisUtils;
import com.rideon.segmentdetector.util.Helper;
import com.vividsolutions.jts.geom.Coordinate;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.opengis.referencing.operation.TransformException;
import org.xml.sax.SAXException;

/**
 *
 * @author Fer
 */
public class SegmentDetectorTest {

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, TransformException {
        Map<String, String> urls = new HashMap<>();

//        urls.put("Route 1", SegmentDetector.class.getResource(FilePaths.ROUTE_1).getFile());
        urls.put("Route 2", SegmentDetector.class.getResource(FilePaths.ROUTE_2).getFile());
        urls.put("Route 2_1", SegmentDetector.class.getResource(FilePaths.ROUTE_2_1).getFile());
//        urls.put("Route 3", SegmentDetector.class.getResource(FilePaths.ROUTE_3).getFile());
        urls.put("Route 4", SegmentDetector.class.getResource(FilePaths.ROUTE_4).getFile());
        urls.put("Route 4_1", SegmentDetector.class.getResource(FilePaths.ROUTE_4_1).getFile());
//        urls.put("Route 5", SegmentDetector.class.getResource(FilePaths.ROUTE_5).getFile());
//        urls.put("Route 6", SegmentDetector.class.getResource(FilePaths.ROUTE_6).getFile());
//        urls.put("Route 7", SegmentDetector.class.getResource(FilePaths.ROUTE_7).getFile());
//        urls.put("Route 8", SegmentDetector.class.getResource(FilePaths.ROUTE_8).getFile());
//        urls.put("Route 9", SegmentDetector.class.getResource(FilePaths.ROUTE_9).getFile());
//        urls.put("Route VM 1", SegmentDetector.class.getResource(FilePaths.ROUTE_VM_1).getFile());
//        urls.put("Route VM 2", SegmentDetector.class.getResource(FilePaths.ROUTE_VM_2).getFile());
//        urls.put("Route VM 3", SegmentDetector.class.getResource(FilePaths.ROUTE_VM_3).getFile());
        boolean printInter = false;

//        for (String routeName : urls.keySet()) {
//            Practice pr = GisUtils.loadPracticeGpx(urls.get(routeName));
//            Coordinate[] coors = pr.getRoute().getGeometry().getCoordinates();
//
//            List<int[]> climbs = SegmentsDetector.searchClimbs(coors);
//            if (printInter) {
//                SegmentUtils.printChart(coors, climbs, routeName + " Ascensos");
//            }
//            climbs = SegmentsDetector.processClimbs(coors, climbs);
//            SegmentUtils.printChart(coors, climbs, routeName + " Ascensos Unidos");
//        }

//        for (String routeName : urls.keySet()) {
//            Practice pr = GisUtils.loadPracticeGpx(urls.get(routeName));
//            Coordinate[] coors = pr.getRoute().getGeometry().getCoordinates();
//
//            List<int[]> climbs = SegmentsDetector.searchDownhills(coors);
//            if (printInter) {
//                SegmentUtils.printChart(coors, climbs, routeName + " Descensos");
//            }
//            climbs = SegmentsDetector.processDownhills(coors, climbs);
//            SegmentUtils.printChart(coors, climbs, routeName + " Descensos Unidos");
//        }

//        for (String routeName : urls.keySet()) {
//            Practice pr = GisUtils.loadPracticeGpx(urls.get(routeName));
//            Coordinate[] coors = pr.getRoute().getGeometry().getCoordinates();
//
//            List<int[]> climbs = SegmentsDetector.searchFlats(coors);
//            if (printInter) {
//                SegmentUtils.printChart(coors, climbs, routeName + " Planos");
//            }
//            climbs = SegmentsDetector.processFlats(coors, climbs);
//            SegmentUtils.printChart(coors, climbs, routeName + " Planos Unidos");
//        }

        for (String routeName : urls.keySet()) {
            Practice pr = GisUtils.gpxToPractice(urls.get(routeName));
            Coordinate[] coors = pr.getRoute().getGeometry().getCoordinates();

            List<int[]> seg = new ArrayList();

            
            List<int[]> climb = SegmentDetector.searchClimbs(coors);
            climb = SegmentDetector.processClimbs(coors, climb);
            seg.addAll(climb);
            
            List<int[]> down = SegmentDetector.searchDownhills(coors);
            down = SegmentDetector.processDownhills(coors, down);
            seg.addAll(down);

            List<int[]> flat = SegmentDetector.searchFlats(coors);
            flat = SegmentDetector.processFlats(coors, flat);
            seg.addAll(flat);

            Helper.printChart(coors, seg, routeName + " Unidos");
        }

    }
}