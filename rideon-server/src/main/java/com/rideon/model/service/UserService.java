/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.service;

import com.rideon.model.dto.BicycleDto;
import com.rideon.model.dto.MultimediaDto;
import com.rideon.model.dto.NotificationDto;
import com.rideon.model.dto.SignFormDto;
import com.rideon.model.dto.UserDto;
import com.rideon.model.dto.list.BaseListDto;

/**
 *
 * @author Fer
 */
public interface UserService {

    public UserDto getById(String id);

    public void remove(String id);

    public UserDto add(SignFormDto formDto);

    public UserDto update(UserDto user);

    public MultimediaDto setImage(String userId, MultimediaDto image);

    public MultimediaDto getImage(String userId);

    public BicycleDto addBike(String userId, BicycleDto bikeDto);

    public void removeBike(String userId, String bikeId);

    public BicycleDto updateBike(String userId, BicycleDto bikeDto);

    public BicycleDto getBike(String userId, String bikeId);

    public BaseListDto<BicycleDto> getAllBikes(String userId);

    public MultimediaDto getBikeImage(String userId, String bikeId);

    public MultimediaDto setBikeImage(String userId, String bikeId, MultimediaDto image);

    public BaseListDto<UserDto> getFriends(String userId);

    public void removeFriend(String userId, String friendId);

    public BaseListDto<UserDto> search(String query);

    public BaseListDto<NotificationDto> getNotifications(String userId);

    public MultimediaDto getThumbnail(String userId);
}
