package com.rideon.segmentdetector;

import com.rideon.common.enums.SegmentCategory;
import com.rideon.model.domain.Practice;
import com.rideon.model.domain.Route;
import com.rideon.model.domain.Segment;
import com.rideon.model.domain.SegmentPractice;
import com.rideon.segmentdetector.util.SegmentUtils;
import com.rideon.common.enums.SegmentType;
import com.vividsolutions.jts.geom.Coordinate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 *
 */
public class SegmentDetector {

    public static final Logger LOGGER = LoggerFactory.getLogger(SegmentDetector.class);
    //Minimun segment's size
    public static final int MIN_CLIMB_SIZE = 2000;
    public static final int MIN_DOWNHILL_SIZE = 4000;
    public static final int MIN_FLAT_SIZE = 10000;
    //
    public static final double MIN_SEGMENT_FLAT_MEASURE = -15;
    public static final double MAX_SEGMENT_FLAT_MEASURE = 15;
    public static final int MAX_DISTANCE_BETWEEN_SEGMENTS = 1500;
    //Number of iterations for joining segments
    public static final int JOIN_SEGMENTS_ITERATIONS = 4;
    //Parts info
    public static final double PART_SIZE = 400;
    public static final double MIN_PART_CLIMB_SIZE = 50;
    public static final double MAX_PART_DONWHILL_SIZE = 50;
    public static final double MIN_PART_FLAT_SIZE = 100;
    public static final double MIN_PART_CLIMB_MEASURE = 5;
    public static final double MIN_PART_DOWNHILL_MEASURE = -5;
    public static final double MIN_PART_FLAT_MEASURE = -7;
    public static final double MAX_PART_FLAT_MEASURE = 7;
    public static final double MIN_SEGMENT_FLAT_INCLINATION = -7;
    public static final double MAX_SEGMENT_FLAT_INCLINATION = 7;

    public static final int MAX_CONTIGUOUS_UNDESIRED_PARTS = 3;
    public static Boolean RUN_IN_THREADS = false;

    public static Map<Segment, SegmentPractice> detectSegments(Practice practice) throws TransformException {

        Long ti = System.currentTimeMillis();
        Map<Segment, SegmentPractice> map = new HashMap<>();

        Map<Segment, SegmentPractice> climbs = null;
        Map<Segment, SegmentPractice> downHills = null;
        Map<Segment, SegmentPractice> flats = null;
        if (!RUN_IN_THREADS) {
            climbs = detectClibms(practice);
            downHills = detectDownhills(practice);
            flats = detectFlats(practice);
        } else {
            try {
                SegmentDetectorClimbsThread climbsRun = new SegmentDetectorClimbsThread(practice);
                Thread climbsTh = new Thread(climbsRun);
                climbsTh.start();

                SegmentDetectorDownHillThread downHillRun = new SegmentDetectorDownHillThread(practice);
                Thread downhillTh = new Thread(downHillRun);
                downhillTh.start();

                SegmentDetectorFlatThread flatRun = new SegmentDetectorFlatThread(practice);
                Thread flatTh = new Thread(flatRun);
                flatTh.start();

                climbsTh.join();
                climbs = climbsRun.getResult();

                downhillTh.join();
                downHills = downHillRun.getResult();

                flatTh.join();
                flats = flatRun.getResult();

            } catch (InterruptedException ex) {
                LOGGER.error("", ex);
            }

        }
        map.putAll(climbs);
        map.putAll(downHills);
        map.putAll(flats);
        Long to = System.currentTimeMillis();
        Long time = (to - ti);
        LOGGER.info("Segment detector time: {}s ", time);
        System.out.println("Segment detector time: " + time + "ms ");
        return map;
    }

    public static Map<Segment, SegmentPractice> detectClibms(Practice practice) throws TransformException {
        Coordinate[] coors = practice.getRoute().getGeometry().getCoordinates();
        Map<Segment, SegmentPractice> map;

        List<int[]> segmentsCoordinates = searchClimbs(coors);
        segmentsCoordinates = processClimbs(coors, segmentsCoordinates);

        map = SegmentUtils.coordinatesToSegmentPractice(practice, segmentsCoordinates, SegmentType.CLIMB);

        return map;
    }

