/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.dao;

import com.rideon.model.domain.FriendshipRequest;
import com.rideon.common.enums.RequestStatus;
import java.util.List;

/**
 *
 * @author Fer
 */
public interface FriendshipRequestDao extends BaseDao<FriendshipRequest> {

    public List<FriendshipRequest> getReceivedRequestByUserId(String userId);

    public List<FriendshipRequest> getSentRequestByUserId(String userId);

    public FriendshipRequest updateRequest(Long requestId, FriendshipRequest status);
}
