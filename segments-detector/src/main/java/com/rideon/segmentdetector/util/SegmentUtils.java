/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.segmentdetector.util;

import com.rideon.common.enums.SegmentCategory;
import com.rideon.segmentdetector.SegmentDetector;
import com.rideon.model.domain.Practice;
import com.rideon.model.domain.Segment;
import com.rideon.model.domain.SegmentPractice;
import com.rideon.common.enums.SegmentType;
import static com.rideon.segmentdetector.util.GisUtils.getSpeedInKmH;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.PrecisionModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.ArrayUtils;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Fer
 */
public class SegmentUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(SegmentUtils.class);

//    /**
//     *
//     * @param values
//     * @return
//     */
//    public static double media(double[] values) {
//        double mean = 0;
//        for (double d : values) {
//            mean += d;
//        }
//        return mean / values.length;
//    }
//    /**
//     *
//     * @param values
//     * @return
//     */
//    public static double standarDesviation(double[] values) {
//        double mean = media(values);
//        double ds = 0;
//        for (double d : values) {
//            ds += (d - mean) * (d - mean);
//        }
//        return Math.sqrt(ds / values.length);
//    }
    /**
     *
     * @param coors
     * @return
     */
    public static double[] getElevations(Coordinate[] coors) {
        double[] e = new double[coors.length];
        for (int i = 0; i < coors.length; i++) {
            e[i] = coors[i].z;
        }
        return e;
    }

    public static int getPosForDinstanceAhead(Coordinate[] coors, double distance, int initPosition) throws TransformException {
        double dist = 0;
        int to = initPosition + 1;
        while (dist < distance && to < coors.length) {
            double parcial = JTS.orthodromicDistance(coors[to - 1], coors[to], DefaultGeographicCRS.WGS84);
            dist += parcial;
            to += 1;
        }
        return to;
    }

    public static int getPosForDinstanceBack(Coordinate[] coors, double distance, int initPosition) throws TransformException {
        double dist = 0;
        int from = initPosition - 1;
        while (dist < distance && from > 0) {
            double parcial = JTS.orthodromicDistance(coors[from], coors[from - 1], DefaultGeographicCRS.WGS84);
            dist += parcial;
            from--;
        }
        return from;
    }

    public static double linestringDistance(Coordinate[] coors, int from, int to) throws TransformException {
        double dist = 0;
        for (int i = from; i < to && i < coors.length - 1; i++) {
            double parcial = JTS.orthodromicDistance(coors[i], coors[i + 1], DefaultGeographicCRS.WGS84);
            dist += parcial;
        }
        return dist;
    }

    public static double pointsDistance(Coordinate a, Coordinate b) throws TransformException {
        double dist = JTS.orthodromicDistance(a, b, DefaultGeographicCRS.WGS84);
        return dist;
    }

    public static double expetedInclination(Coordinate[] coors, int position) throws TransformException {
        int to = SegmentUtils.getPosForDinstanceAhead(coors, SegmentDetector.PART_SIZE / 2, position);
        int from = SegmentUtils.getPosForDinstanceBack(coors, SegmentDetector.PART_SIZE / 2, position);
        return getElevationMeasure(coors, from, to);
    }

    public static double getElevationMeasure(Coordinate[] coors, int from, int to) {
        double measure = 0;
        for (int i = from + 1; (i < to && i < coors.length - 1); i++) {
            measure += coors[i + 1].z - coors[i].z;
        }
        return measure;
    }

    public static List<Segment> coordinatesToSegments(Coordinate[] coors, List<int[]> points, SegmentType type) throws TransformException {
        List<Segment> segments = new ArrayList<>();
        for (int[] c : points) {
            segments.add(coordinatesToSegment(coors, c, type));
        }
        return segments;
    }

    public static Map<Segment, SegmentPractice> coordinatesToSegmentPractice(Practice practice, List<int[]> points, SegmentType type) {
        Map<Segment, SegmentPractice> segments = new HashMap<>();
        Coordinate[] coors = practice.getRoute().getGeometry().getCoordinates();
        for (int[] c : points) {
            try {
                segments.put(coordinatesToSegment(coors, c, type), getSegmentPractice(practice, c));
            } catch (TransformException ex) {
                LOGGER.error("", ex);
            }
        }
        return segments;
    }

    public static Segment coordinatesToSegment(Coordinate[] coors, int[] points, SegmentType type) throws TransformException {
//        Coordinate[] coors = practice.getRoute().getGeometry().getCoordinates();
        Segment segment = new Segment();
        segment.setSegmentType(type);
        Coordinate[] segmentCoors = (Coordinate[]) ArrayUtils.subarray(coors, points[0], points[1]);
        GeometryFactory geomFac = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326);
        LineString geom = geomFac.createLineString(segmentCoors);
        segment.setGeometry(geom);
        segment.setCategory(getCategory(coors, points[0], points[1]));


        double totalClimb = 0;
        double totalDownhill = 0;
        Double maxAltitude = null;
        Double minAltitude = null;
        double maxInclination = 0;

        for (int i = points[0]; i <= points[1]; i++) {
            Coordinate c = coors[i];


            if (maxAltitude == null) {
                maxAltitude = c.z;
            }
            if (minAltitude == null) {
                minAltitude = c.z;
            }
            maxAltitude = c.z > maxAltitude ? c.z : maxAltitude;
            minAltitude = c.z < minAltitude ? c.z : minAltitude;

            if (i > points[0]) {
                Coordinate c2 = coors[i - 1];
                double d = SegmentUtils.pointsDistance(c, c2);
                if ((c2.z - c.z > 0)) {
                    totalClimb += d;
                } else {
                    totalDownhill += d;
                }
                double inc = getInclination(coors, i - 1, i);
                if (type == SegmentType.DOWNHILL) {
                    maxInclination = inc < maxInclination ? inc : maxInclination;
                } else {
                    maxInclination = inc > maxInclination ? inc : maxInclination;
                }

            }
        }

        double segmentDistance = linestringDistance(coors, points[0], points[1]);
        double inclination = getInclination(coors, points[0], points[1]);
        segment.setDistance(Math.round(segmentDistance));
        segment.setMaxaltitude(maxAltitude);
        segment.setMinaltitude(minAltitude);
        segment.setTotalclimb(Math.round(totalClimb));
        segment.setTotaldownhill(Math.round(totalDownhill));
        double in = inclination * 1000;
        in = Math.round(in);
        in = in / 1000;
        segment.setInclination(in);
        double mIn = maxInclination * 1000;
        mIn = Math.round(mIn);
        mIn = mIn / 1000;
        segment.setMaxInclination(mIn);

        return segment;
    }

    public static SegmentPractice getSegmentPractice(Practice practice, int[] points) throws TransformException {
        SegmentPractice sp = new SegmentPractice();
        Date[] t = (Date[]) ArrayUtils.subarray(practice.getTimes(), points[0], points[1]);
        sp.setTimes(t);

        Coordinate[] coors = practice.getRoute().getGeometry().getCoordinates();

        double maxSpeed = 0;
        Date[] times = practice.getTimes();

        for (int i = points[0]; i <= points[1]; i++) {
            Coordinate c = coors[i];
            if (i > points[0]) {
                Coordinate c2 = coors[i - 1];
                double d = SegmentUtils.pointsDistance(c, c2);
                double s = getSpeedInKmH(d, (times[i].getTime() - times[i - 1].getTime()));

                maxSpeed = s > maxSpeed ? s : maxSpeed;
            }
        }

        double segmentDistance = linestringDistance(coors, points[0], points[1]);
        long duration = times[points[1]].getTime() - times[points[0]].getTime();
        double mediumspeed = GisUtils.getSpeedInKmH(segmentDistance, duration);

        sp.setDuration(duration);
        sp.setMediumspeed(Math.round(mediumspeed));
        sp.setMaxspeed(Math.round(maxSpeed));

        return sp;
    }

    public static double getInclination(Coordinate[] coors, int from, int to) throws TransformException {
        double distance = linestringDistance(coors, from, to);
        if (distance < 1) {
            return 0;
        }

        return (coors[to].z - coors[from].z) / linestringDistance(coors, from, to);
    }

    public static SegmentCategory getCategory(Coordinate[] coors, int from, int to) throws TransformException {
        double maxInclination = 0;
        double altitude = coors[to].z - coors[from].z;
        for (int i = from + 1; (i < to && i < coors.length - 1); i++) {
            double inclination = getInclination(coors, i - 1, i);
            maxInclination = inclination > maxInclination ? inclination : maxInclination;
        }
        double measure = Math.abs(altitude) / 10 + maxInclination;


        if (measure > 139) {
            return SegmentCategory.ESPECIAL;
        }
        if (80 < measure && measure < 139) {
            return SegmentCategory.FIRST;
        }
        if (50 < measure && measure < 79) {
            return SegmentCategory.SECOND;
        }
        if (30 < measure && measure < 49) {
            return SegmentCategory.THIRD;
        }
        if (20 < measure && measure < 19) {
            return SegmentCategory.FOURTH;
        }

        return null;
    }
}