    public static List<Segment> detectClibms(Route route) throws TransformException {
        Coordinate[] coors = route.getGeometry().getCoordinates();
        List<Segment> segments;

        List<int[]> segmentsCoordinates = searchClimbs(coors);
        segmentsCoordinates = processClimbs(coors, segmentsCoordinates);

        segments = SegmentUtils.coordinatesToSegments(coors, segmentsCoordinates, SegmentType.CLIMB);

        return segments;
    }

    public static Map<Segment, SegmentPractice> detectDownhills(Practice practice) throws TransformException {
        Map<Segment, SegmentPractice> map;
        Coordinate[] coors = practice.getRoute().getGeometry().getCoordinates();
        List<int[]> segmentsCoordinates = searchDownhills(coors);
        segmentsCoordinates = processDownhills(coors, segmentsCoordinates);

        map = SegmentUtils.coordinatesToSegmentPractice(practice, segmentsCoordinates, SegmentType.DOWNHILL);

        return map;
    }

    public static List<Segment> detectDownhills(Route route) throws TransformException {
        Coordinate[] coors = route.getGeometry().getCoordinates();
        List<Segment> segments;

        List<int[]> segmentsCoordinates = searchDownhills(coors);
        segmentsCoordinates = processDownhills(coors, segmentsCoordinates);

        segments = SegmentUtils.coordinatesToSegments(coors, segmentsCoordinates, SegmentType.DOWNHILL);

        return segments;
    }

    public static Map<Segment, SegmentPractice> detectFlats(Practice practice) throws TransformException {
        Map<Segment, SegmentPractice> map;
        Coordinate[] coors = practice.getRoute().getGeometry().getCoordinates();
        List<int[]> segmentsCoordinates = searchFlats(coors);
        segmentsCoordinates = processFlats(coors, segmentsCoordinates);

        map = SegmentUtils.coordinatesToSegmentPractice(practice, segmentsCoordinates, SegmentType.FLAT);

        return map;
    }

    public static List<Segment> detectFlats(Route route) throws TransformException {
        Coordinate[] coors = route.getGeometry().getCoordinates();
        List<Segment> segments;

        List<int[]> segmentsCoordinates = searchFlats(coors);
        segmentsCoordinates = processFlats(coors, segmentsCoordinates);

        segments = SegmentUtils.coordinatesToSegments(coors, segmentsCoordinates, SegmentType.FLAT);

        return segments;
    }

    static List<int[]> searchClimbs(Coordinate[] coors) throws TransformException {
        int from;
        int to = 0;
        List<int[]> climbs = new ArrayList<>();

        int contigousDownhillParts;

        while (to < coors.length) {
            from = to;
            to++;
            contigousDownhillParts = 0;
            while (contigousDownhillParts < MAX_CONTIGUOUS_UNDESIRED_PARTS && to < coors.length) {
                if (SegmentUtils.expetedInclination(coors, to) < 0) {
                    from++;
                    contigousDownhillParts++;
                } else {
                    contigousDownhillParts = 0;
                }
                to++;
            }

            int rTo = to == coors.length ? to - contigousDownhillParts - 1 : to - contigousDownhillParts;
            double partSize = SegmentUtils.linestringDistance(coors, from, rTo);
            double elevationMeasure = SegmentUtils.getElevationMeasure(coors, from, rTo);
            if (partSize > MIN_PART_CLIMB_SIZE && elevationMeasure > MIN_PART_CLIMB_MEASURE) {
                int[] s = new int[2];
                s[0] = from;
                s[1] = rTo;
                climbs.add(s);
            }
        }
        return climbs;
    }

