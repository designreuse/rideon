/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.dto;

import org.codehaus.jackson.map.annotate.JsonRootName;

/**
 *
 * @author fer
 */
@JsonRootName(value = "signFormDto")
public class SignFormDto {

    private String username;
    private String email;
    private String fullName;
    private String password;
    private String confirmPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String username) {
        this.email = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String repassword) {
        this.confirmPassword = repassword;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String name) {
        this.fullName = name;
    }
}
