/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.domain;

import com.rideon.common.enums.BicycleType;
import com.vividsolutions.jts.geom.LineString;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import org.hibernate.annotations.Type;

/**
 *
 * @author Fer
 */
@Entity(name = "routes")
public class Route implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @Lob
    private String description;
//    private BicycleType type;
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Segment> segments = new HashSet<>();
    @Type(type = "org.hibernate.spatial.GeometryType")
    private LineString geometry;
    private double minaltitude;
    private double totalclimb;
    private double maxaltitude;
    private double totaldownhill;
    private double distance;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date creationDate = new Date();

    public LineString getGeometry() {
        return geometry;
    }

    public void setGeometry(LineString geometry) {
        this.geometry = geometry;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Segment> getSegments() {
        return segments;
    }

    public void setSegments(Set<Segment> segments) {
        this.segments = segments;
    }

//    public BicycleType getType() {
//        return type;
//    }
//
//    public void setType(BicycleType type) {
//        this.type = type;
//    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getMinaltitude() {
        return minaltitude;
    }

    public void setMinaltitude(double minaltitude) {
        this.minaltitude = minaltitude;
    }

    public double getTotalclimb() {
        return totalclimb;
    }

    public void setTotalclimb(double totalclimb) {
        this.totalclimb = totalclimb;
    }

    public double getMaxaltitude() {
        return maxaltitude;
    }

    public void setMaxaltitude(double maxaltitude) {
        this.maxaltitude = maxaltitude;
    }

    public double getTotaldownhill() {
        return totaldownhill;
    }

    public void setTotaldownhill(double totaldownhill) {
        this.totaldownhill = totaldownhill;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Route) {
            return this.id.equals(((Route) obj).getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.id);
        return hash;
    }
}
