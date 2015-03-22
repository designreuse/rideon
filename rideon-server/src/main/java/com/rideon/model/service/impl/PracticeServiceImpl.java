/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.service.impl;

import com.rideon.model.dao.PracticeDao;
import com.rideon.model.dao.RouteDao;
import com.rideon.model.dao.SegmentDao;
import com.rideon.model.dao.SegmentPracticeDao;
import com.rideon.model.dao.UserDao;
import com.rideon.model.dto.PracticeDto;
import com.rideon.model.dto.RouteDto;
import com.rideon.model.dto.SegmentPracticeDto;
import com.rideon.model.dto.list.BaseListDto;
import com.rideon.common.enums.Privacy;
import com.rideon.model.dao.EventDao;
import com.rideon.model.domain.Bicycle;
import com.rideon.model.domain.Practice;
import com.rideon.model.domain.Route;
import com.rideon.model.domain.Segment;
import com.rideon.model.domain.SegmentPractice;
import com.rideon.model.domain.User;
import com.rideon.segmentdetector.SegmentDetector;
import com.rideon.segmentdetector.util.GisUtils;
import com.rideon.model.service.PracticeService;
import com.vividsolutions.jts.geom.LineString;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import ma.glasnost.orika.MapperFacade;
import org.alternativevision.gpx.GPXParser;
import org.alternativevision.gpx.beans.GPX;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

/**
 *
 * @author Fer
 */
@Service(value = "practiceService")
public class PracticeServiceImpl implements PracticeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PracticeServiceImpl.class);
    @Autowired
    MapperFacade mapper;
    @Autowired
    PracticeDao practiceDao;
    @Autowired
    UserDao userDao;
    @Autowired
    SegmentDao segmentDao;
    @Autowired
    RouteDao routeDao;
    @Autowired
    EventDao eventDao;
    @Autowired
    SegmentPracticeDao segmentPracticeDao;

    @Override
    public PracticeDto add(String gpx, String userId) throws ParserConfigurationException, IOException, SAXException {
        InputStream is = new ByteArrayInputStream(gpx.getBytes());
        GPXParser p = new GPXParser();
        GPX gp = p.parseGPX(is);
        return add(gp, userId);
    }

    @Override
    public PracticeDto add(GPX gpxDto, String userId) throws ParserConfigurationException {
        PracticeDto practiceDto = null;
        Practice practice;
        User owner = userDao.getById(userId);
        if (owner != null) {
            try {
                practice = GisUtils.gpxToPractice(gpxDto);
                List<Practice> prs = practiceDao.searchByDate(practice.getTimes()[0], userId);
                if (prs.isEmpty()) {
                    practice.setOwner(owner);
                    Route rt = routeDao.add(practice.getRoute());
                    practice.setRoute(rt);
                    practiceDao.add(practice);
                    Map<Segment, SegmentPractice> map = SegmentDetector.detectSegments(practice);
                    for (Segment segment : map.keySet()) {

                        Segment s = searchForSegment(segment.getGeometry());
                        SegmentPractice sp = map.get(segment);
                        if (s == null) {
                            s = segmentDao.add(segment);
                        }

                        //Añado el segmento a la lista de segmento de la ruta 
                        Route r = practice.getRoute();
                        routeDao.addSegment(r, s);
                        //Añado la practica del segmento a la lista de practicas del segmento
                        sp.setPractice(practice);
                        sp.setSegment(s);
                        segmentPracticeDao.add(sp);
                        practice.getSegmentsResults().add(sp);
                    }
                    practiceDao.update(practice);

                    Bicycle principal = userDao.getPrincipalBycicle(userId);
                    if (principal != null) {
                        principal.setKilometers(principal.getKilometers() + practice.getRoute().getDistance());
                        userDao.updateBicycle(userId, principal);
                    }
                    practiceDto = mapper.map(practice, PracticeDto.class);
                } else {
                    return mapper.map(prs.get(0), PracticeDto.class);
                }
            } catch (ParserConfigurationException | SAXException | IOException | TransformException ex) {
                LOGGER.error("", ex);
                throw new ParserConfigurationException("Error parsing gpx file");
            }
            return practiceDto;
        } else {
            throw new IllegalArgumentException("User " + userId + " not found.");
        }
    }

    private Segment searchForSegment(LineString geom) {
        Set<Segment> set = segmentDao.search(geom);
        for (Segment seg : set) {
            if (GisUtils.compareWith(geom, seg.getGeometry())) {
                return seg;
            }
        }
        return null;
    }

    @Override
    public void remove(String id) {
        eventDao.removePractice(Long.valueOf(id));
        practiceDao.remove(Long.valueOf(id));
    }

    @Override
    public PracticeDto getById(String id) {
        Practice p = practiceDao.getById(Long.valueOf(id));
        if (p != null) {
            return mapper.map(p, PracticeDto.class);
        }
        return null;
    }

    @Override
    public BaseListDto<PracticeDto> getByUser(String userId) {
        Set<Practice> practices = userDao.getPractices(userId);
        BaseListDto<PracticeDto> practicesDto = new BaseListDto<>();
        for (Practice pr : practices) {
            practicesDto.add(mapper.map(pr, PracticeDto.class));
        }
        return practicesDto;
    }

    @Override
    public BaseListDto<PracticeDto> search(Date from, Date to, String userId) {
        List<Practice> list = practiceDao.searchByDate(from, to, userId);
        BaseListDto<PracticeDto> listDto = new BaseListDto<>();
        for (Practice pr : list) {
            listDto.add(mapper.map(pr, PracticeDto.class));
        }
        return listDto;
    }

    @Override
    public BaseListDto<PracticeDto> search(Date date, String userId) {
        List<Practice> list = practiceDao.searchByDate(date, userId);
        BaseListDto<PracticeDto> listDto = new BaseListDto<>();
        for (Practice pr : list) {
            listDto.add(mapper.map(pr, PracticeDto.class));
        }
        return listDto;
    }

    @Override
    public RouteDto getRouteById(String practiceId) throws ParserConfigurationException, TransformerException {
        Route r = practiceDao.getRoute(Long.valueOf(practiceId));
        if (r != null) {
            return mapper.map(r, RouteDto.class);
        }
        return null;
    }

    @Override
    public BaseListDto<SegmentPracticeDto> getSegmentById(String practiceId) throws ParserConfigurationException, TransformerException {
        Set<SegmentPractice> r = practiceDao.getSegment(Long.valueOf(practiceId));
        BaseListDto list = new BaseListDto();
        if (!r.isEmpty()) {
            for (SegmentPractice sp : r) {
                list.add(mapper.map(sp, SegmentPracticeDto.class));
            }
        }
        return list;
    }

    @Override
    public String getPracticeGPX(String practiceId) throws ParserConfigurationException, TransformerException {
        return practiceDao.getGPX(Long.valueOf(practiceId));
    }

    @Override
    public PracticeDto changePrivacy(PracticeDto practiceDto) {
        Practice pr = mapper.map(practiceDto, Practice.class);
        pr = practiceDao.update(pr);
        return mapper.map(pr, PracticeDto.class);
    }

    @Override
    public String getOwnerUsername(String groupId) {
        return practiceDao.getOwnerUsername(Long.valueOf(groupId));
    }

    @Override
    public Privacy getPrivacy(String groupId) {
        return practiceDao.getPrivacy(Long.valueOf(groupId));
    }
}
