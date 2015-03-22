/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.controller.permisions;

import com.rideon.model.dto.UserDto;
import com.rideon.common.enums.Privacy;
import com.rideon.model.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Fer
 */
@Component(value = "groupPermisions")
public class GroupPermisions {

    @Autowired
    private GroupService groupService;

    public boolean isOwner(String userAuthenticated, String groupId) {
        return groupService.getOwnerUsername(groupId).equals(userAuthenticated) || userAuthenticated.equals("admin");
    }

    public boolean isAllowed(String userAuthenticated, String groupId) {
        if (groupService.getPrivacy(groupId).equals(Privacy.PUBLIC) || userAuthenticated.equals("admin")) {
            return true;
        }
        return isMember(userAuthenticated, groupId);
    }

    public boolean isMember(String userAuthenticated, String groupId) {

        UserDto userAuthenticatedDto = new UserDto();
        userAuthenticatedDto.setUsername(userAuthenticated);
        return groupService.getMembers(groupId).contains(userAuthenticatedDto);
    }

    public boolean isMessageOwner(String userAuthenticated, String groupId, String messageId) {
        if (isOwner(userAuthenticated, groupId) || userAuthenticated.equals("admin")) {
            return true;
        }
        return groupService.getMessage(groupId, messageId).getOwner().getUsername().equals(userAuthenticated);
    }
}
