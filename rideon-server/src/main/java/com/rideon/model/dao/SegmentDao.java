/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.dao;

import com.rideon.model.domain.Segment;
import com.rideon.model.domain.SegmentPractice;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Fer
 */
public interface SegmentDao extends BaseDao<Segment> {

    public Set<Segment> search(LineString lineString);

    public Set<Segment> searchInBounds(Geometry geometry);

    public String getGPX(Long segmentId);

    public List<SegmentPractice> getResutls(Long valueOf);

    public List<Long> getLast(int limit);
}
