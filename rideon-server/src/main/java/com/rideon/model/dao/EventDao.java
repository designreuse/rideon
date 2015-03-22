/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.dao;

import com.rideon.common.enums.Privacy;
import com.rideon.model.domain.Event;
import com.rideon.model.domain.Message;
import com.rideon.model.domain.Multimedia;
import com.rideon.model.domain.Practice;
import com.rideon.model.domain.Route;
import com.rideon.model.domain.Segment;
import com.rideon.model.domain.SegmentPractice;
import com.rideon.model.domain.User;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Fer
 */
public interface EventDao extends BaseDao<Event> {

    public Practice addPractice(Long eventId, Practice practice);

    public Route setRoute(Long route, Long eventId);

    public Segment setSegment(Long segment, Long eventId);

    public Object getRoute(Long eventId);

    public Multimedia setImage(Long eventId, Multimedia image);

    public Multimedia getImage(Long eventId);

    public Multimedia getThumbnail(Long eventId);

    public Multimedia setThumbnail(Long eventId, Multimedia thumbnail);

    public List<User> addMembers(Long eventId, List<User> members);

    public void removeMember(Long eventId, String memberId);

    public List<User> getMembers(Long eventId);

    public Message addMessage(Long eventId, Message message);

    public Message getMessage(Long eventId, Long messageId);

    public List<Message> getMessages(Long eventId);

    public void removeMessage(Long eventId, Long messageId);

    public List<Practice> getRouteResults(Long eventId);

    public List<SegmentPractice> getSegmentResults(Long valueOf);

    public List<Event> search(String[] querys);

    public List<Event> search(Date f, Date t);

    public String getOwnerUsername(Long valueOf);

    public Privacy getPrivacy(Long valueOf);

    public void removePractice(Long practiceId);
}