    static List<int[]> searchDownhills(Coordinate[] coors) throws TransformException {
        int from;
        int to = 0;
        List<int[]> climbs = new ArrayList<>();

        int contiguousClimbParts;

        while (to < coors.length) {
            from = to;
            to++;
            contiguousClimbParts = 0;
            while (contiguousClimbParts < MAX_CONTIGUOUS_UNDESIRED_PARTS && to < coors.length) {
                if (SegmentUtils.expetedInclination(coors, to) > MIN_PART_DOWNHILL_MEASURE) {
                    from++;
                    contiguousClimbParts++;
                } else {
                    contiguousClimbParts = 0;
                }
                to++;
            }

            int rTo = to == coors.length ? to - contiguousClimbParts - 1 : to - contiguousClimbParts;
            double partSize = SegmentUtils.linestringDistance(coors, from, rTo);
            double elevationMeasure = SegmentUtils.getElevationMeasure(coors, from, rTo);
            if (partSize > MAX_PART_DONWHILL_SIZE && elevationMeasure < MIN_PART_DOWNHILL_MEASURE) {
                int[] s = new int[2];
                s[0] = from;
                s[1] = rTo;
                climbs.add(s);
            }
        }
        return climbs;
    }

    static List<int[]> searchFlats(Coordinate[] coors) throws TransformException {
        int from;
        int to = 0;
        List<int[]> climbs = new ArrayList<>();

        int contiguousNonFlatParts;

        while (to < coors.length) {
            from = to;
            to++;
            contiguousNonFlatParts = 0;
            while (contiguousNonFlatParts < MAX_CONTIGUOUS_UNDESIRED_PARTS && to < coors.length) {
                double expectedInclination = SegmentUtils.expetedInclination(coors, to);
                if (expectedInclination < MIN_PART_FLAT_MEASURE || expectedInclination > MAX_PART_FLAT_MEASURE) {
                    from++;
                    contiguousNonFlatParts++;
                } else {
                    contiguousNonFlatParts = 0;
                }
                to++;
            }

            int rTo = to == coors.length ? to - contiguousNonFlatParts - 1 : to - contiguousNonFlatParts;
            double partSize = SegmentUtils.linestringDistance(coors, from, rTo);
            double elevationMeasure = SegmentUtils.getElevationMeasure(coors, from, rTo);
            if (partSize > MIN_PART_FLAT_SIZE && elevationMeasure < MAX_SEGMENT_FLAT_MEASURE && elevationMeasure > MIN_SEGMENT_FLAT_MEASURE) {
                int[] s = new int[2];
                s[0] = from;
                s[1] = rTo;
                climbs.add(s);
            }
        }
        return climbs;

    }

    static List<int[]> processClimbs(Coordinate[] coors, List<int[]> climbs) throws TransformException {
        List<int[]> tclimbs;
        for (int i = 0; i < JOIN_SEGMENTS_ITERATIONS; i++) {
            tclimbs = new ArrayList<>();
            for (int[] c1 : climbs) {
                if (tclimbs.isEmpty()) {
                    tclimbs.add(c1);
                } else {
                    int[] c0 = tclimbs.get(tclimbs.size() - 1);
                    double distance = SegmentUtils.linestringDistance(coors, c0[1], c1[0]);
                    double elevationMeasureBetwenSegments = SegmentUtils.getElevationMeasure(coors, c0[1], c1[1]);
                    if (distance < MAX_DISTANCE_BETWEEN_SEGMENTS
                            && elevationMeasureBetwenSegments > MIN_PART_CLIMB_MEASURE
                            && coors[c0[0]].z < coors[c1[0]].z) {
                        c0[1] = c1[1];
                    } else {
                        tclimbs.add(c1);
                    }
                }
            }
            climbs = tclimbs;
        }

        tclimbs = new ArrayList<>();
        for (int[] c : climbs) {
            SegmentCategory category = SegmentUtils.getCategory(coors, c[0], c[1]);
//            double segmentsElevationMeasure = SegmentUtils.getElevationMeasure(coors, c[0], c[1]);
            if (SegmentUtils.linestringDistance(coors, c[0], c[1]) > MIN_CLIMB_SIZE && category != null) {
//            if (SegmentUtils.linestringDistance(coors, c[0], c[1]) > MIN_CLIMB_SIZE && segmentsElevationMeasure > MIN_SEGMENT_ELEVATION_MEASURE) {
                tclimbs.add(c);
            }
        }
        climbs = tclimbs;

        return climbs;
    }

