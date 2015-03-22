/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.dao.impl;

import com.rideon.model.dao.SegmentDao;
import com.rideon.common.enums.Privacy;
import com.rideon.model.domain.Practice;
import com.rideon.model.domain.Segment;
import com.rideon.model.domain.SegmentPractice;
import com.rideon.segmentdetector.util.GisUtils;
import com.rideon.util.Constants;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.Query;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Fer
 */
@Repository(value = "segmentDao")
public class SegmentDaoImpl extends BaseDaoImpl<Segment> implements SegmentDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(SegmentDaoImpl.class);

    @Override
    @Transactional(readOnly = true)
    public Set<Segment> search(LineString lineString) {
        Set<Segment> resultSearch = new HashSet<>();
        Geometry buffer = GisUtils.applyBufferInMeters(lineString, Constants.BUFFER_DISTANCE);
        String queryString = "select s.id from segments s where ST_Covers(ST_SetSRID(ST_GeomFromEWKT('" + buffer.toText() + "'), " + buffer.getSRID() + "), s.geometry)";
        Query query = entityManager.createNativeQuery(queryString);
        List result = query.getResultList();
        if (!result.isEmpty()) {
            Iterator resItr = result.iterator();
            while (resItr.hasNext()) {
                Long id = Long.valueOf(resItr.next().toString());
                Segment s = entityManager.find(Segment.class, id);
                if (s != null) {
                    resultSearch.add(s);
                }
            }
        }
        return resultSearch;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Segment> searchInBounds(Geometry geometry) {
        Set<Segment> resultSearch = new HashSet<>();
//        Geometry buffer = GisUtils.applyBufferInMeters(lineString, Constants.BUFFER_DISTANCE);
        String queryString = "select s.id from segments s where ST_Covers(ST_SetSRID(ST_GeomFromEWKT('" + geometry.toText() + "'), " + geometry.getSRID() + "), s.geometry)";
        Query query = entityManager.createNativeQuery(queryString);
        List result = query.getResultList();
        if (!result.isEmpty()) {
            Iterator resItr = result.iterator();
            while (resItr.hasNext()) {
                Long id = Long.valueOf(resItr.next().toString());
                Segment s = entityManager.find(Segment.class, id);
                if (s != null) {
                    resultSearch.add(s);
                }
            }
        }
        return resultSearch;
    }

    @Override
    @Transactional(readOnly = true)
    public String getGPX(Long segmentId) {
        Segment route = entityManager.find(Segment.class, segmentId);
        if (route != null) {
            try {
                return GisUtils.linestringToGPXString(route.getGeometry());
            } catch (ParserConfigurationException | TransformerException ex) {
                LOGGER.error("", ex);
                return null;
            }
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SegmentPractice> getResutls(Long segmentId) {
        List<SegmentPractice> list = new ArrayList<>();

        String q = "select sp.id.practiceId from segment_practice sp where sp.id.segmentId=" + segmentId + " order by sp.duration ASC";
        String q2 = "select pr.id from practices pr where pr.privacy=" + Privacy.PUBLIC.ordinal() + " and pr.id in (" + q + ")";
        String q3 = "select sp from segment_practice sp where sp.id.practiceId in (" + q2 + ") and sp.id.segmentId = " + segmentId;
        Query query = entityManager.createQuery(q3);
        List resultSet = query.getResultList();

        Iterator it = resultSet.iterator();
        while (it.hasNext()) {
            SegmentPractice sp = (SegmentPractice) it.next();
            list.add(sp);
        }
        return list;
    }

    @Override
    public List<Long> getLast(int limit) {
        List<Long> list = new ArrayList<>();

        String q = "select s.id from segments s order by s.creationDate DESC LIMIT " + limit;
        Query query = entityManager.createNativeQuery(q);
        List resultSet = query.getResultList();

        Iterator it = resultSet.iterator();
        while (it.hasNext()) {
            Long s = Long.valueOf(it.next().toString());
            list.add(s);
        }
        return list;

    }
}
