/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.mapper.custom;

import com.rideon.model.dto.LineStringDto;
import com.rideon.segmentdetector.util.GisUtils;
import com.rideon.util.Constants;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.PrecisionModel;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 *
 * @author Fer
 */
public class LineStringCustomMapper extends CustomMapper<LineString, LineStringDto> {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(LineStringCustomMapper.class);
//    @Override
//    public void mapAtoB(LineString a, LineStringDto b, MappingContext context) {
//        GeometryJSON g = new GeometryJSON();
//        StringWriter writer = new StringWriter();
//        try {
//            g.writeLine(a, writer);
//            b.setJsonGeom(writer.toString());
//        } catch (IOException ex) {
//            LOGGER.error("", ex);
//        }
//    }
//    @Override
//    public void mapBtoA(LineStringDto b, LineString a, MappingContext context) {
//        GeometryJSON g = new GeometryJSON();
//        LineString lineString;
//        try {
//            lineString = g.readLine(b.getJsonGeom());
//            GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), Constants.SRID);
//            a = geometryFactory.createLineString(lineString.getCoordinateSequence());
//        } catch (IOException ex) {
//            LOGGER.error("", ex);
//        }
//    }
    @Override
    public void mapAtoB(LineString a, LineStringDto b, MappingContext context) {
        try {
            String gpx = GisUtils.linestringToGPXString(a);
            b.setGpx(gpx);
        } catch (ParserConfigurationException | TransformerException ex) {
            LOGGER.error("", ex);
        }
    }

    @Override
    public void mapBtoA(LineStringDto b, LineString a, MappingContext context) {
        try {
            LineString lineString = GisUtils.gpxToLineString(b.getGpx());
            GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), Constants.SRID);
            a = geometryFactory.createLineString(lineString.getCoordinateSequence());
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            LOGGER.error("", ex);
        }
    }
}
