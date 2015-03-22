/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.web.vo;

import com.rideon.model.dto.BicycleDto;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Fer
 */
public class BicicyleForm {

    private Long id;
    private String brand;
    private String model;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private String buyDate;
    private Double kilometers = 0d;
    private MultipartFile image;
    private Boolean isPrincipal;

    public BicicyleForm() {
    }

    public BicicyleForm(BicycleDto dto) {
        this.id = dto.getId();
        this.brand = dto.getBrand();
        this.model = dto.getModel();
        this.isPrincipal = dto.getIsPrincipal();
        this.kilometers = dto.getKilometers();
        if (dto.getBuyDate() != null) {
            SimpleDateFormat parserSDF = new SimpleDateFormat("yyyy-MM-dd");
            String s = parserSDF.format(dto.getBuyDate());
            this.buyDate = s;
        }
    }

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

    public String getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(String buyDate) {
        this.buyDate = buyDate;
    }

    public Double getKilometers() {
        return kilometers;
    }

    public void setKilometers(Double kilometers) {
        this.kilometers = kilometers;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public Boolean getIsPrincipal() {
        return isPrincipal;
    }

    public void setIsPrincipal(Boolean isPrincipal) {
        this.isPrincipal = isPrincipal;
    }

    public BicycleDto toBicycleDto() throws ParseException {
        BicycleDto bike = new BicycleDto();
        bike.setId(id);
        bike.setBrand(brand);
        bike.setModel(model);
        if (kilometers == null) {
            bike.setKilometers(0d);
        }
        else{
            bike.setKilometers(kilometers);
        }
        bike.setIsPrincipal(isPrincipal);
        if (buyDate != null && !buyDate.isEmpty()) {
            SimpleDateFormat parserSDF = new SimpleDateFormat("yyyy-MM-dd");
            Date d = parserSDF.parse(buyDate);
            bike.setBuyDate(d);
        }
        return bike;
    }
}
