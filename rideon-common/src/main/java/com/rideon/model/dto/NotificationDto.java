/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.dto;

import org.codehaus.jackson.map.annotate.JsonRootName;

/**
 *
 * @author Fer
 */
@JsonRootName(value = "notificationDto")
public class NotificationDto {

    private UserDto target;
    private FriendshipRequestDto request;

    public UserDto getTarget() {
        return target;
    }

    public void setTarget(UserDto target) {
        this.target = target;
    }

    public FriendshipRequestDto getRequest() {
        return request;
    }

    public void setRequest(FriendshipRequestDto request) {
        this.request = request;
    }
}
