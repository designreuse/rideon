/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.web.vo;

import com.rideon.model.dto.PracticeDto;
import com.rideon.model.dto.SegmentPracticeDto;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import org.codehaus.jackson.map.annotate.JsonRootName;

/**
 *
 * @author Fer
 */
@JsonRootName(value = "calendarEvent")
public class CalendarPracticeEvent {

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
    private double mediumspeed;
    private long duration;
    private double maxspeed;
    private List<SegmentPracticeDto> segmentsResults;

    public CalendarPracticeEvent() {
    }

    public CalendarPracticeEvent(PracticeDto practice) throws ParseException {
        this.id = practice.getId();
        this.title = "Practice";
        this.start = new Date(practice.getPracticeDate().getTime() / 1000);
        this.mediumspeed = practice.getMediumspeed();
        this.maxspeed = practice.getMaxspeed();
        this.segmentsResults = practice.getSegmentsResults();
        this.duration = practice.getDuration();
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

    public double getMediumspeed() {
        return mediumspeed;
    }

    public void setMediumspeed(double mediumspeed) {
        this.mediumspeed = mediumspeed;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public double getMaxspeed() {
        return maxspeed;
    }

    public void setMaxspeed(double maxspeed) {
        this.maxspeed = maxspeed;
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

    public List<SegmentPracticeDto> getSegmentsResults() {
        return segmentsResults;
    }

    public void setSegmentsResults(List<SegmentPracticeDto> segmentsResults) {
        this.segmentsResults = segmentsResults;
    }
}
