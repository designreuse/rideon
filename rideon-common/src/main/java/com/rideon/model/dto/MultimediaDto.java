/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.dto;

import java.io.Serializable;
import org.codehaus.jackson.map.annotate.JsonRootName;

/**
 *
 * @author Fer
 */
@JsonRootName(value = "multimediaDto")
public class MultimediaDto implements Serializable{

    private Long id;
    private String name;
    private Long fileSize;
    private String fileType;
    private byte[] dataArray;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getDataArray() {
        return dataArray;
    }

    public void setDataArray(byte[] dataArray) {
        this.dataArray = dataArray;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
