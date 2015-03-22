/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.web.vo;

import com.rideon.model.dto.EventDto;
import com.rideon.common.enums.EventType;
import com.rideon.common.enums.Privacy;
import java.text.ParseException;
import java.util.Date;
import org.codehaus.jackson.map.annotate.JsonRootName;

/**
 *
 * @author Fer
 */
@JsonRootName(value = "calendarEvent")
public class CalendarEvent {

    private Long id;
    private String title;
    private Date start;
    private Date end;
    private Boolean allDay;
    private String url;
    private String className;
    private Boolean editable;
    private Boolean startEditable;
    private Boolean durationEditable;
    private EventType eventType;
    private Privacy privacy;

    public CalendarEvent() {
    }

    public CalendarEvent(EventDto event) throws ParseException {
        this.id = event.getId();
        if (event.getName() != null) {
            this.title = event.getName();
        } else {
            this.title = event.getEventType().name();
        }
        if (event.getEventDate() != null) {
            Date d1 = event.getEventDate();
            Long dL = d1.getTime();
            this.start = new Date(dL / 1000);
        }
        this.eventType = event.getEventType();
        this.privacy = event.getPrivacy();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Boolean getAllDay() {
        return allDay;
    }

    public void setAllDay(Boolean allDay) {
        this.allDay = allDay;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public Boolean getStartEditable() {
        return startEditable;
    }

    public void setStartEditable(Boolean startEditable) {
        this.startEditable = startEditable;
    }

    public Boolean getDurationEditable() {
        return durationEditable;
    }

    public void setDurationEditable(Boolean durationEditable) {
        this.durationEditable = durationEditable;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Privacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
    }
}
