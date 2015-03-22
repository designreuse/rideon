/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.controller;

import com.rideon.model.dto.LineStringDto;
import com.rideon.model.dto.PracticeDto;
import com.rideon.model.dto.RouteDto;
import com.rideon.model.dto.SegmentPracticeDto;
import com.rideon.model.dto.list.BaseListDto;
import com.rideon.model.service.PracticeService;
import com.rideon.common.util.RestCommonPaths;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.xml.sax.SAXException;

/**
 *
 * @author Fer
 */
@Controller
public class PracticeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PracticeController.class);
    @Autowired
    PracticeService practiceService;

    @PreAuthorize("@userPermisions.isAllowed(authentication.name, #userId)")
    @RequestMapping(value = RestCommonPaths.USER_PRACTICES, method = RequestMethod.POST)
    public ResponseEntity<PracticeDto> add(@RequestBody LineStringDto gpx,
            @PathVariable(RestCommonPaths.USER_ID) String userId) {
        try {
            PracticeDto pr = (PracticeDto) practiceService.add(gpx.getGpx(), userId);
            return new ResponseEntity<>(pr, HttpStatus.CREATED);
        } catch (ParserConfigurationException | SAXException | IOException | TransformException ex) {
            LOGGER.error("", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("@practicePermisions.isAllowed(authentication.name, #practiceId)")
    @RequestMapping(value = RestCommonPaths.PRACTICE_BY_ID, method = RequestMethod.GET)
    public ResponseEntity<PracticeDto> get(@PathVariable(RestCommonPaths.USER_ID) String userId,
            @PathVariable(RestCommonPaths.PRACTICE_ID) String practiceId) {
        PracticeDto pr = practiceService.getById(practiceId);
        if (pr == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(pr, HttpStatus.OK);
    }

    @PreAuthorize("@practicePermisions.isOwner(authentication.name, #practiceId)")
    @RequestMapping(value = RestCommonPaths.PRACTICE_BY_ID, method = RequestMethod.PUT)
    public ResponseEntity<PracticeDto> update(@PathVariable(RestCommonPaths.USER_ID) String userId,
            @PathVariable(RestCommonPaths.PRACTICE_ID) String practiceId,
            @RequestBody PracticeDto practiceDto) {
        PracticeDto pr = practiceService.changePrivacy(practiceDto);
        if (pr == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(pr, HttpStatus.OK);
    }

    @PreAuthorize("@practicePermisions.isAllowed(authentication.name, #practiceId)")
    @RequestMapping(value = RestCommonPaths.PRACTICE_ROUTE_BY_ID, method = RequestMethod.GET)
    public ResponseEntity<String> getPracticeRoute(@PathVariable(RestCommonPaths.USER_ID) String userId,
            @PathVariable(RestCommonPaths.PRACTICE_ID) String practiceId) {
        try {
            RouteDto r = practiceService.getRouteById(practiceId);
            if (r == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(r.toString(), HttpStatus.OK);
        } catch (ParserConfigurationException | TransformerException ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("@practicePermisions.isAllowed(authentication.name, #practiceId)")
    @RequestMapping(value = RestCommonPaths.PRACTICE_GPX, method = RequestMethod.GET)
    public ResponseEntity<String> getPracticeGPX(@PathVariable(RestCommonPaths.USER_ID) String userId,
            @PathVariable(RestCommonPaths.PRACTICE_ID) String practiceId) {
        String r;
        try {
            r = practiceService.getPracticeGPX(practiceId);
            if (r == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(r, HttpStatus.OK);
        } catch (ParserConfigurationException | TransformerException ex) {
            LOGGER.error("", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("@practicePermisions.isAllowed(authentication.name, #practiceId)")
    @RequestMapping(value = RestCommonPaths.PRACTICE_SEGMENT_BY_ID, method = RequestMethod.GET)
    public ResponseEntity<BaseListDto<SegmentPracticeDto>> getPracticeSegment(
            @PathVariable(RestCommonPaths.USER_ID) String userId,
            @PathVariable(RestCommonPaths.PRACTICE_ID) String practiceId) {
        BaseListDto<SegmentPracticeDto> r;
        try {
            r = practiceService.getSegmentById(practiceId);
            return new ResponseEntity<>(r, HttpStatus.OK);
        } catch (ParserConfigurationException | TransformerException ex) {
            LOGGER.error("", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("@practicePermisions.isOwner(authentication.name, #practiceId)")
    @RequestMapping(value = RestCommonPaths.PRACTICE_BY_ID, method = RequestMethod.DELETE)
    public ResponseEntity remove(
            @PathVariable(RestCommonPaths.USER_ID) String userId,
            @PathVariable(RestCommonPaths.PRACTICE_ID) String practiceId) {
        try {
            practiceService.remove(practiceId);
        } catch (Exception ex) {
            LOGGER.error("", ex);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(HttpStatus.OK);

    }
}
