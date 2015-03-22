/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.domain;

import com.rideon.common.enums.SegmentCategory;
import com.rideon.common.enums.SegmentType;
import com.vividsolutions.jts.geom.Coordinate;
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
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import org.hibernate.annotations.Type;

/**
 *
 * @author Fer
 */
@Entity(name = "segments")
public class Segment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private SegmentType segmentType;
    @Type(type = "org.hibernate.spatial.GeometryType")
    private LineString geometry;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id.segmentId", cascade = {CascadeType.ALL})
    private Set<SegmentPractice> practices = new HashSet<>();
    private SegmentCategory category;
    private double distance;
    private double minaltitude;
    private double totalclimb;
    private double maxaltitude;
    private double totaldownhill;
    private double maxInclination;
    private double inclination;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date creationDate = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LineString getGeometry() {
        return geometry;
    }

    public void setGeometry(LineString geom) {
        this.geometry = geom;
    }

    public Set<SegmentPractice> getPractices() {
        return practices;
    }

    public void setPractices(Set<SegmentPractice> practices) {
        this.practices = practices;
    }

    public SegmentType getSegmentType() {
        return segmentType;
    }

    public void setSegmentType(SegmentType type) {
        this.segmentType = type;
    }

    public SegmentCategory getCategory() {
        return category;
    }

    public void setCategory(SegmentCategory category) {
        this.category = category;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
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

    public double getMaxInclination() {
        return maxInclination;
    }

    public void setMaxInclination(double maxInclination) {
        this.maxInclination = maxInclination;
    }

    public double getInclination() {
        return inclination;
    }

    public void setInclination(double inclination) {
        this.inclination = inclination;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Segment) {
            Segment s = (Segment) obj;
            //IF entity exists at database, compare it's ids
            if (s.getId() != null && this.id != null) {
                return this.id.equals(s.getId());
                //If don't, compare the track
            } else {
                if (s.getGeometry() != null && s.getGeometry().getCoordinates().length > 0) {
                    for (int i = 0; i < s.getGeometry().getCoordinates().length; i++) {
                        Coordinate c1 = s.getGeometry().getCoordinates()[i];
                        Coordinate c2 = this.geometry.getCoordinateN(i);
                        if (!c1.equals(c2)) {
                            return false;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + Objects.hashCode(this.id);
        return hash;
    }
}
