/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.web.vo;

import com.rideon.model.dto.UserDto;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Fer
 */
public class UserForm {

    private String username;
    private String email;
    private String fullName;
    private String country;
    private String province;
    private String town;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private String bornDate;
    private MultipartFile image;
    private String password;
    private String repassword;

    public UserForm() {
    }

    public UserForm(UserDto dto) {
        this.username = dto.getUsername();
        this.email = dto.getEmail();
        this.fullName = dto.getFullName();
        this.country = dto.getCountry();
        this.province = dto.getProvince();
        this.town = dto.getTown();
        if (dto.getBornDate() != null) {
            SimpleDateFormat parserSDF = new SimpleDateFormat("yyyy-MM-dd");
            String s = parserSDF.format(dto.getBornDate());
            this.bornDate = s;
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getBornDate() {
        return bornDate;
    }

    public void setBornDate(String bornDate) {
        this.bornDate = bornDate;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepassword() {
        return repassword;
    }

    public void setRepassword(String repassword) {
        this.repassword = repassword;
    }

    public UserDto toUserDto() throws ParseException {
        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setFullName(fullName);
        userDto.setEmail(email);
        userDto.setCountry(country);
        userDto.setTown(town);
        userDto.setProvince(province);
        if (bornDate != null && !bornDate.isEmpty()) {
            SimpleDateFormat parserSDF = new SimpleDateFormat("yyyy-MM-dd");
            Date d = parserSDF.parse(bornDate);
            userDto.setBornDate(d);
        }
        return userDto;
    }
}
