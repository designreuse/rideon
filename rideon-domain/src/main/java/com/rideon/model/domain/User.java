/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.domain;

import com.rideon.common.enums.Privacy;
import com.rideon.common.enums.UserRole;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Fer
 */
@Entity(name = "users")
public class User implements Serializable {

    @Id
    private String username;
    private String email;
    private String password;
    private String fullName;
    private String country;
    private String province;
    private String town;
    private Privacy profilePrivacy;
    private UserRole userRole;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date bornDate;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Bicycle> bicycles;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "owner")
    private Set<Practice> practices;
    @ManyToMany(mappedBy = "members", fetch = FetchType.LAZY)
    private Set<Group> groups;
    @ManyToMany(mappedBy = "members", fetch = FetchType.LAZY)
    private Set<Event> events;
    @ManyToMany
    @JoinTable(name = "friends",
            joinColumns =
            @JoinColumn(name = "userId"),
            inverseJoinColumns =
            @JoinColumn(name = "friendId"))
    private Set<User> friends;
    @ManyToMany
    @JoinTable(name = "friends",
            joinColumns =
            @JoinColumn(name = "friendId"),
            inverseJoinColumns =
            @JoinColumn(name = "userId"))
    private Set<User> friendOf;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Multimedia thumbnail;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Multimedia image;

    public String getUsername() {
        return username;
    }

    public void setUsername(String nickName) {
        this.username = nickName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Bicycle> getBicycles() {
        return bicycles;
    }

    public void setBicycles(Set<Bicycle> bicycles) {
        this.bicycles = bicycles;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public Date getBornDate() {
        return bornDate;
    }

    public void setBornDate(Date bornDate) {
        this.bornDate = bornDate;
    }

    public Set<User> getFriends() {
        return friends;
    }

    public void setFriends(Set<User> friends) {
        this.friends = friends;
    }

    public Set<User> getFriendOf() {
        return friendOf;
    }

    public void setFriendOf(Set<User> friendOf) {
        this.friendOf = friendOf;
    }

    public Multimedia getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Multimedia thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Multimedia getImage() {
        return image;
    }

    public void setImage(Multimedia image) {
        this.image = image;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public Set<Practice> getPractices() {
        return practices;
    }

    public void setPractices(Set<Practice> practices) {
        this.practices = practices;
    }

    public Privacy getProfilePrivacy() {
        return profilePrivacy;
    }

    public void setProfilePrivacy(Privacy profilePrivacy) {
        this.profilePrivacy = profilePrivacy;
    }

    public Boolean isAdmin() {
        return userRole == UserRole.ADMIN;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            return this.username.equals(((User) obj).getUsername());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.username);
        return hash;
    }
}