    static List<int[]> processDownhills(Coordinate[] coors, List<int[]> climbs) throws TransformException {
        List<int[]> tclimbs;
        for (int i = 0; i < JOIN_SEGMENTS_ITERATIONS; i++) {
            tclimbs = new ArrayList<>();
            for (int[] c1 : climbs) {
                if (tclimbs.isEmpty()) {
                    tclimbs.add(c1);
                } else {
                    int[] c0 = tclimbs.get(tclimbs.size() - 1);
                    double distance = SegmentUtils.linestringDistance(coors, c0[1], c1[0]);
                    double elevationMeasureBetwenSegments = SegmentUtils.getElevationMeasure(coors, c0[1], c1[1]);
                    if (distance < MAX_DISTANCE_BETWEEN_SEGMENTS && elevationMeasureBetwenSegments < 0) {
                        c0[1] = c1[1];
                    } else {
                        tclimbs.add(c1);
                    }
                }
            }
            climbs = tclimbs;
        }

        tclimbs = new ArrayList<>();
        for (int[] c : climbs) {
            double segmentsElevationMeasure = SegmentUtils.getElevationMeasure(coors, c[0], c[1]);
            SegmentCategory category = SegmentUtils.getCategory(coors, c[0], c[1]);
            if (SegmentUtils.linestringDistance(coors, c[0], c[1]) > MIN_DOWNHILL_SIZE && category != null) {
//            if (SegmentUtils.linestringDistance(coors, c[0], c[1]) > MIN_DOWNHILL_SIZE && segmentsElevationMeasure < MIN_SEGMENT_DOWNHILL_MEASURE) {
                tclimbs.add(c);
            }
        }
        climbs = tclimbs;

        return climbs;
    }

    static List<int[]> processFlats(Coordinate[] coors, List<int[]> climbs) throws TransformException {
        List<int[]> tclimbs;
        for (int i = 0; i < JOIN_SEGMENTS_ITERATIONS; i++) {
            tclimbs = new ArrayList<>();
            for (int[] c1 : climbs) {
                if (tclimbs.isEmpty()) {
                    tclimbs.add(c1);
                } else {
                    int[] c0 = tclimbs.get(tclimbs.size() - 1);
                    double distance = SegmentUtils.linestringDistance(coors, c0[1], c1[0]);
                    double elevationMeasureBetwenSegments = SegmentUtils.getElevationMeasure(coors, c0[1], c1[1]);
                    if (distance < MAX_DISTANCE_BETWEEN_SEGMENTS
                            && elevationMeasureBetwenSegments < MAX_SEGMENT_FLAT_MEASURE
                            && elevationMeasureBetwenSegments > MIN_SEGMENT_FLAT_MEASURE) {
                        c0[1] = c1[1];
                    } else {
                        tclimbs.add(c1);
                    }
                }
            }
            climbs = tclimbs;
        }

        tclimbs = new ArrayList<>();
        for (int[] c : climbs) {
            double segmentsElevationMeasure = SegmentUtils.getElevationMeasure(coors, c[0], c[1]);
            double inclination = SegmentUtils.getInclination(coors, c[0], c[1]);
//            if (SegmentUtils.linestringDistance(coors, c[0], c[1]) > MIN_FLAT_SIZE
//                    && segmentsElevationMeasure > MIN_SEGMENT_FLAT_MEASURE
//                    && segmentsElevationMeasure < MAX_SEGMENT_FLAT_MEASURE) {
            if (SegmentUtils.linestringDistance(coors, c[0], c[1]) > MIN_FLAT_SIZE
                    && inclination > MIN_SEGMENT_FLAT_INCLINATION
                    && inclination < MAX_SEGMENT_FLAT_INCLINATION) {
                System.out.println(inclination);
                tclimbs.add(c);
            }
        }
        climbs = tclimbs;

        return climbs;
    }
}
