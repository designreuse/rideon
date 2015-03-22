/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.controller.permisions;

import com.rideon.model.dto.UserDto;
import com.rideon.model.dto.list.BaseListDto;
import com.rideon.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Fer
 */
@Component(value = "userPermisions")
public class UserPermisions {

    @Autowired
    private UserService usersService;

    public boolean isAllowed(String userAuthenticated, String userRequest) {
        return userRequest.equals(userAuthenticated) || userAuthenticated.equals("admin");
    }

    public boolean isFriendOf(String userAuthenticated, String userRequest) {
        if (isAllowed(userAuthenticated, userRequest)) {
            return true;
        }
        UserDto userRequestDto = new UserDto();
        userRequestDto.setUsername(userRequest);
        BaseListDto<UserDto> friends = usersService.getFriends(userAuthenticated);
        Boolean ret = friends.contains(userRequestDto);
        return ret;
    }
}
