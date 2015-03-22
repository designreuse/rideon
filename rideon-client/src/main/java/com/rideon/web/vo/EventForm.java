/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.web.vo;

import com.rideon.model.dto.EventDto;
import com.rideon.model.dto.UserDto;
import com.rideon.common.enums.EventStatus;
import com.rideon.common.enums.EventType;
import com.rideon.common.enums.Privacy;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Fer
 */
public class EventForm {

    private Long id;
    private String name;
    private String description;
    private UserDto owner;
    private Privacy privacy;
    private String eventDate;
    private EventType eventType;
    private EventStatus eventStatus;
    private Boolean uploadAfterDate;
    private MultipartFile image;

    public EventForm() {
    }

    public EventForm(EventDto eventDto) {
        this.id = eventDto.getId();
        this.name = eventDto.getName();
        this.description = eventDto.getDescription();
        this.owner = eventDto.getOwner();
        this.privacy = eventDto.getPrivacy();
        if (eventDto.getEventDate() != null) {
            SimpleDateFormat parserSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String s = parserSDF.format(eventDto.getEventDate());
            this.eventDate = s;
        }
        this.eventType = eventDto.getEventType();
        this.eventStatus = eventDto.getStatus();
        this.uploadAfterDate = eventDto.getUploadAfterDate();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDto getOwner() {
        return owner;
    }

    public void setOwner(UserDto owner) {
        this.owner = owner;
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

    public Privacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public EventStatus getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(EventStatus eventStatus) {
        this.eventStatus = eventStatus;
    }

    public Boolean getUploadAfterDate() {
        return uploadAfterDate;
    }

    public void setUploadAfterDate(Boolean uploadAfterDate) {
        this.uploadAfterDate = uploadAfterDate;
    }

    public EventDto toEventDto() throws ParseException {
        EventDto dto = new EventDto();

        dto.setId(id);
        dto.setName(name);
        dto.setDescription(description);
        dto.setOwner(owner);
        dto.setPrivacy(privacy);
        if (this.eventDate != null && !this.eventDate.isEmpty()) {
            SimpleDateFormat parserSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date d = parserSDF.parse(eventDate);
            dto.setEventDate(d);
        }
        dto.setEventType(eventType);
        dto.setStatus(eventStatus);
        dto.setUploadAfterDate(uploadAfterDate);
        return dto;
    }
}
