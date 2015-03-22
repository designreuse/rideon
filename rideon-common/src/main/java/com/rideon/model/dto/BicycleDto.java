/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.dto;

import java.io.Serializable;
import java.util.Date;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Fer
 */
@JsonRootName(value = "bicycleDto")
public class BicycleDto implements Serializable{

    private Long id;
    private String brand;
    private String model;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date buyDate;
    private Double kilometers;
    private Boolean isPrincipal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Date getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }

    public Double getKilometers() {
        return kilometers;
    }

    public void setKilometers(Double kilometers) {
        this.kilometers = kilometers;
    }

    public Boolean getIsPrincipal() {
        return isPrincipal;
    }

    public void setIsPrincipal(Boolean isPrincipal) {
        this.isPrincipal = isPrincipal;
    }
}
