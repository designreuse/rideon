/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.web.controller;

import com.rideon.web.util.JSPWebPaths;
import com.rideon.model.dto.BicycleDto;
import com.rideon.model.dto.EventDto;
import com.rideon.model.dto.GroupDto;
import com.rideon.model.dto.MultimediaDto;
import com.rideon.model.dto.UserDto;
import com.rideon.common.util.RestCommonPaths;
import com.rideon.model.dto.PracticeDto;
import com.rideon.web.util.UrlConstructorSW;
import com.rideon.web.util.UrlConstructorSWImpl;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestClientException;
import org.springframework.web.servlet.ModelAndView;
import com.rideon.model.dto.list.BaseListDto;
import com.rideon.model.dto.list.PracticeListDto;
import com.rideon.web.vo.BicicyleForm;
import com.rideon.web.vo.SessionUser;
import com.rideon.web.vo.UserForm;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Fer
 */
@Controller
public class AccountController extends GenericAbstractController {

    private Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

    @RequestMapping(value = "/{" + RestCommonPaths.USER_ID + "}", method = RequestMethod.GET)
    public ModelAndView getProfilePage(@PathVariable(RestCommonPaths.USER_ID) String userId, HttpServletRequest request) {

        SessionUser user = getCurrentUser();
        ModelAndView model;

        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.USER_BY_ID);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.USER_ID, userId);

        if (user.getUsername().equals(userId) || user.getUsername().equals("admin")) {
            model = new ModelAndView(JSPWebPaths.CURRENT_USER_PROFILE);
            if (user.getUsername().equals("admin")) {
                UserDto adminDto = getRestTemplate().getForEntity(url.getUrl(), UserDto.class, args).getBody();
                model.addObject("user", adminDto);
            } else {
                model.addObject("user", user);
            }
        } else {
            model = new ModelAndView(JSPWebPaths.ACCOUNT_NOPRIV);
            UserDto userDto = getRestTemplate().getForEntity(url.getUrl(), UserDto.class, args).getBody();
            model.addObject("user", userDto);
        }

        url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.USER_BIKES);
        BaseListDto<BicycleDto> bikeList = getRestTemplate().getForEntity(url.getUrl(), BaseListDto.class, args).getBody();

        url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.USER_FRIENDS);
        BaseListDto<UserDto> friends = getRestTemplate().getForEntity(url.getUrl(), BaseListDto.class, args).getBody();

        url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.SEARCH_USER_PRACTICES_BY_DATE_RANGE);
        Long toDate = new Date().getTime();
        Long fromDate = new Date().getTime();
        Long m = 1000l * 60 * 6024 * 30;
        fromDate = fromDate - m;

        args = new HashMap<>();
        args.put(RestCommonPaths.FROM_DATE, fromDate.toString());
        args.put(RestCommonPaths.TO_DATE, toDate.toString());
        args.put(RestCommonPaths.USER_ID, userId);

        PracticeListDto list = getRestTemplate().getForEntity(url.getUrl(), PracticeListDto.class, args).getBody();
        List<PracticeDto> practices = list.getData();

        model.addObject("practices", practices);
        model.addObject("friends", friends);
        model.addObject("groups", getGroups(userId));
        model.addObject("events", getEvents(userId));
        model.addObject("bicycles", bikeList.getData());
        return model;
    }

    @RequestMapping(value = "/{" + RestCommonPaths.USER_ID + "}/edit", method = RequestMethod.GET)
    public ModelAndView getEditProfilePage(@PathVariable(RestCommonPaths.USER_ID) String userId) {
        ModelAndView model = new ModelAndView(JSPWebPaths.CURRENT_USER_PROFILE_EDIT);
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.USER_BY_ID);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.USER_ID, userId);
        UserDto userDto = getRestTemplate().getForEntity(url.getUrl(), UserDto.class, args).getBody();
        UserForm userForm = new UserForm(userDto);
        model.addObject("userForm", userForm);
        return model;
    }

    @RequestMapping(value = "/{" + RestCommonPaths.USER_ID + "}/edit", method = RequestMethod.POST)
    public String editProfile(UserForm userForm, @PathVariable(RestCommonPaths.USER_ID) String userId) throws IOException, ParseException {

        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.USER_BY_ID);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.USER_ID, userId);

        getRestTemplate().put(url.getUrl(), userForm.toUserDto(), args);

        //Update bicycle image
        if (!userForm.getImage().isEmpty()) {
            MultimediaDto image = new MultimediaDto();
            byte[] bArray = userForm.getImage().getBytes();

            image.setDataArray(bArray);
            image.setFileType(userForm.getImage().getContentType());
            image.setFileSize(userForm.getImage().getSize());

            args = new HashMap<>();
            args.put(RestCommonPaths.USER_ID, getCurrentUser().getUsername());
            url = new UrlConstructorSWImpl(getUrlBase());
            url.addParameter(RestCommonPaths.USER_IMAGE_BY_ID);
            getRestTemplate().put(url.getUrl(), image, args);
        }

        return "redirect:/" + getCurrentUser().getUsername();
    }

    @RequestMapping(value = "/{" + RestCommonPaths.USER_ID + "}/image", method = RequestMethod.GET)
    public void getUserImage(@PathVariable(RestCommonPaths.USER_ID) String userId, HttpServletResponse response) throws IOException {


        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.USER_IMAGE_BY_ID);

        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.USER_ID, userId);

        MultimediaDto image = getRestTemplate().getForEntity(url.getUrl(), MultimediaDto.class, args).getBody();
        byte[] data = image.getDataArray();

        response.setContentType("image/jpeg");
        response.getOutputStream().write(data);
        response.getOutputStream().flush();
    }

    @RequestMapping(value = "/{" + RestCommonPaths.USER_ID + "}/thumbnail", method = RequestMethod.GET)
    public void getUserThumbnaild(@PathVariable(RestCommonPaths.USER_ID) String userId, HttpServletResponse response) throws IOException {

        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.USER_THUMBNAIL_BY_ID);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.USER_ID, userId);

        MultimediaDto image = getRestTemplate().getForEntity(url.getUrl(), MultimediaDto.class, args).getBody();
        byte[] data = image.getDataArray();

        response.setContentType("image/jpeg");
        response.getOutputStream().write(data);
        response.getOutputStream().flush();

    }

    @RequestMapping(value = "/{" + RestCommonPaths.USER_ID + "}/bike/{" + RestCommonPaths.BIKE_ID + "}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void removeBike(@PathVariable(RestCommonPaths.USER_ID) String userId, @PathVariable(RestCommonPaths.BIKE_ID) String bikeId) {
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.BIKE_BY_ID);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.USER_ID, userId);
        args.put(RestCommonPaths.BIKE_ID, bikeId);
        try {
            getRestTemplate().delete(url.getUrl(), args);
        } catch (RestClientException e) {
            LOGGER.error("", e);
        }
    }

    @RequestMapping(value = "/{" + RestCommonPaths.USER_ID + "}/bike", method = RequestMethod.GET)
    public ModelAndView getAddBikePage(@PathVariable(RestCommonPaths.USER_ID) String userId, HttpServletRequest request) {
        ModelAndView model = new ModelAndView(JSPWebPaths.BIKE_ADD);
        BicicyleForm bikeForm = new BicicyleForm();
        model.addObject("user", getCurrentUser());
        model.addObject("bicycleForm", bikeForm);
        return model;
    }

    @RequestMapping(value = "/{" + RestCommonPaths.USER_ID + "}/bike", method = RequestMethod.POST)
    public String addBike(@PathVariable(RestCommonPaths.USER_ID) String userId, BicicyleForm bikeForm) throws IOException, ParseException {

        //Bicycle Creation
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.USER_BIKES);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.USER_ID, userId);
        BicycleDto newBike = getRestTemplate().postForEntity(url.getUrl(), bikeForm.toBicycleDto(), BicycleDto.class, args).getBody();


        if (!bikeForm.getImage().isEmpty()) {
            MultimediaDto image = new MultimediaDto();
            image.setDataArray(bikeForm.getImage().getBytes());
            image.setFileType(bikeForm.getImage().getContentType());
            image.setFileSize(bikeForm.getImage().getSize());

            url = new UrlConstructorSWImpl(getUrlBase());
            url.addParameter(RestCommonPaths.BIKE_IMAGE_BY_ID);
            args.put(RestCommonPaths.BIKE_ID, newBike.getId().toString());
            getRestTemplate().put(url.getUrl(), image, args);
        }

        return "redirect:/" + userId;
    }

    @RequestMapping(value = "/{" + RestCommonPaths.USER_ID + "}/bike/{" + RestCommonPaths.BIKE_ID + "}", method = RequestMethod.POST)
    public String editBike(@PathVariable(RestCommonPaths.USER_ID) String userId,
            @PathVariable(RestCommonPaths.BIKE_ID) String bikeId, BicicyleForm bikeForm) throws IOException, ParseException {

        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.BIKE_BY_ID);
        //Update bicycle info
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.USER_ID, userId);
        args.put(RestCommonPaths.BIKE_ID, bikeId);
        getRestTemplate().put(url.getUrl(), bikeForm.toBicycleDto(), args);

        //Update bicycle image
        if (!bikeForm.getImage().isEmpty()) {
            MultimediaDto image = new MultimediaDto();
            image.setDataArray(bikeForm.getImage().getBytes());
            url = new UrlConstructorSWImpl(getUrlBase());
            url.addParameter(RestCommonPaths.BIKE_IMAGE_BY_ID);
            getRestTemplate().put(url.getUrl(), image, args);
        }
        return "redirect:/" + userId;
    }

    @RequestMapping(value = "/{" + RestCommonPaths.USER_ID + "}/bike/{" + RestCommonPaths.BIKE_ID + "}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void editBikeAjax(@PathVariable(RestCommonPaths.USER_ID) String userId,
            @PathVariable(RestCommonPaths.BIKE_ID) String bikeId, @RequestBody BicicyleForm bikeForm) throws IOException, ParseException {

        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.BIKE_BY_ID);
        //Update bicycle info
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.USER_ID, userId);
        args.put(RestCommonPaths.BIKE_ID, bikeId);
        getRestTemplate().put(url.getUrl(), bikeForm.toBicycleDto(), args);
    }

    @RequestMapping(value = "/{" + RestCommonPaths.USER_ID + "}/bike/{" + RestCommonPaths.BIKE_ID + "}/image", method = RequestMethod.GET)
    public void getBikeImage(@PathVariable(RestCommonPaths.USER_ID) String userId,
            @PathVariable(RestCommonPaths.BIKE_ID) String bikeId, HttpServletResponse response) throws IOException {


        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.BIKE_IMAGE_BY_ID);

        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.USER_ID, userId);
        args.put(RestCommonPaths.BIKE_ID, bikeId);

        MultimediaDto image = getRestTemplate().getForEntity(url.getUrl(), MultimediaDto.class, args).getBody();
        byte[] data = image.getDataArray();

        response.setContentType("image/jpeg");
        response.getOutputStream().write(data);
        response.getOutputStream().flush();
    }

    @RequestMapping(value = "/{" + RestCommonPaths.USER_ID + "}/bike/{" + RestCommonPaths.BIKE_ID + "}", method = RequestMethod.GET)
    public ModelAndView getEditBikePage(@PathVariable(RestCommonPaths.USER_ID) String userId,
            @PathVariable(RestCommonPaths.BIKE_ID) String bikeId) {

        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.BIKE_BY_ID);

        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.USER_ID, userId);
        args.put(RestCommonPaths.BIKE_ID, bikeId);

        BicycleDto response = getRestTemplate().getForEntity(url.getUrl(), BicycleDto.class, args).getBody();
        BicicyleForm bikeForm = new BicicyleForm(response);
        ModelAndView model = new ModelAndView(JSPWebPaths.BIKE_EDIT);
        model.addObject("user", getCurrentUser());
        model.addObject("bicycleForm", bikeForm);

        return model;
    }

    @RequestMapping(value = "/{" + RestCommonPaths.USER_ID + "}", method = RequestMethod.DELETE)
    public @ResponseBody
    String removeUser(@PathVariable(RestCommonPaths.USER_ID) String userId, HttpServletRequest request) {

        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.USER_BY_ID);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.USER_ID, userId);

        getRestTemplate().delete(url.getUrl(), args);

        request.getSession().invalidate();

        return "redirect:/login";
    }

    private BaseListDto<EventDto> getEvents(String userId) {
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.USER_EVENTS);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.USER_ID, userId);
        BaseListDto<EventDto> events = getRestTemplate().getForEntity(url.getUrl(), BaseListDto.class, args).getBody();
        return events;
    }

    private BaseListDto<GroupDto> getGroups(String userId) {
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.USER_GROUPS);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.USER_ID, userId);
        BaseListDto<GroupDto> events = getRestTemplate().getForEntity(url.getUrl(), BaseListDto.class, args).getBody();
        return events;
    }
}
