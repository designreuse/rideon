/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.dao;

import com.rideon.model.domain.Route;
import com.rideon.model.domain.Segment;
import com.vividsolutions.jts.geom.Geometry;
import java.util.List;

/**
 *
 * @author Fer
 */
public interface RouteDao extends BaseDao<Route> {

    public List<Route> search(String name);

    public List<Route> search(Geometry geom);

    public List<Segment> getSegments(Long valueOf);

    public String getGPX(Long routeId);
    
    public List<Long> getLast(int limit);

    public void addSegment(Route r, Segment s);
}
