/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.service;

import com.rideon.model.dto.FriendshipRequestDto;
import com.rideon.model.dto.list.BaseListDto;

/**
 *
 * @author Fer
 */
public interface FriendshipRequestService {

    public FriendshipRequestDto add(FriendshipRequestDto request);

    public FriendshipRequestDto getById(String requestId);

    public BaseListDto<FriendshipRequestDto> getByUserId(String userId);

    public void remove(String requestId);

    public FriendshipRequestDto updateStatus(String requestId, FriendshipRequestDto request);

}
