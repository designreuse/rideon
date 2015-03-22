/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.service.impl;

import com.rideon.model.dao.FriendshipRequestDao;
import com.rideon.model.dao.UserDao;
import com.rideon.model.dto.FriendshipRequestDto;
import com.rideon.model.dto.list.BaseListDto;
import com.rideon.common.enums.NotificationStatus;
import com.rideon.common.enums.RequestStatus;
import com.rideon.model.domain.FriendshipRequest;
import com.rideon.model.service.FriendshipRequestService;
import java.util.Date;
import java.util.List;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Fer
 */
@Service(value = "friendshipRequestService")
public class FriendshipRequestServiceImpl implements FriendshipRequestService {

    @Autowired
    FriendshipRequestDao friendshipRequestDao;
    @Autowired
    UserDao userDao;
    @Autowired
    MapperFacade mapper;

    @Override
    public FriendshipRequestDto add(FriendshipRequestDto request) {
        if (request.getPetitioner().equals(request.getTarget())) {
//            throw new IllegalAccessException("Petitioner and Target can't be the same");
            return null;
        }
        try {
            request.setRequestDate(new Date());
            request.setRequestStatus(RequestStatus.NOT_ANSWERED);
            request.setNotificationStatus(NotificationStatus.UNREADED);
            FriendshipRequest res = friendshipRequestDao.add(mapper.map(request, FriendshipRequest.class));
            return mapper.map(res, FriendshipRequestDto.class);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public FriendshipRequestDto getById(String requestId) {
        FriendshipRequest res = friendshipRequestDao.getById(Long.valueOf(requestId));
        return mapper.map(res, FriendshipRequestDto.class);
    }

    @Override
    public BaseListDto<FriendshipRequestDto> getByUserId(String userId) {
        List<FriendshipRequest> reqList = friendshipRequestDao.getReceivedRequestByUserId(userId);
        BaseListDto<FriendshipRequestDto> dtoList = new BaseListDto<>();
        for (FriendshipRequest req : reqList) {
            dtoList.add(mapper.map(req, FriendshipRequestDto.class));
        }
        return dtoList;
    }

    @Override
    public void remove(String requestId) {
        friendshipRequestDao.remove(Long.valueOf(requestId));
    }

    @Override
    public FriendshipRequestDto updateStatus(String requestId, FriendshipRequestDto request) {
        //Chequeos de seguridad
        FriendshipRequest req = friendshipRequestDao.updateRequest(Long.valueOf(requestId), mapper.map(request, FriendshipRequest.class));
        return mapper.map(req, FriendshipRequestDto.class);
    }
}
