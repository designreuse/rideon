/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.controller;

import com.rideon.model.dto.SegmentDto;
import com.rideon.model.dto.SegmentPracticeDto;
import com.rideon.model.dto.list.BaseListDto;
import com.rideon.model.service.SegmentService;
import com.rideon.common.util.RestCommonPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Fer
 */
@Controller
public class SegmentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SegmentController.class);
    @Autowired
    SegmentService segmentService;

    @RequestMapping(value = RestCommonPaths.SEGMENT_BY_ID, method = RequestMethod.GET)
    @ResponseBody
    public String get(@PathVariable(RestCommonPaths.SEGMENT_ID) String segmentId) {
        SegmentDto segment = segmentService.get(segmentId);
        return segment.toString();
    }

    @RequestMapping(value = RestCommonPaths.SEGMENT_RESULTS, method = RequestMethod.GET)
    @ResponseBody
    public BaseListDto<SegmentPracticeDto> getResults(@PathVariable(RestCommonPaths.SEGMENT_ID) String segmentId) {
        BaseListDto<SegmentPracticeDto> segments = segmentService.getResults(segmentId);
        return segments;
    }

    @RequestMapping(value = RestCommonPaths.SEGMENT_GPX, method = RequestMethod.GET)
    @ResponseBody
    public String getGpx(@PathVariable(RestCommonPaths.SEGMENT_ID) String segmentId) {
        return segmentService.getGpx(segmentId);
    }

    @RequestMapping(value = RestCommonPaths.SEGMENT_LAST, method = RequestMethod.GET)
    @ResponseBody
    public BaseListDto<String> getLast() {
        BaseListDto<String> list = segmentService.getLast("5");
        return list;
    }
}
