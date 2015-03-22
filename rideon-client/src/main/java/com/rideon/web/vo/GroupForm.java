/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.web.vo;

import com.rideon.model.dto.GroupDto;
import com.rideon.model.dto.UserDto;
import com.rideon.common.enums.Privacy;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Fer
 */
public class GroupForm {

    private Long id;
    private String name;
    private String description;
    private UserDto owner;
    private Privacy privacy;
    private MultipartFile image;

    public GroupForm() {
    }

    public GroupForm(GroupDto dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.owner = dto.getOwner();
        this.privacy = dto.getPrivacy();
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserDto getOwner() {
        return owner;
    }

    public void setOwner(UserDto owner) {
        this.owner = owner;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public Privacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
    }

    public GroupDto toGroupDto() {
        GroupDto group = new GroupDto();
        group.setId(id);
        group.setName(name);
        group.setDescription(description);
        group.setOwner(owner);
        group.setPrivacy(privacy);
        return group;
    }
}
