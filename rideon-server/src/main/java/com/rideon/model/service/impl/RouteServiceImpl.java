/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.service.impl;

import com.rideon.model.dao.RouteDao;
import com.rideon.model.dto.RouteDto;
import com.rideon.model.dto.SegmentDto;
import com.rideon.model.dto.list.BaseListDto;
import com.rideon.model.dto.list.RouteListDto;
import com.rideon.model.dto.list.SegmentListDto;
import com.rideon.model.domain.Route;
import com.rideon.model.domain.Segment;
import com.rideon.model.service.RouteService;
import com.vividsolutions.jts.geom.Geometry;
import java.util.List;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Fer
 */
@Service(value = "routeService")
public class RouteServiceImpl implements RouteService {

    @Autowired
    MapperFacade mapper;
    @Autowired
    RouteDao routeDao;

    @Override
    public RouteDto add(RouteDto route) {
        Route r = (Route) mapper.map(route, Route.class);
        routeDao.add(r);
        return mapper.map(r, RouteDto.class);
    }

    @Override
    public void remove(String routeId) {
        routeDao.remove(Long.valueOf(routeId));
    }

    @Override
    public RouteDto get(String segmentId) {
        Route route = routeDao.getById(Long.valueOf(segmentId));
        if (route != null) {
            return mapper.map(route, RouteDto.class);
        }
        return null;
    }

    @Override
    public RouteListDto search(Geometry geometry) {
        List<Route> routes = routeDao.search(geometry);
        RouteListDto dto = new RouteListDto();
        for (Route r : routes) {
            dto.add(mapper.map(r, RouteDto.class));
        }
        return dto;
    }

    @Override
    public RouteListDto search(String name) {
        List<Route> routes = routeDao.search(name);
        RouteListDto dto = new RouteListDto();
        for (Route r : routes) {
            dto.add(mapper.map(r, RouteDto.class));
        }
        return dto;
    }

    @Override
    public SegmentListDto getSegments(String routeId) {
        List<Segment> segmens = routeDao.getSegments(Long.valueOf(routeId));
        SegmentListDto listDto = new SegmentListDto();
        for (Segment s : segmens) {
            listDto.add(mapper.map(s, SegmentDto.class));
        }
        return listDto;
    }

    @Override
    public String getGpx(String routeId) {
        return routeDao.getGPX(Long.valueOf(routeId));
    }

    @Override
    public BaseListDto<String> getLast(String limit) {
        List<Long> list = routeDao.getLast(Integer.valueOf(limit));
        BaseListDto<String> listDto = new BaseListDto<>();
        for (Long l : list) {
            listDto.add(l.toString());
        }
        return listDto;

    }
}
