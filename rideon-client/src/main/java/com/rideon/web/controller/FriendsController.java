/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.web.controller;

import com.rideon.web.util.JSPWebPaths;
import com.rideon.model.dto.FriendshipRequestDto;
import com.rideon.model.dto.NotificationDto;
import com.rideon.model.dto.UserDto;
import com.rideon.model.dto.list.BaseListDto;
import com.rideon.common.util.RestCommonPaths;
import com.rideon.web.util.UrlConstructorSW;
import com.rideon.web.util.UrlConstructorSWImpl;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Fer
 */
@Controller
public class FriendsController extends GenericAbstractController {

    @RequestMapping(value = "/{" + RestCommonPaths.USER_ID + "}/friends", method = RequestMethod.GET)
    public ModelAndView getFriendsPage(@PathVariable(RestCommonPaths.USER_ID) String userId, HttpServletRequest request) {

        ModelAndView model = new ModelAndView(JSPWebPaths.FRIENDS);

        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.USER_ID, userId);

        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.USER_BY_ID);
        UserDto user = getRestTemplate().getForEntity(url.getUrl(), UserDto.class, args).getBody();
        
        url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.USER_FRIENDS);
        BaseListDto<UserDto> friends = getRestTemplate().getForEntity(url.getUrl(), BaseListDto.class, args).getBody();

        url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.USER_FRIENDS_REQUEST);
        BaseListDto<FriendshipRequestDto> requests = getRestTemplate().getForEntity(url.getUrl(), BaseListDto.class, args).getBody();

        model.addObject("user", user);
        model.addObject("requests", requests);
        model.addObject("friends", friends);
        return model;
    }

    @RequestMapping(value = "/{" + RestCommonPaths.USER_ID + "}/friends", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void addFriendRequest(@PathVariable(RestCommonPaths.USER_ID) String userId, @RequestBody String friendId) throws IOException {


        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.USER_FRIENDS_REQUEST);

        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.USER_ID, userId);

        UserDto petitioner = new UserDto();
        petitioner.setUsername(userId);
        UserDto target = new UserDto();
        target.setUsername(friendId);

        FriendshipRequestDto request = new FriendshipRequestDto();
        request.setPetitioner(petitioner);
        request.setTarget(target);

        getRestTemplate().postForEntity(url.getUrl(), request, FriendshipRequestDto.class, args).getBody();
    }

    @RequestMapping(value = "/{" + RestCommonPaths.USER_ID + "}/friends", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    public void updateFriendRequest(@PathVariable(RestCommonPaths.USER_ID) String userId,
            @RequestBody FriendshipRequestDto request) throws IOException {

        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.USER_FRIENDS_REQUEST_BY_ID);

        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.USER_ID, userId);
        Long id = request.getId();
        args.put(RestCommonPaths.REQUEST_ID, id.toString());

        getRestTemplate().put(url.getUrl(), request, args);
    }

    @RequestMapping(value = "/{" + RestCommonPaths.USER_ID + "}/notifications", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    public void updateNotification(@PathVariable(RestCommonPaths.USER_ID) String userId,
            @RequestBody FriendshipRequestDto request) throws IOException {

        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.USER_FRIENDS_REQUEST_BY_ID);

        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.USER_ID, userId);
        Long id = request.getId();
        args.put(RestCommonPaths.REQUEST_ID, id.toString());

        getRestTemplate().put(url.getUrl(), request, args);
    }

    @RequestMapping(value = "/{" + RestCommonPaths.USER_ID + "}/friends/{" + RestCommonPaths.FRIEND_ID + "}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void removeFriend(@PathVariable(RestCommonPaths.USER_ID) String userId,
            @PathVariable(RestCommonPaths.FRIEND_ID) String friendId) throws IOException {

        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.USER_FRIENDS_BY_ID);

        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.USER_ID, userId);
        args.put(RestCommonPaths.FRIEND_ID, friendId);

        getRestTemplate().delete(url.getUrl(), args);
    }

    @RequestMapping(value = "/{" + RestCommonPaths.USER_ID + "}/notifications", method = RequestMethod.GET)
    public @ResponseBody
    List<NotificationDto> getUserNotifications(@PathVariable(RestCommonPaths.USER_ID) String userId) {

        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.USER_NOTIFICATION);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.USER_ID, userId);

        BaseListDto<NotificationDto> list = getRestTemplate().getForEntity(url.getUrl(), BaseListDto.class, args).getBody();
        List<NotificationDto> notList = list.getData();

        return notList;
    }
}
