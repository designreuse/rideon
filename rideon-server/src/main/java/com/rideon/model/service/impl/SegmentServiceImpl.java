/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.service.impl;

import com.rideon.model.dao.PracticeDao;
import com.rideon.model.dao.SegmentDao;
import com.rideon.model.dao.UserDao;
import com.rideon.model.dto.SegmentDto;
import com.rideon.model.dto.SegmentPracticeDto;
import com.rideon.model.dto.UserDto;
import com.rideon.model.dto.list.BaseListDto;
import com.rideon.model.dto.list.SegmentListDto;
import com.rideon.model.domain.Segment;
import com.rideon.model.domain.SegmentPractice;
import com.rideon.model.service.SegmentService;
import com.vividsolutions.jts.geom.Geometry;
import java.util.List;
import java.util.Set;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Fer
 */
@Service(value = "segmentService")
public class SegmentServiceImpl implements SegmentService {

    @Autowired
    MapperFacade mapper;
    @Autowired
    SegmentDao segmentDao;
    @Autowired
    UserDao userDao;
    @Autowired
    PracticeDao practiceDao;

    @Override
    public SegmentDto add(SegmentDto segmentDto) {
        Segment s = (Segment) mapper.map(segmentDto, Segment.class);
        segmentDao.add(s);
        return mapper.map(s, SegmentDto.class);
    }

    @Override
    public void remove(String segmentId) {
        segmentDao.remove(Long.valueOf(segmentId));
    }

    @Override
    public SegmentDto get(String segmentId) {
        Segment s = segmentDao.getById(Long.valueOf(segmentId));
        if (s != null) {
            return mapper.map(s, SegmentDto.class);
        }
        return null;
    }

    @Override
    public Set<SegmentDto> search() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SegmentListDto search(Geometry geometry) {
        Set<Segment> routes = segmentDao.searchInBounds(geometry);
        SegmentListDto dto = new SegmentListDto();
        for (Segment r : routes) {
            dto.add(mapper.map(r, SegmentDto.class));
        }
        return dto;
    }

   

    @Override
    public String getGpx(String segmentId) {
        return segmentDao.getGPX(Long.valueOf(segmentId));
    }

    @Override
    public BaseListDto<SegmentPracticeDto> getResults(String segmentId) {
        List<SegmentPractice> results = segmentDao.getResutls(Long.valueOf(segmentId));
        BaseListDto<SegmentPracticeDto> listDto = new BaseListDto<>();

        for (SegmentPractice sp : results) {
            SegmentPracticeDto s = mapper.map(sp, SegmentPracticeDto.class);
            UserDto owner = mapper.map(practiceDao.getOwner(s.getId().getPracticeId()), UserDto.class);
            s.setOwner(owner);
            listDto.add(s);
        }
        return listDto;
    }

    @Override
    public BaseListDto<String> getLast(String limit) {
        List<Long> list = segmentDao.getLast(Integer.valueOf(limit));
        BaseListDto<String> listDto = new BaseListDto<>();
        for (Long l : list) {
            listDto.add(l.toString());
        }
        return listDto;
    }
}