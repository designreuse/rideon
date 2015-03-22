/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.segmentdetector.util;

import com.rideon.model.domain.Practice;
import com.rideon.model.domain.Route;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.PrecisionModel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.alternativevision.gpx.GPXParser;
import org.alternativevision.gpx.beans.GPX;
import org.alternativevision.gpx.beans.Track;
import org.alternativevision.gpx.beans.Waypoint;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 *
 * @author fer
 */
public class GisUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(GisUtils.class);

    /**
     * Loads a gpx file and extract the track into a LineString object.
     *
     * @param url path to pgx file
     * @return LineString object
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static LineString gpxFileToLineString(String url) throws ParserConfigurationException, SAXException, IOException, ParserConfigurationException, ParserConfigurationException {
        FileInputStream fis = new FileInputStream(url);
        GPXParser p = new GPXParser();
        GPX gpx = p.parseGPX(fis);
        return gpxToLineString(gpx);
    }

    /**
     *
     * @param url
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static Practice gpxToPractice(String url) throws ParserConfigurationException, SAXException, IOException {
        FileInputStream fis = new FileInputStream(url);
        GPXParser p = new GPXParser();
        GPX gpx = p.parseGPX(fis);

        return gpxToPractice(gpx);
    }

    /**
     * Load the gpx and extract the route, the times.
     *
     * @param gpx
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static Practice gpxToPractice(GPX gpx) throws ParserConfigurationException, SAXException, IOException {
        Practice practice = null;
        try {
            Route route = new Route();
            route.setGeometry(gpxToLineString(gpx));
            double routeDistance = SegmentUtils.linestringDistance(route.getGeometry().getCoordinates(), 0, route.getGeometry().getCoordinates().length);
            routeDistance = Math.round(routeDistance);
            route.setDistance(routeDistance);
            List<Track> tracks = new ArrayList<>(gpx.getTracks());
            if (tracks.get(0).getName() != null) {
                route.setName(tracks.get(0).getName());
            }
            if (tracks.get(0).getComment() != null) {
                route.setDescription(tracks.get(0).getComment());
            }
            if (tracks.get(0).getDescription() != null) {
                route.setDescription(tracks.get(0).getDescription());
            }

            Double maxAltitude = null;
            Double minAltitude = null;
            double maxSpeed = 0;
            double totalClimb = 0;
            double totalDownhill = 0;

            ArrayList<Date> list = new ArrayList<>();
            if (gpx.getTracks().size() > 0) {
                Iterator<Track> itr = gpx.getTracks().iterator();
                while (itr.hasNext()) {
                    Track track = (Track) itr.next();

                    for (int i = 0; i < track.getTrackPoints().size(); i++) {
                        Waypoint c = track.getTrackPoints().get(i);
                        list.add(c.getTime());

                        if (c.getElevation() != null) {
                            if (maxAltitude == null) {
                                maxAltitude = c.getElevation();
                            }
                            if (minAltitude == null) {
                                minAltitude = c.getElevation();
                            }
                            maxAltitude = c.getElevation() > maxAltitude ? c.getElevation() : maxAltitude;
                            minAltitude = c.getElevation() < minAltitude ? c.getElevation() : minAltitude;
                        }

                        if (i > 0) {
                            Waypoint c2 = track.getTrackPoints().get(i - 1);
                            Coordinate a = waypointToCoordinate(c);
                            Coordinate b = waypointToCoordinate(c2);
                            double d = SegmentUtils.pointsDistance(a, b);
                            double s = getSpeedInKmH(d, (c.getTime().getTime() - c2.getTime().getTime()));

                            maxSpeed = s > maxSpeed ? s : maxSpeed;

                            if (c2.getElevation() != null && c.getElevation() != null && (c2.getElevation() - c.getElevation() > 0)) {
                                totalClimb += d;
                            } else {
                                totalDownhill += d;
                            }
                        }
                    }
                }
            }
            Date[] times = new Date[list.size()];
            list.toArray(times);


            long time0 = times[times.length - 1].getTime();
            long time1 = times[0].getTime();
            long duration = time0 - time1;
            double mediumspeed = getSpeedInKmH(routeDistance, duration);
            route.setDistance(routeDistance);
            route.setMaxaltitude(maxAltitude);
            route.setMinaltitude(minAltitude);
            route.setTotalclimb(Math.round(totalClimb));
            route.setTotaldownhill(Math.round(totalDownhill));

            practice = new Practice();
            practice.setRoute(route);
            practice.setTimes(times);
            practice.setPracticeDate(times[0]);
            practice.setDuration(duration);
            practice.setMediumspeed(Math.round(mediumspeed));
            practice.setMaxspeed(Math.round(maxSpeed));

        } catch (TransformException ex) {
            java.util.logging.Logger.getLogger(GisUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return practice;
    }

    public static LineString gpxToLineString(String gpxString) throws ParserConfigurationException, SAXException, IOException {
        GPXParser parser = new GPXParser();
        InputStream bis = new ByteArrayInputStream(gpxString.getBytes());
        GPX gpx = parser.parseGPX(bis);
        return gpxToLineString(gpx);
    }

    public static LineString gpxToLineString(GPX gpx) {
        if (gpx.getTracks().size() > 0) {
            Iterator<Track> itr = gpx.getTracks().iterator();
            while (itr.hasNext()) {
                Track track = (Track) itr.next();
                ArrayList<Coordinate> coordinates = new ArrayList<>();
                if (track.getTrackPoints().size() > 1) {
                    for (Waypoint c : track.getTrackPoints()) {
                        if (c.getElevation() != null) {
                            Coordinate cor = new Coordinate(c.getLongitude(), c.getLatitude(), c.getElevation());
                            coordinates.add(cor);
                        }
                    }
                    Coordinate[] cArr = new Coordinate[coordinates.size()];
                    coordinates.toArray(cArr);
                    GeometryFactory geomFac = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326);
                    LineString lineStr = geomFac.createLineString(cArr);
                    return lineStr;
                }
            }
        }
        return null;
    }

    public static Date[] getTimes(GPX gpx) {
        ArrayList<Date> list = new ArrayList<>();
        if (gpx.getTracks().size() > 0) {
            Iterator<Track> itr = gpx.getTracks().iterator();
            while (itr.hasNext()) {
                Track track = (Track) itr.next();
                for (Waypoint c : track.getTrackPoints()) {
                    list.add(c.getTime());
                }
            }
        }
        Date[] dArr = new Date[list.size()];
        list.toArray(dArr);
        return dArr;
    }

    public static GPX linestringToGPX(LineString lineString) {

        GPX gpx = new GPX();
        Track track = new Track();
        ArrayList<Waypoint> waypoints = new ArrayList<>();
        for (Coordinate c : lineString.getCoordinates()) {
            Waypoint wayPoint = new Waypoint();
            wayPoint.setLongitude(c.x);
            wayPoint.setLatitude(c.y);
            wayPoint.setElevation(c.z);
            waypoints.add(wayPoint);
        }
        track.setTrackPoints(waypoints);
        HashSet<Track> tracks = new HashSet<>();
        tracks.add(track);
        gpx.setTracks(tracks);

        return gpx;
    }

    public static GPX linestringToGPX(LineString lineString, Date[] times) {

        GPX gpx = new GPX();
        Track track = new Track();
        ArrayList<Waypoint> waypoints = new ArrayList<>();
        for (int i = 0; i < lineString.getCoordinates().length; i++) {
            Coordinate c = lineString.getCoordinates()[i];
            Waypoint wayPoint = new Waypoint();
            wayPoint.setLongitude(c.x);
            wayPoint.setLatitude(c.y);
            wayPoint.setElevation(c.z);
            wayPoint.setTime(times[i]);
            waypoints.add(wayPoint);
        }
        track.setTrackPoints(waypoints);
        HashSet<Track> tracks = new HashSet<>();
        tracks.add(track);
        gpx.setTracks(tracks);

        return gpx;
    }

    public static String linestringToGPXString(LineString lineString) throws ParserConfigurationException, TransformerException {
        GPX gpx = linestringToGPX(lineString);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        GPXParser parser = new GPXParser();
        parser.writeGPX(gpx, baos);

        return baos.toString();
    }

    public static String linestringToGPXString(LineString lineString, Date[] times) throws ParserConfigurationException, TransformerException {
        GPX gpx = linestringToGPX(lineString, times);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        GPXParser parser = new GPXParser();
        parser.writeGPX(gpx, baos);

        return baos.toString();
    }

    public static GPX parseGpx(String url) throws FileNotFoundException, ParserConfigurationException, SAXException, IOException {
        FileInputStream fis = new FileInputStream(url);
        GPXParser parser = new GPXParser();
        GPX gpx = parser.parseGPX(fis);
        return (GPX) gpx;
    }

    public static Geometry applyBufferInMeters(Geometry geom, double distance) {
        Geometry buffer = geom;
        try {
            CoordinateReferenceSystem wsgCRS = DefaultGeographicCRS.WGS84;
            CoordinateReferenceSystem metersCRS = CRS.decode("EPSG:26986", true);
            MathTransform transform = CRS.findMathTransform(wsgCRS, metersCRS);
            Geometry targetGeometryetry = JTS.transform(geom, transform);
            buffer = targetGeometryetry.buffer(distance);
            transform = CRS.findMathTransform(metersCRS, wsgCRS);
            buffer = JTS.transform(buffer, transform);
        } catch (Exception ex) {
            LOGGER.error("", ex);
        }
        return buffer;
    }

    public static Boolean compareWith(LineString linestring1, LineString linestring2) {
        try {
            Geometry buffer = applyBufferInMeters(linestring1, 100);
            Boolean covers = buffer.covers(linestring2);
            double distance1 = SegmentUtils.linestringDistance(linestring1.getCoordinates(), 0, linestring1.getNumPoints());
            double distance2 = SegmentUtils.linestringDistance(linestring2.getCoordinates(), 0, linestring2.getNumPoints());
            double distancePercentaje = (distance2 / distance1);
            if (distancePercentaje > 0.95 && covers) {
                LOGGER.debug("LineString 1 covers the {} % of the LineString 2 distance", (distancePercentaje * 100));
                return true;
            }
        } catch (TransformException ex) {
            LOGGER.error("", ex);
        }

        return false;
    }

    public static Coordinate waypointToCoordinate(Waypoint w) {
        return new Coordinate(w.getLongitude(), w.getLatitude());
    }

    public static double getSpeedInKmH(double distance, long timeinmiliseconds) {
        if (timeinmiliseconds <= 0) {
            return 0;
        }
        long sec = timeinmiliseconds / 1000;
        double speedMs = distance / sec * (3.6);
        return speedMs;
    }
}
