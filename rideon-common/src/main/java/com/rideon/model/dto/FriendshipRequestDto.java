/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.dto;

import com.rideon.common.enums.NotificationStatus;
import com.rideon.common.enums.RequestStatus;
import java.util.Date;
import org.codehaus.jackson.map.annotate.JsonRootName;

/**
 *
 * @author Fer
 */
@JsonRootName(value = "friendshipRequestDto")
public class FriendshipRequestDto {

    private Long id;
    private UserDto petitioner;
    private UserDto target;
    private RequestStatus requestStatus;
    private NotificationStatus notificationStatus;
    private Date requestDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDto getPetitioner() {
        return petitioner;
    }

    public void setPetitioner(UserDto petitioner) {
        this.petitioner = petitioner;
    }

    public UserDto getTarget() {
        return target;
    }

    public void setTarget(UserDto target) {
        this.target = target;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public NotificationStatus getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(NotificationStatus notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }
}
