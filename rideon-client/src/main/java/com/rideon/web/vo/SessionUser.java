/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.web.vo;

import com.rideon.model.dto.MultimediaDto;
import com.rideon.model.dto.UserDto;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import org.springframework.context.annotation.Scope;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author Fer
 */
@Scope("session")
public class SessionUser extends org.springframework.security.core.userdetails.User implements Serializable {

    private String email;
    private String fullName;
    private String country;
    private String province;
    private String town;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date bornDate;

    public SessionUser(UserDto user, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        if (user != null) {
            this.email = user.getEmail();
            this.fullName = user.getFullName();
            this.country = user.getCountry();
            this.province = user.getProvince();
            this.town = user.getTown();
            this.bornDate = user.getBornDate();
        }
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

    public UserDto toUserDto() {
        UserDto userDto = new UserDto();
        userDto.setUsername(this.getUsername());
        userDto.setFullName(fullName);
        userDto.setEmail(email);
        userDto.setBornDate(bornDate);
        userDto.setCountry(country);
        userDto.setTown(town);
        userDto.setProvince(province);
        return userDto;
    }
}
