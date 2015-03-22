/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.mapper.custom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rideon.segmentdetector.util.GisUtils;
import com.rideon.util.Constants;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.PrecisionModel;
import java.io.IOException;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;
import org.geojson.Crs;
import org.geojson.GeoJsonObject;
import org.geotools.geojson.geom.GeometryJSON;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 *
 * @author Fer
 */
public class LinestringJsonObjectCustomConverter extends BidirectionalConverter<LineString, org.geojson.LineString> {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(LinestringJsonObjectCustomConverter.class);

    @Override
    public org.geojson.LineString convertTo(LineString source, Type<org.geojson.LineString> destinationType) {
        org.geojson.LineString b;
        try {
            GeometryJSON g = new GeometryJSON();
            StringWriter writer = new StringWriter();
            g.writeLine(source, writer);
            b = new ObjectMapper().readValue(writer.toString(), org.geojson.LineString.class);
            Crs crs = new Crs();
            crs.getProperties().put("name", "EPSG:4326");
            b.setCrs(crs);
            return b;
        } catch (IOException ex) {
            Logger.getLogger(LinestringJsonObjectCustomConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public LineString convertFrom(org.geojson.LineString source, Type<LineString> destinationType) {
        try {
            LineString lineString = GisUtils.gpxToLineString(source.toString());
            GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), Constants.SRID);
            LineString a = geometryFactory.createLineString(lineString.getCoordinateSequence());
            return a;
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            LOGGER.error("", ex);
        }
        return null;
    }
}
