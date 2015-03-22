/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.dto;

import java.util.Date;
import org.codehaus.jackson.map.annotate.JsonRootName;

/**
 *
 * @author Fer
 */
@JsonRootName(value = "messageDto")
public class MessageDto {

    private Long id;
    private String text;
    private UserDto owner;
    private Date writtingDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserDto getOwner() {
        return owner;
    }

    public void setOwner(UserDto owner) {
        this.owner = owner;
    }

    public Date getWrittingDate() {
        return writtingDate;
    }

    public void setWrittingDate(Date writtingDate) {
        this.writtingDate = writtingDate;
    }
}
