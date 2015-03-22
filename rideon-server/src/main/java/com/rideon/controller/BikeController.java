/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.controller;

import com.rideon.model.dto.BicycleDto;
import com.rideon.model.dto.MultimediaDto;
import com.rideon.model.dto.list.BaseListDto;
import com.rideon.model.service.UserService;
import com.rideon.common.util.RestCommonPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Fer
 */
@Controller
public class BikeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BikeController.class);
    @Autowired
    UserService userService;

    @PreAuthorize("@userPermisions.isFriendOf(authentication.name, #userId)")
    @RequestMapping(value = RestCommonPaths.USER_BIKES, method = RequestMethod.GET)
    @ResponseBody
    public BaseListDto<BicycleDto> getAllBikes(@PathVariable(RestCommonPaths.USER_ID) String userId) {
        return userService.getAllBikes(userId);
    }

    @PreAuthorize("@userPermisions.isFriendOf(authentication.name, #userId)")
    @RequestMapping(value = RestCommonPaths.BIKE_BY_ID, method = RequestMethod.GET)
    public ResponseEntity<BicycleDto> getBike(@PathVariable(RestCommonPaths.BIKE_ID) String bikeId,
            @PathVariable(RestCommonPaths.USER_ID) String userId) {

        BicycleDto bike = userService.getBike(userId, bikeId);
        if (bike != null) {
            return new ResponseEntity<>(bike, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("@userPermisions.isAllowed(authentication.name, #userId)")
    @RequestMapping(value = RestCommonPaths.USER_BIKES, method = RequestMethod.POST)
    public ResponseEntity<BicycleDto> addBike(@PathVariable(RestCommonPaths.USER_ID) String userId,
            @RequestBody BicycleDto bike) {
        try {
            bike = userService.addBike(userId, bike);
            return new ResponseEntity<>(bike, HttpStatus.CREATED);
        } catch (Throwable e) {
            LOGGER.error("", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("@userPermisions.isAllowed(authentication.name, #userId)")
    @RequestMapping(value = RestCommonPaths.BIKE_BY_ID, method = RequestMethod.PUT)
    public ResponseEntity<?> updateBike(@PathVariable(RestCommonPaths.USER_ID) String userId,
            @PathVariable(RestCommonPaths.BIKE_ID) String bikeId,
            @RequestBody BicycleDto bike) {
        try {
            userService.updateBike(userId, bike);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @PreAuthorize("@userPermisions.isAllowed(authentication.name, #userId)")
    @RequestMapping(value = RestCommonPaths.BIKE_BY_ID, method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteBike(@PathVariable(RestCommonPaths.BIKE_ID) String bikeId,
            @PathVariable(RestCommonPaths.USER_ID) String userId) {
        try {
            userService.removeBike(userId, bikeId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("@userPermisions.isFriendOf(authentication.name, #userId)")
    @RequestMapping(value = RestCommonPaths.BIKE_IMAGE_BY_ID, method = RequestMethod.GET)
    public ResponseEntity<MultimediaDto> getBikeImage(@PathVariable(RestCommonPaths.BIKE_ID) String bikeId,
            @PathVariable(RestCommonPaths.USER_ID) String userId) {
        MultimediaDto image = userService.getBikeImage(userId, bikeId);
        ResponseEntity<MultimediaDto> response;
        if (image != null) {
            response = new ResponseEntity<>(image, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @PreAuthorize("@userPermisions.isAllowed(authentication.name, #userId)")
    @RequestMapping(value = RestCommonPaths.BIKE_IMAGE_BY_ID, method = RequestMethod.PUT)
    public ResponseEntity<MultimediaDto> setBikeImage(@PathVariable(RestCommonPaths.BIKE_ID) String bikeId,
            @PathVariable(RestCommonPaths.USER_ID) String userId, @RequestBody MultimediaDto image) {
        MultimediaDto img = userService.setBikeImage(userId, bikeId, image);
        ResponseEntity<MultimediaDto> response;
        if (image != null) {
            response = new ResponseEntity<>(img, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
}
