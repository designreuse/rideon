/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.web.controller;

import com.rideon.web.util.JSPWebPaths;
import com.rideon.model.dto.EventDto;
import com.rideon.model.dto.GroupDto;
import com.rideon.model.dto.MessageDto;
import com.rideon.model.dto.MultimediaDto;
import com.rideon.model.dto.UserDto;
import com.rideon.common.util.RestCommonPaths;
import com.rideon.web.util.UrlConstructorSW;
import com.rideon.web.util.UrlConstructorSWImpl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.rideon.model.dto.list.BaseListDto;
import com.rideon.model.dto.list.UserListDto;
import com.rideon.web.vo.EventForm;
import com.rideon.web.vo.GroupForm;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Fer
 */
@Controller
public class GroupController extends GenericAbstractController {

    @RequestMapping(value = "/groups", method = RequestMethod.GET)
    public ModelAndView getGroupsPage(HttpServletRequest request) {
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.USER_GROUPS);

        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.USER_ID, request.getUserPrincipal().getName());
        BaseListDto<GroupDto> response = getRestTemplate().getForEntity(url.getUrl(), BaseListDto.class, args).getBody();
        List<GroupDto> groups = response.getData();

        ModelAndView model = new ModelAndView(JSPWebPaths.GROUPS);
        model.addObject("groups", groups);
        return model;
    }

    @RequestMapping(value = "/groups/add", method = RequestMethod.GET)
    public ModelAndView getAddGroupPage() {
        ModelAndView model = new ModelAndView(JSPWebPaths.GROUP_ADD);
        model.addObject("group", new GroupDto());
        return model;
    }

    @RequestMapping(value = "/groups/add", method = RequestMethod.POST)
    public String addGroup(GroupDto groupDto, HttpServletRequest request) {

        UserDto owner = new UserDto();
        owner.setUsername(request.getUserPrincipal().getName());
        groupDto.setOwner(owner);

        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.GROUP);

        ResponseEntity<GroupDto> response = getRestTemplate().postForEntity(url.getUrl(), groupDto, GroupDto.class);

        if (response.getStatusCode() == HttpStatus.CREATED) {
            return "redirect:/groups/" + response.getBody().getId();
        } else {
            return "redirect:/groups";
        }
    }

    @RequestMapping(value = "/groups/{" + RestCommonPaths.GROUP_ID + "}", method = RequestMethod.GET)
    public ModelAndView getGroupPage(@PathVariable(RestCommonPaths.GROUP_ID) String groupId) {
        ModelAndView model;
        GroupDto group = getGroup(groupId);
        UserListDto members = getMembers(groupId);
        UserDto currentUser = new UserDto();
        currentUser.setUsername(getCurrentUser().getUsername());

        if (group.getOwner().equals(currentUser)|| getCurrentUser().getUsername().equals("admin")) {
            model = new ModelAndView(JSPWebPaths.GROUP_ADMIN);
        } else if (members.contains(currentUser)) {
            model = new ModelAndView(JSPWebPaths.GROUP_MEMBER);
        } else {
            model = new ModelAndView(JSPWebPaths.GROUP_NOMEMBER);
        }
        model.addObject("group", group);
        model.addObject("members", members);
        model.addObject("message", new MessageDto());
        model.addObject("messages", getMessages(groupId));
        model.addObject("events", getEvents(groupId));

        return model;
    }

    @RequestMapping(value = "/groups/{" + RestCommonPaths.GROUP_ID + "}/members", method = RequestMethod.GET)
    public ModelAndView getAddMembersPage(@PathVariable(RestCommonPaths.GROUP_ID) String groupId, HttpServletRequest request) {

        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.GROUP_MEMBERS);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.GROUP_ID, groupId);
        BaseListDto<UserDto> members = getRestTemplate().getForEntity(url.getUrl(), BaseListDto.class, args).getBody();

        ModelAndView model = new ModelAndView(JSPWebPaths.GROUP_ADD_MEMBERS_ADMIN);
        model.addObject("group", getGroup(groupId));
        model.addObject("members", members);
        return model;
    }

    @RequestMapping(value = "/groups/{" + RestCommonPaths.GROUP_ID + "}/members/add", method = RequestMethod.POST)
    public ResponseEntity addMembers(@PathVariable(RestCommonPaths.GROUP_ID) String groupId, @RequestBody BaseListDto<String> usersId) {
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.GROUP_MEMBERS);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.GROUP_ID, groupId);

        ResponseEntity<BaseListDto> res = getRestTemplate().postForEntity(url.getUrl(), usersId, BaseListDto.class, args);
        return res;
    }

    @RequestMapping(value = "/groups/{" + RestCommonPaths.GROUP_ID + "}/members/{" + RestCommonPaths.USER_ID + "}", method = RequestMethod.DELETE)
    public @ResponseBody
    String removeMembers(@PathVariable(RestCommonPaths.GROUP_ID) String groupId,
            @PathVariable(RestCommonPaths.USER_ID) String userId) {
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.GROUP_MEMBERS_BY_ID);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.GROUP_ID, groupId);
        args.put(RestCommonPaths.USER_ID, userId);

        getRestTemplate().delete(url.getUrl(), args);
        return "User Removed";
    }
