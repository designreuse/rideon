/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.dto;

import com.rideon.common.enums.UserRole;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Fer
 */
@JsonRootName(value = "userDto")
public class UserDto implements Serializable {

    private String username;
    private String email;
    private String fullName;
    private String country;
    private String province;
    private String town;
    private UserRole userRole;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date bornDate;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.username);
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
        final UserDto other = (UserDto) obj;
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        return true;
    }
}
