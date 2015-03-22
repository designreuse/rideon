/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.dao;

import com.rideon.common.enums.Privacy;
import com.rideon.model.domain.Practice;
import com.rideon.model.domain.Route;
import com.rideon.model.domain.SegmentPractice;
import com.rideon.model.domain.User;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author Fer
 */
public interface PracticeDao extends BaseDao<Practice> {

    public List<Practice> searchByDate(Date from, Date to, String userId);

    public List<Practice> searchByDate(Date date, String userId);

    public Route getRoute(Long id);

    public Set<SegmentPractice> getSegment(Long id);

    public String getGPX(Long valueOf) throws ParserConfigurationException, TransformerException;

    public User getOwner(Long practice);

    public String getOwnerUsername(Long valueOf);

    public Privacy getPrivacy(Long valueOf);
}