//de aqui

    @RequestMapping(value = "/groups/{" + RestCommonPaths.GROUP_ID + "}/events/add", method = RequestMethod.GET)
    public ModelAndView getAddEventPage(@PathVariable(RestCommonPaths.GROUP_ID) String groupId) {

        ModelAndView model = new ModelAndView(JSPWebPaths.GROUP_ADD_EVENTS);
        model.addObject("eventForm", new EventForm());
        return model;
    }

    @RequestMapping(value = "/groups/{" + RestCommonPaths.GROUP_ID + "}/events/add", method = RequestMethod.POST)
    public String addEvent(@PathVariable(RestCommonPaths.GROUP_ID) String groupId, EventForm eventForm) throws ParseException {
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.GROUP_EVENTS_BY_ID);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.GROUP_ID, groupId);
        UserDto owner = new UserDto();
        owner.setUsername(getCurrentUser().getUsername());
        eventForm.setOwner(owner);
        EventDto event = getRestTemplate().postForObject(url.getUrl(), eventForm.toEventDto(), EventDto.class, args);

        return "redirect:/events/" + event.getId();
    }

    @RequestMapping(value = "/groups/{" + RestCommonPaths.GROUP_ID + "}/edit", method = RequestMethod.GET)
    public ModelAndView getEditGroupPage(@PathVariable(RestCommonPaths.GROUP_ID) String groupId) {

        ModelAndView model = new ModelAndView(JSPWebPaths.GROUP_EDIT);
        model.addObject("groupForm", new GroupForm(getGroup(groupId)));
        model.addObject("members", getMembers(groupId));
        return model;
    }

    @RequestMapping(value = "/groups/{" + RestCommonPaths.GROUP_ID + "}/edit", method = RequestMethod.POST)
    public String editGroup(@PathVariable(RestCommonPaths.GROUP_ID) String groupId, GroupForm groupFrom) throws IOException {

        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.GROUP_BY_ID);
        //Update bicycle info
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.GROUP_ID, groupId);
        getRestTemplate().put(url.getUrl(), groupFrom.toGroupDto(), args);

        //Update bicycle image
        if (!groupFrom.getImage().isEmpty()) {
            MultimediaDto image = new MultimediaDto();
            image.setDataArray(groupFrom.getImage().getBytes());
            url = new UrlConstructorSWImpl(getUrlBase());
            url.addParameter(RestCommonPaths.GROUP_IMAGE_BY_ID);
            getRestTemplate().put(url.getUrl(), image, args);
        }
        return "redirect:/groups/" + groupId;
    }

    @RequestMapping(value = "/groups/{" + RestCommonPaths.GROUP_ID + "}/image", method = RequestMethod.GET)
    public void getGroupImage(@PathVariable(RestCommonPaths.GROUP_ID) String groupId, HttpServletResponse response) throws IOException {
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.GROUP_IMAGE_BY_ID);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.GROUP_ID, groupId);

        MultimediaDto image = getRestTemplate().getForEntity(url.getUrl(), MultimediaDto.class, args).getBody();
        byte[] data = image.getDataArray();

        response.setContentType("image/jpeg");
        response.getOutputStream().write(data);
        response.getOutputStream().flush();
    }

    @RequestMapping(value = "/groups/{" + RestCommonPaths.GROUP_ID + "}/thumbnail", method = RequestMethod.GET)
    public void getGroupThumbnail(@PathVariable(RestCommonPaths.GROUP_ID) String groupId, HttpServletResponse response) throws IOException {
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.GROUP_THUMBNAIL_BY_ID);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.GROUP_ID, groupId);

        MultimediaDto image = getRestTemplate().getForEntity(url.getUrl(), MultimediaDto.class, args).getBody();
        byte[] data = image.getDataArray();

        response.setContentType("image/jpeg");
        response.getOutputStream().write(data);
        response.getOutputStream().flush();
    }

    @RequestMapping(value = "/groups/{" + RestCommonPaths.GROUP_ID + "}/remove", method = RequestMethod.GET)
    public String removeGroup(@PathVariable(RestCommonPaths.GROUP_ID) String groupId) {

        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.GROUP_BY_ID);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.GROUP_ID, groupId);
        getRestTemplate().delete(url.getUrl(), args);

        return "redirect:/groups";
    }

    @RequestMapping(value = "/groups/{" + RestCommonPaths.GROUP_ID + "}/message", method = RequestMethod.POST)
    public String addMessage(@PathVariable(RestCommonPaths.GROUP_ID) String groupId, MessageDto message) {
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.GROUP_MESSAGES);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.GROUP_ID, groupId);

        message.setOwner(getCurrentUser().toUserDto());
        message.setWrittingDate(new Date());

        getRestTemplate().postForEntity(url.getUrl(), message, MessageDto.class, args).getBody();
        return "redirect:/groups/" + groupId;
    }

    @RequestMapping(value = "/groups/{" + RestCommonPaths.GROUP_ID + "}/message/{" + RestCommonPaths.MESSAGE_ID + "}", method = RequestMethod.DELETE)
    public @ResponseBody
    String removeMessage(@PathVariable(RestCommonPaths.GROUP_ID) String groupId, @PathVariable(RestCommonPaths.MESSAGE_ID) String messageId) {
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.GROUP_MESSAGES_BY_ID);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.GROUP_ID, groupId);
        args.put(RestCommonPaths.MESSAGE_ID, messageId);

        getRestTemplate().delete(url.getUrl(), args);
        return "redirect:/groups/" + groupId;
    }

    private UserListDto getMembers(String groupId) {
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.GROUP_MEMBERS);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.GROUP_ID, groupId);
        UserListDto members = getRestTemplate().getForEntity(url.getUrl(), UserListDto.class, args).getBody();
        return members;
    }

    private BaseListDto<MessageDto> getMessages(String groupId) {
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.GROUP_MESSAGES);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.GROUP_ID, groupId);
        BaseListDto<MessageDto> messages = getRestTemplate().getForEntity(url.getUrl(), BaseListDto.class, args).getBody();
        return messages;
    }

    private BaseListDto<EventDto> getEvents(String groupId) {
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.GROUP_EVENTS_BY_ID);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.GROUP_ID, groupId);
        BaseListDto<EventDto> events = getRestTemplate().getForEntity(url.getUrl(), BaseListDto.class, args).getBody();
        return events;
    }

    private GroupDto getGroup(String groupId) {
        UrlConstructorSW url = new UrlConstructorSWImpl(getUrlBase());
        url.addParameter(RestCommonPaths.GROUP_BY_ID);
        Map<String, String> args = new HashMap<>();
        args.put(RestCommonPaths.GROUP_ID, groupId);

        GroupDto group = getRestTemplate().getForEntity(url.getUrl(), GroupDto.class, args).getBody();
        return group;
    }
}
