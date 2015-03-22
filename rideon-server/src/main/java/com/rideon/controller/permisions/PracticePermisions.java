/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.controller.permisions;

import com.rideon.model.dto.UserDto;
import com.rideon.model.service.PracticeService;
import com.rideon.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Fer
 */
@Component(value = "practicePermisions")
public class PracticePermisions {

    @Autowired
    private PracticeService practiceService;
    @Autowired
    private UserService userService;

    public boolean isOwner(String userAuthenticated, String practiceId) {
        return practiceService.getOwnerUsername(practiceId).equals(userAuthenticated) || userAuthenticated.equals("admin");
    }

    public boolean isAllowed(String userAuthenticated, String practiceId) {
        if (isOwner(userAuthenticated, practiceId)) {
            return true;
        }
        String owner = practiceService.getOwnerUsername(practiceId);
        UserDto auth = new UserDto();
        auth.setUsername(userAuthenticated);
        return userService.getFriends(owner).contains(auth);
    }
}
