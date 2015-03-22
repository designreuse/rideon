/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.service;

import com.rideon.model.dto.RouteDto;
import com.rideon.model.dto.list.BaseListDto;
import com.rideon.model.dto.list.RouteListDto;
import com.rideon.model.dto.list.SegmentListDto;
import com.vividsolutions.jts.geom.Geometry;

/**
 *
 * @author Fer
 */
public interface RouteService {

    public RouteDto add(RouteDto route);

    public void remove(String routeId);

    public RouteDto get(String routeId);

    public SegmentListDto getSegments(String routeId);

    public RouteListDto search(Geometry geometry);

    public RouteListDto search(String name);

    public String getGpx(String routeId);

    public BaseListDto<String> getLast(String limit);
}
