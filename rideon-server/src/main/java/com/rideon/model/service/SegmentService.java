/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.service;

import com.rideon.model.dto.SegmentDto;
import com.rideon.model.dto.SegmentPracticeDto;
import com.rideon.model.dto.list.BaseListDto;
import com.rideon.model.dto.list.SegmentListDto;
import com.vividsolutions.jts.geom.Geometry;
import java.util.Set;

/**
 *
 * @author Fer
 */
public interface SegmentService {

    public SegmentDto add(SegmentDto segmentDto);

    public void remove(String segmentId);

    public SegmentDto get(String segmentId);

    public Set<SegmentDto> search();

    public SegmentListDto search(Geometry geometry);

    public String getGpx(String segmentId);

    public BaseListDto<SegmentPracticeDto> getResults(String segmentId);

    public BaseListDto<String> getLast(String limit);
}
