/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.dao.impl;

import com.rideon.model.dao.RouteDao;
import com.rideon.model.domain.Route;
import com.rideon.model.domain.Segment;
import com.rideon.segmentdetector.util.GisUtils;
import com.rideon.util.Constants;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.PrecisionModel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.Query;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Fer
 */
@Repository(value = "routeDao")
public class RouteDaoImpl extends BaseDaoImpl<Route> implements RouteDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteDaoImpl.class);

    @Override
    @Transactional
    public Route add(Route object) {

        List<Route> list = search(object.getGeometry());

        for (Route r : list) {
            if (r.getGeometry().equalsExact(object.getGeometry())) {
                return r;
            }
        }
        return super.add(object); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> search(String name) {
        Query q = entityManager.createQuery("Select r.id from routes r where r.name like '%" + name + "%'");
        List res = q.getResultList();
        Iterator itr = res.iterator();
        ArrayList<Route> response = new ArrayList<>();
        while (itr.hasNext()) {
            Long id = (Long) itr.next();
            Route r = entityManager.find(Route.class, id);
            Coordinate[] coors = r.getGeometry().getCoordinates();
            GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), Constants.SRID);
            Coordinate[] subCoors = (Coordinate[]) ArrayUtils.subarray(coors, 0, 2);
            LineString ln = geometryFactory.createLineString(subCoors);
            r.setGeometry(ln);
            response.add(r);
        }
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> search(Geometry geom) {
        Set<Route> resultSearch = new HashSet<>();
        String queryString = "select r.id from routes r where ST_Covers(ST_SetSRID(ST_GeomFromEWKT('" + geom.toText() + "'), " + geom.getSRID() + "), r.geometry)";
        Query query = entityManager.createNativeQuery(queryString);
        List result = query.getResultList();
        if (!result.isEmpty()) {
            Iterator resItr = result.iterator();
            while (resItr.hasNext()) {
                Long id = Long.valueOf(resItr.next().toString());
                Route s = entityManager.find(Route.class, id);
                if (s != null) {
                    resultSearch.add(s);
                }
            }
        }
        return new ArrayList(resultSearch);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Segment> getSegments(Long valueOf) {
        Route route = entityManager.find(Route.class, valueOf);
        List<Segment> list = new ArrayList<>();
        if (route != null) {
            for (Segment s : route.getSegments()) {
                list.add(entityManager.find(Segment.class, s.getId()));
            }
        }
        return list;
    }

    @Override
    @Transactional
    public String getGPX(Long routeId) {
        Route route = entityManager.find(Route.class, routeId);
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
    public List<Long> getLast(int limit) {
        List<Long> list = new ArrayList<>();

        String q = "select r.id from routes r order by r.creationDate DESC LIMIT " + limit;
        Query query = entityManager.createNativeQuery(q);
        List resultSet = query.getResultList();

        Iterator it = resultSet.iterator();
        while (it.hasNext()) {
            Long s = Long.valueOf(it.next().toString());
            list.add(s);
        }
        return list;
    }

    @Override
    @Transactional
    public void addSegment(Route r, Segment s) {
        r = entityManager.find(Route.class, r.getId());
        if (r != null) {
            r.getSegments().add(s);
            entityManager.merge(r);
        }
    }
}
