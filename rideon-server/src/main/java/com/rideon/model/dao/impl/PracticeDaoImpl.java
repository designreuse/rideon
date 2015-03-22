/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.dao.impl;

import com.rideon.model.dao.PracticeDao;
import com.rideon.common.enums.Privacy;
import com.rideon.model.domain.Practice;
import com.rideon.model.domain.Route;
import com.rideon.model.domain.SegmentPractice;
import com.rideon.model.domain.User;
import com.rideon.segmentdetector.util.GisUtils;
import com.vividsolutions.jts.geom.LineString;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.Query;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Fer
 */
@Repository(value = "practiceDao")
public class PracticeDaoImpl extends BaseDaoImpl<Practice> implements PracticeDao {

    @Override
    @Transactional
    public Practice add(Practice object) {
        Route r = entityManager.find(Route.class, object.getRoute().getId());
        object.setRoute(r);
        return super.add(object); //To change body of generated methods, choose Tools | Templates.
    }

    
    @Override
    @Transactional(readOnly = true)
    public List<Practice> searchByDate(Date from, Date to, String userId) {
        String query = "select p.id from practices p where p.owner = '" + userId + "' and p.practiceDate >= '" + from + "' and p.practiceDate <= '" + to + "'";
        Query q = entityManager.createQuery(query);
        List resultList = q.getResultList();
        Iterator itr = resultList.iterator();
        List<Practice> practices = new ArrayList<>();
        while (itr.hasNext()) {
            Long id = (Long) itr.next();
            practices.add(entityManager.find(Practice.class, id));
        }
        return practices;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Practice> searchByDate(Date date, String userId) {
        String query = "select p.id from practices p where p.owner = '" + userId + "' and p.practiceDate = '" + date + "'";
        Query q = entityManager.createQuery(query);
        List resultList = q.getResultList();
        Iterator itr = resultList.iterator();
        List<Practice> practices = new ArrayList<>();
        while (itr.hasNext()) {
            Long id = (Long) itr.next();
            practices.add(entityManager.find(Practice.class, id));
        }
        return practices;
    }

    @Override
    @Transactional
    public Route getRoute(Long id) {
        Practice pr = entityManager.find(Practice.class, id);
        Route r = entityManager.find(Route.class, pr.getRoute().getId());
        return r;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<SegmentPractice> getSegment(Long id) {
        Practice pr = entityManager.find(Practice.class, id);
        Set<SegmentPractice> set = new HashSet<>();
        for (SegmentPractice sp : pr.getSegmentsResults()) {
            set.add(entityManager.find(SegmentPractice.class, sp.getId()));
        }
        return set;
    }

    @Override
    @Transactional(readOnly = true)
    public String getGPX(Long id) throws ParserConfigurationException, TransformerException {
        Practice pr = entityManager.find(Practice.class, id);
        if (pr != null) {
            LineString ln = pr.getRoute().getGeometry();
            Date[] times = pr.getTimes();
            return GisUtils.linestringToGPXString(ln, times);
        }
        return null;
    }

    @Override
    @Transactional
    public User getOwner(Long practice) {
        Practice pr = entityManager.find(Practice.class, practice);
        if (pr != null) {
            return entityManager.find(User.class, pr.getOwner().getUsername());
        }
        return null;
    }

    @Override
    @Transactional
    public Practice update(Practice object) {
        Practice pr = entityManager.find(Practice.class, object.getId());
        if (pr != null && object.getPrivacy() != null) {
            pr.setPrivacy(object.getPrivacy());
            entityManager.merge(pr);
            return pr;
        }
        return object;
    }

    @Override
    public String getOwnerUsername(Long valueOf) {
        Query q = entityManager.createQuery("Select p.owner.username From practices p Where p.id =" + valueOf);
        return (String) q.getSingleResult();
    }

    @Override
    public Privacy getPrivacy(Long valueOf) {
        Query q = entityManager.createQuery("Select p.privacy From practices p Where p.id =" + valueOf);
        return (Privacy) q.getSingleResult();
    }
}
