/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.domain;

import com.rideon.common.enums.NotificationStatus;
import com.rideon.common.enums.RequestStatus;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author Fer
 */
@Entity(name = "friendship_request")
public class FriendshipRequest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne(fetch = FetchType.EAGER)
    private User petitioner;
    @OneToOne(fetch = FetchType.EAGER)
    private User target;
    private RequestStatus requestStatus = RequestStatus.NOT_ANSWERED;
    private NotificationStatus notificationStatus = NotificationStatus.UNREADED;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date requestDate;
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getPetitioner() {
        return petitioner;
    }

    public void setPetitioner(User petitioner) {
        this.petitioner = petitioner;
    }

    public User getTarget() {
        return target;
    }

    public void setTarget(User target) {
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FriendshipRequest) {
            FriendshipRequest req = ((FriendshipRequest) obj);
            if (this.id.equals(req.getId())
                    && this.target.getUsername().equals(req.getTarget().getUsername())
                    && this.petitioner.getUsername().equals(req.getPetitioner().getUsername())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.id);
        return hash;
    }
}
