/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.controller;

import com.rideon.model.dto.FriendshipRequestDto;
import com.rideon.model.dto.MultimediaDto;
import com.rideon.model.dto.NotificationDto;
import com.rideon.model.dto.SignFormDto;
import com.rideon.model.dto.UserDto;
import com.rideon.model.dto.list.BaseListDto;
import com.rideon.model.service.FriendshipRequestService;
import com.rideon.model.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.rideon.common.util.RestCommonPaths;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Fer
 */
@Controller
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private FriendshipRequestService friendshipRequestService;

    @PreAuthorize("@userPermisions.isFriendOf(authentication.name, #userId)")
    @RequestMapping(value = RestCommonPaths.USER_BY_ID, method = RequestMethod.GET)
    public ResponseEntity<UserDto> getUser(@PathVariable(RestCommonPaths.USER_ID) String userId) {
        UserDto user = userService.getById(userId);
        if (user != null) {
            LOGGER.debug("Found user: {}", user.getEmail());
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = RestCommonPaths.USER, method = RequestMethod.POST)
    public ResponseEntity<UserDto> addUser(@RequestBody SignFormDto form) throws DuplicateKeyException {
        if (userExist(form.getEmail())) {
            throw new DuplicateKeyException("User " + form.getEmail() + " already exist");
        }
        LOGGER.info("Adding user {}", form.getEmail());
        UserDto createdUser = userService.add(form);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PreAuthorize("@userPermisions.isAllowed(authentication.name, #userDto.username)")
    @RequestMapping(value = RestCommonPaths.USER_BY_ID, method = RequestMethod.PUT)
    public @ResponseBody
    UserDto updateUser(@PathVariable(RestCommonPaths.USER_ID) String userId,
            @RequestBody UserDto userDto) throws DuplicateKeyException {
        return userService.update(userDto);
    }

    @PreAuthorize("@userPermisions.isFriendOf(authentication.name, #userId)")
    @RequestMapping(value = RestCommonPaths.USER_IMAGE_BY_ID, method = RequestMethod.GET)
    public ResponseEntity<MultimediaDto> getUserImage(@PathVariable(RestCommonPaths.USER_ID) String userId) {
        MultimediaDto image = userService.getImage(userId);
        if (image != null) {
            return new ResponseEntity<>(image, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = RestCommonPaths.USER_THUMBNAIL_BY_ID, method = RequestMethod.GET)
    public ResponseEntity<MultimediaDto> getThumbnail(@PathVariable(RestCommonPaths.USER_ID) String userId) {
        MultimediaDto image = userService.getThumbnail(userId);
        if (image != null) {
            return new ResponseEntity<>(image, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("@userPermisions.isAllowed(authentication.name, #userId)")
    @RequestMapping(value = RestCommonPaths.USER_IMAGE_BY_ID, method = {RequestMethod.PUT, RequestMethod.POST})
    public ResponseEntity<MultimediaDto> setUserImage(@PathVariable(RestCommonPaths.USER_ID) String userId, @RequestBody MultimediaDto img) {
        MultimediaDto image = userService.setImage(userId, img);
        if (image != null) {
            return new ResponseEntity<>(image, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("@userPermisions.isFriendOf(authentication.name, #userId)")
    @RequestMapping(value = RestCommonPaths.USER_FRIENDS, method = RequestMethod.GET)
    public @ResponseBody
    BaseListDto<UserDto> getUserFriends(@PathVariable(RestCommonPaths.USER_ID) String userId) {
        BaseListDto userList = userService.getFriends(userId);
        return userList;
    }

    @PreAuthorize("@userPermisions.isAllowed(authentication.name, #userId)")
    @RequestMapping(value = RestCommonPaths.USER_FRIENDS_BY_ID, method = RequestMethod.DELETE)
    public @ResponseBody
    ResponseEntity removeUserFriends(@PathVariable(RestCommonPaths.USER_ID) String userId,
            @PathVariable(RestCommonPaths.FRIEND_ID) String friendId) {
        userService.removeFriend(userId, friendId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("@userPermisions.isAllowed(authentication.name, #userId)")
    @RequestMapping(value = RestCommonPaths.USER_NOTIFICATION, method = RequestMethod.GET)
    public @ResponseBody
    BaseListDto<NotificationDto> getUserNotifications(@PathVariable(RestCommonPaths.USER_ID) String userId) {
        BaseListDto notifications = userService.getNotifications(userId);
        return notifications;
    }

    @PreAuthorize("@userPermisions.isAllowed(authentication.name, #userId)")
    @RequestMapping(value = RestCommonPaths.USER_FRIENDS_REQUEST, method = RequestMethod.GET)
    public @ResponseBody
    BaseListDto<FriendshipRequestDto> getFriendshipRequest(@PathVariable(RestCommonPaths.USER_ID) String userId) {
        BaseListDto<FriendshipRequestDto> request = friendshipRequestService.getByUserId(userId);
        return request;
    }

    @RequestMapping(value = RestCommonPaths.USER_FRIENDS_REQUEST, method = RequestMethod.POST)
    public @ResponseBody
    FriendshipRequestDto addFriendshipRequest(@PathVariable(RestCommonPaths.USER_ID) String userId, @RequestBody FriendshipRequestDto request) {
        FriendshipRequestDto req = friendshipRequestService.add(request);
        return req;
    }

    @PreAuthorize("@userPermisions.isAllowed(authentication.name, #request.target.username) or "
            + "@userPermisions.isAllowed(authentication.name, #request.petitioner.username)")
    @RequestMapping(value = RestCommonPaths.USER_FRIENDS_REQUEST_BY_ID, method = RequestMethod.PUT)
    public @ResponseBody
    FriendshipRequestDto updateFriendshipRequest(@PathVariable(RestCommonPaths.USER_ID) String userId,
            @PathVariable(RestCommonPaths.REQUEST_ID) String requestId, @RequestBody FriendshipRequestDto request) {
        FriendshipRequestDto req = friendshipRequestService.updateStatus(requestId, request);
        return req;
    }

    @PreAuthorize("@userPermisions.isAllowed(authentication.name, #userId)")
    @RequestMapping(value = RestCommonPaths.USER_BY_ID, method = RequestMethod.DELETE)
    public ResponseEntity removeUser(@PathVariable(RestCommonPaths.USER_ID) String userId) {
        try {
            userService.remove(userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Boolean userExist(String userId) {
        LOGGER.info("Adding user {}", userId);
        Boolean bol = userService.getById(userId) != null;
        return bol;
    }
}
