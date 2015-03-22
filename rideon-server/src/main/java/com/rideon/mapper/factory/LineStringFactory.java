/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.mapper.factory;

import com.rideon.model.dto.LineStringDto;
import com.rideon.segmentdetector.util.GisUtils;
import com.rideon.util.Constants;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.PrecisionModel;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.ObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 *
 * @author Fer
 */
public class LineStringFactory implements ObjectFactory<LineString> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LineStringFactory.class);

    @Override
    public LineString create(Object source, MappingContext mappingContext) {
        LineStringDto dto = (LineStringDto) source;
        try {
            LineString lineString = GisUtils.gpxToLineString(dto.getGpx());
            GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), Constants.SRID);
            LineString a = geometryFactory.createLineString(lineString.getCoordinateSequence());
            return a;
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            LOGGER.error("", ex);
        }
        return null;
    }
}
