/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.domain;

import com.rideon.common.enums.EventStatus;
import com.rideon.common.enums.EventType;
import com.rideon.common.enums.Privacy;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Fer
 */
@Entity(name = "events")
public class Event implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @Lob
    private String description;
    @ManyToOne(fetch = FetchType.EAGER)
    private User owner;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Multimedia image;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Multimedia thumbnail;
    @ManyToMany
    @JoinTable(name = "event_users",
            inverseJoinColumns = {
        @JoinColumn(name = "users_username", referencedColumnName = "username")},
            joinColumns = {
        @JoinColumn(name = "event_id", referencedColumnName = "id")})
    private Set<User> members = new HashSet<>();
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Practice> practices = new HashSet<>();
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Message> messages = new HashSet<>();
    @ManyToOne(fetch = FetchType.LAZY)
    private Route route;
    @ManyToOne(fetch = FetchType.LAZY)
    private Segment segment;
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventDate;
    private Privacy privacy;
    private EventStatus status;
    @Column(nullable = false)
    private EventType eventType;
    @ManyToOne(fetch = FetchType.EAGER)
    private Group group;
    private Boolean uploadAfterDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Multimedia getImage() {
        return image;
    }

    public void setImage(Multimedia image) {
        this.image = image;
    }

    public Multimedia getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Multimedia thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Set<User> getMembers() {
        return members;
    }

    public void setMembers(Set<User> members) {
        this.members = members;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Segment getSegment() {
        return segment;
    }

    public void setSegment(Segment segment) {
        this.segment = segment;
    }

    public Privacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public Set<Practice> getPractices() {
        return practices;
    }

    public void setPractices(Set<Practice> practices) {
        this.practices = practices;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Boolean getUploadAfterDate() {
        return uploadAfterDate;
    }

    public void setUploadAfterDate(Boolean uploadAfterDate) {
        this.uploadAfterDate = uploadAfterDate;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Event other = (Event) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
