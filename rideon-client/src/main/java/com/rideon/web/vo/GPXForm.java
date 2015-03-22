/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.web.vo;

import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Fer
 */
public class GPXForm {

    private MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
