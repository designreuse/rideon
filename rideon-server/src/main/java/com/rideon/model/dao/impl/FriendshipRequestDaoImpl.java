/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.dao.impl;

import com.rideon.model.dao.FriendshipRequestDao;
import com.rideon.model.dao.UserDao;
import com.rideon.model.domain.FriendshipRequest;
import com.rideon.model.domain.User;
import com.rideon.common.enums.NotificationStatus;
import com.rideon.common.enums.RequestStatus;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Fer
 */
@Repository(value = "friendshipRequestDao")
public class FriendshipRequestDaoImpl extends BaseDaoImpl<FriendshipRequest> implements FriendshipRequestDao {

    @Autowired
    UserDao userDao;

    @Override
    @Transactional(readOnly = true)
    public FriendshipRequest getById(Object objectId) {
        FriendshipRequest req = entityManager.find(FriendshipRequest.class, objectId);
        if (req != null) {
            req.setPetitioner(userDao.getById(req.getPetitioner().getUsername()));
            req.setTarget(userDao.getById(req.getTarget().getUsername()));
        }
        return req;
    }

    @Override
    @Transactional
    public FriendshipRequest add(FriendshipRequest object) {
        String qs = "Select r.id From friendship_request r "
                + "Where r.target.username = '" + object.getTarget().getUsername() + "' "
                + "and r.petitioner.username = '" + object.getPetitioner().getUsername() + "' ";
        Query q = entityManager.createQuery(qs);
        List res = q.getResultList();
        //If there is any request for this users
        if (res.isEmpty()) {
            object.setRequestDate(new Date());
            object.setRequestStatus(RequestStatus.NOT_ANSWERED);
            object.setNotificationStatus(NotificationStatus.UNREADED);
            User petitioner = userDao.getById(object.getPetitioner().getUsername());
            User target = userDao.getById(object.getTarget().getUsername());
            if (petitioner != null && target != null) {
                object.setPetitioner(petitioner);
                object.setTarget(target);
                entityManager.persist(object);
                return object;
            } else {
                throw new IllegalArgumentException("Petitioner or target does not exists");
            }
        } else {
            Long reqId = (Long) res.get(0);
            FriendshipRequest req = getById(reqId);
            User pet = req.getPetitioner();
            User targ = req.getTarget();
            //If there is some request and it's status is accepted, will check if they are 
            //still friends
            //If don't, activate again the petition
            if ((req.getRequestStatus() == RequestStatus.ACCEPTED && !userDao.getFriends(pet.getUsername()).contains(targ))
                    || (req.getRequestStatus() != RequestStatus.ACCEPTED)) {
                req.setRequestStatus(RequestStatus.NOT_ANSWERED);
                req.setNotificationStatus(NotificationStatus.UNREADED);
                req.setRequestDate(new Date());
                entityManager.merge(targ);
            }
            //Otherwise discard the request
            return req;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<FriendshipRequest> getReceivedRequestByUserId(String userId) {
        Query q = entityManager.createQuery("Select r.id From friendship_request r Where r.target.username = '" + userId + "'");
        List res = q.getResultList();
        Iterator it = res.iterator();
        Set<FriendshipRequest> set = new HashSet<>();
        while (it.hasNext()) {
            Long reqId = (Long) it.next();
            FriendshipRequest req = getById(reqId);
            if (req.getRequestStatus() == RequestStatus.NOT_ANSWERED) {
                set.add(req);
            }
        }
        return new ArrayList<>(set);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FriendshipRequest> getSentRequestByUserId(String userId) {
        Query q = entityManager.createQuery("Select request From friendship_request Where petitioner.username = '" + userId + "'");
        List res = q.getResultList();
        Iterator it = res.iterator();
        Set<FriendshipRequest> set = new HashSet<>();
        while (it.hasNext()) {
            FriendshipRequest req = (FriendshipRequest) it.next();
            set.add(getById(req));
        }
        return new ArrayList<>(set);
    }

    @Override
    @Transactional
    public FriendshipRequest updateRequest(Long reqeuestId, FriendshipRequest request) {
        FriendshipRequest req = getById(reqeuestId);
        if (req != null) {
            if (request.getRequestStatus() != null) {
                req.setRequestStatus(request.getRequestStatus());
            }
            if (request.getNotificationStatus() != null) {
                req.setNotificationStatus(request.getNotificationStatus());
            }
            if (request.getRequestStatus() != null && request.getRequestStatus().equals(RequestStatus.ACCEPTED)) {
                userDao.addFriend(req.getPetitioner().getUsername(), req.getTarget().getUsername());
                req.setNotificationStatus(NotificationStatus.UNREADED);
            }
            entityManager.merge(req);
        }
        return req;
    }
}
