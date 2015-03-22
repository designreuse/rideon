/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.service;

import com.rideon.model.dto.PracticeDto;
import com.rideon.model.dto.RouteDto;
import com.rideon.model.dto.SegmentPracticeDto;
import com.rideon.model.dto.list.BaseListDto;
import com.rideon.common.enums.Privacy;
import java.io.IOException;
import java.util.Date;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.alternativevision.gpx.beans.GPX;
import org.opengis.referencing.operation.TransformException;
import org.xml.sax.SAXException;

/**
 *
 * @author Fer
 */
public interface PracticeService {

    public PracticeDto add(String gpx, String userId) throws ParserConfigurationException, SAXException, IOException, TransformException;

    public PracticeDto add(GPX gpxDto, String userId) throws ParserConfigurationException, SAXException, IOException, TransformException;

    public void remove(String id);

    public PracticeDto getById(String id);

    public BaseListDto<PracticeDto> getByUser(String userId);

    public BaseListDto<PracticeDto> search(Date from, Date to, String userId);

    public BaseListDto<PracticeDto> search(Date date, String userId);

    public RouteDto getRouteById(String practiceId) throws ParserConfigurationException, TransformerException;

    public BaseListDto<SegmentPracticeDto> getSegmentById(String practiceId) throws ParserConfigurationException, TransformerException;

    public String getPracticeGPX(String practiceId) throws ParserConfigurationException, TransformerException;
    
    public PracticeDto changePrivacy(PracticeDto practiceDto);

    public String getOwnerUsername(String groupId);

    public Privacy getPrivacy(String groupId);
}
