/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.test.service;

import com.rideon.model.dto.EventDto;
import com.rideon.model.dto.PracticeDto;
import com.rideon.model.dto.RouteDto;
import com.rideon.model.dto.SegmentDto;
import com.rideon.model.dto.SegmentPracticeDto;
import com.rideon.model.dto.UserDto;
import com.rideon.model.dto.list.BaseListDto;
import com.rideon.model.dto.list.SegmentListDto;
import com.rideon.model.dto.list.UserListDto;
import com.rideon.common.enums.EventType;
import com.rideon.common.enums.Privacy;
import com.rideon.segmentdetector.util.GisUtils;
import com.rideon.model.service.EventService;
import com.rideon.model.service.GroupService;
import com.rideon.model.service.PracticeService;
import com.rideon.model.service.RouteService;
import com.rideon.model.service.SegmentService;
import com.rideon.model.service.UserService;
import com.rideon.test.BaseTest;
import com.rideon.util.FilePaths;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import ma.glasnost.orika.MapperFacade;
import org.alternativevision.gpx.beans.GPX;
import org.geojson.Feature;
import org.geojson.GeoJsonObject;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.xml.sax.SAXException;

/**
 *
 * @author Fer
 */
public class EventServiceTest extends BaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventServiceTest.class);
    @Autowired
    EventService eventService;
    @Autowired
    GroupService groupService;
    @Autowired
    UserService userService;
    @Autowired
    RouteService routeService;
    @Autowired
    SegmentService segmentService;
    @Autowired
    PracticeService practiceService;
    @Autowired
    MapperFacade mapper;

    @BeforeTransaction
    public void addEvent() throws FileNotFoundException, ParserConfigurationException, SAXException, IOException, TransformException, TransformerException, ParseException {

//        GeoJsonObject o = new Feature();
//
//        if (eventService.search("Desafio_1").isEmpty()) {
//            EventDto eventDto = new EventDto();
//            UserDto owner = userService.getById("vilas");
//            if (owner != null) {
//                eventDto.setOwner(owner);
//                eventDto.setName("Desafio_1");
//                eventDto.setPrivacy(Privacy.PUBLIC);
//                SimpleDateFormat parserSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//                Date d = parserSDF.parse("2014-08-26 12:00");
//                eventDto.setEventDate(d);
//                eventDto.setEventType(EventType.CHALLENGE);
//                eventDto = (EventDto) eventService.add(eventDto);
//
//                UserDto artur = userService.getById("artur");
//                UserDto mati = userService.getById("mati");
//                UserDto manu = userService.getById("manu");
//                UserListDto members = new UserListDto();
//                members.add(artur);
//                members.add(mati);
//                members.add(manu);
//                eventService.addMembers(eventDto.getId().toString(), members);
//
//                GPX route_gpx = GisUtils.parseGpx(getClass().getResource(FilePaths.ROUTE_3).getFile());
//                PracticeDto practiceDto = practiceService.add(route_gpx, "artur");
//                practiceDto.setPrivacy(Privacy.PUBLIC);
//                practiceService.changePrivacy(practiceDto);
//                RouteDto route = practiceService.getRouteById(practiceDto.getId().toString());
//                eventService.setRoute(route.getId(), eventDto.getId().toString());
//
//                GPX route_3_1_gpx = GisUtils.parseGpx(getClass().getResource(FilePaths.ROUTE_3_1).getFile());
//                PracticeDto practiceDto1 = practiceService.add(route_3_1_gpx, "artur");
//                eventService.addPractice(eventDto.getId().toString(), practiceDto1);
//
//                GPX route_3_2_gpx = GisUtils.parseGpx(getClass().getResource(FilePaths.ROUTE_3_2).getFile());
//                PracticeDto practiceDto2 = practiceService.add(route_3_2_gpx, "mati");
//                eventService.addPractice(eventDto.getId().toString(), practiceDto2);
//
//                GPX route_3_3_gpx = GisUtils.parseGpx(getClass().getResource(FilePaths.ROUTE_3_3).getFile());
//                PracticeDto practiceDto3 = practiceService.add(route_3_3_gpx, "manu");
//                eventService.addPractice(eventDto.getId().toString(), practiceDto3);
//
//                GPX route_3_4_gpx = GisUtils.parseGpx(getClass().getResource(FilePaths.ROUTE_3_4).getFile());
//                PracticeDto practiceDto4 = practiceService.add(route_3_4_gpx, "vilas");
//                eventService.addPractice(eventDto.getId().toString(), practiceDto4);
//            }
//        }
//        if (eventService.search("Desafio_2").isEmpty()) {
//            EventDto eventDto = new EventDto();
//            UserDto owner = userService.getById("vilas");
//            if (owner != null) {
//                eventDto.setOwner(owner);
//                eventDto.setName("Desafio_2");
//                eventDto.setDescription("");
//                eventDto.setPrivacy(Privacy.PUBLIC);
//                SimpleDateFormat parserSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//                Date d = parserSDF.parse("2014-01-28 16:00");
//                eventDto.setEventDate(d);
//                eventDto.setEventType(EventType.CHALLENGE);
//                eventDto = (EventDto) eventService.add(eventDto);
//
//                UserDto manu = userService.getById("manu");
//                UserDto mati = userService.getById("mati");
//                UserListDto members = new UserListDto();
//                members.add(manu);
//                members.add(mati);
//                eventService.addMembers(eventDto.getId().toString(), members);
//
//                GPX gpx = GisUtils.parseGpx(getClass().getResource(FilePaths.ROUTE_4).getFile());
//                PracticeDto practiceDto = practiceService.add(gpx, "manu");
//                practiceDto.setPrivacy(Privacy.PUBLIC);
//                practiceService.changePrivacy(practiceDto);
//                RouteDto route = practiceService.getRouteById(practiceDto.getId().toString());
//                SegmentListDto segmentList = routeService.getSegments(route.getId());
////            for (SegmentDto s : segmentList.getFeatures()) {
////                if (s.getDistance() > 12900) {
////                    eventService.setSegment(s.getId(), eventDto.getId().toString());
////                }
////            }
//                eventService.setSegment(segmentList.getFeatures().get(0).getId(), eventDto.getId().toString());
//
//                GPX route_4_2_gpx = GisUtils.parseGpx(getClass().getResource(FilePaths.ROUTE_4_1).getFile());
//                PracticeDto practiceDto3 = practiceService.add(route_4_2_gpx, "manu");
//                eventService.addPractice(eventDto.getId().toString(), practiceDto3);
//
//            }
//        }
//        if (eventService.search("Quedada_1").isEmpty()) {
//            EventDto eventDto = new EventDto();
//            UserDto owner = userService.getById("vilas");
//            if (owner != null) {
//                eventDto.setOwner(owner);
//                eventDto.setName("Quedada_1");
//                eventDto.setDescription("C.C.GONDOMAR 20 MAR 2011 COUSO-HOSPITAL-AREAS-LA GUARDIA-BAYONA-BELESAR");
//                eventDto.setPrivacy(Privacy.PUBLIC);
//                SimpleDateFormat parserSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//                Date d = parserSDF.parse("2014-01-31 10:00");
//                eventDto.setEventDate(d);
//                eventDto.setEventType(EventType.HANGOUT);
//                eventDto = (EventDto) eventService.add(eventDto);
//
//                UserDto artur = userService.getById("artur");
//                UserDto mati = userService.getById("mati");
//                UserDto manu = userService.getById("manu");
//                UserListDto members = new UserListDto();
//                members.add(artur);
//                members.add(mati);
//                members.add(manu);
//                eventService.addMembers(eventDto.getId().toString(), members);
//
//                GPX gpx = GisUtils.parseGpx(getClass().getResource(FilePaths.ROUTE_2).getFile());
//                PracticeDto practiceDto = practiceService.add(gpx, "manu");
//                practiceDto.setPrivacy(Privacy.PUBLIC);
//                practiceService.changePrivacy(practiceDto);
//                RouteDto route = practiceService.getRouteById(practiceDto.getId().toString());
//                eventService.setRoute(route.getId(), eventDto.getId().toString());
//            }
//        }
    }

    @Test
    public void fillDb() throws FileNotFoundException, ParserConfigurationException, SAXException, IOException, TransformException, TransformerException {
//        GPX gpx = GisUtils.parseGpx(getClass().getResource(FilePaths.ROUTE_1).getFile());
//        PracticeDto practiceDto = practiceService.add(gpx, "artur");
//        eventService.addPractice("4552", practiceDto);
//        for (String s : routeService.getLast("5")) {
//            System.out.println(s);
//        }
//        for (String s : segmentService.getLast("5")) {
//            System.out.println(s);
//        }
//        Privacy user = groupService.getPrivacy("5044");
//        user = Privacy.PRIVATE;
    }

    @Ignore
    @Test
    public void addEventRoute() throws FileNotFoundException, ParserConfigurationException, SAXException, IOException, TransformException, TransformerException {
        EventDto eventDto = new EventDto();
        eventDto.setOwner(userService.getById("vilas"));
        eventDto.setEventType(EventType.CHALLENGE);
        eventDto = (EventDto) eventService.add(eventDto);
        Assert.assertNotNull(eventDto.getId());

        UserDto artur = userService.getById("artur");
        UserDto mati = userService.getById("mati");
        UserListDto members = new UserListDto();
        members.add(artur);
        members.add(mati);
        eventService.addMembers(eventDto.getId().toString(), members);
        members = eventService.getMembers(eventDto.getId().toString());
        Assert.assertEquals(members.size(), 3);

        GPX gpx = GisUtils.parseGpx(getClass().getResource(FilePaths.ROUTE_2).getFile());
        PracticeDto practiceDto = practiceService.add(gpx, "artur");
        RouteDto route = practiceService.getRouteById(practiceDto.getId().toString());
        eventService.setRoute(route.getId(), eventDto.getId().toString());

        Object ob = eventService.getRoute(eventDto.getId().toString());
        Assert.assertNotNull(ob);
        Assert.assertTrue(ob instanceof RouteDto);

        GPX gpx2 = GisUtils.parseGpx(getClass().getResource(FilePaths.ROUTE_2).getFile());
        PracticeDto practiceDto2 = practiceService.add(gpx2, "mati");
        eventService.addPractice(eventDto.getId().toString(), practiceDto);
        eventService.addPractice(eventDto.getId().toString(), practiceDto2);

        BaseListDto<Object> resultList = eventService.getRouteResults(eventDto.getId().toString());
        for (int i = 0; i < resultList.size(); i++) {
            PracticeDto prdto = (PracticeDto) resultList.get(i);
            System.out.println(i + 1 + "º " + prdto.getOwner().getUsername() + " -> " + prdto.getDuration());
        }
    }

    @Ignore
    @Test
    public void addEventSegment() throws FileNotFoundException, ParserConfigurationException, SAXException, IOException, TransformException, TransformerException {
        EventDto eventDto = new EventDto();
        eventDto.setOwner(userService.getById("vilas"));
        eventDto.setEventType(EventType.CHALLENGE);
        eventDto = (EventDto) eventService.add(eventDto);
        Assert.assertNotNull(eventDto.getId());

        UserDto artur = userService.getById("artur");
        UserDto mati = userService.getById("mati");
        UserListDto members = new UserListDto();
        members.add(artur);
        members.add(mati);
        eventService.addMembers(eventDto.getId().toString(), members);
        members = eventService.getMembers(eventDto.getId().toString());
        Assert.assertEquals(members.size(), 3);

        GPX gpx = GisUtils.parseGpx(getClass().getResource(FilePaths.ROUTE_4).getFile());
        PracticeDto practiceDto = practiceService.add(gpx, "artur");
        RouteDto route = practiceService.getRouteById(practiceDto.getId().toString());
        SegmentListDto segments = routeService.getSegments(route.getId().toString());
//        eventService.setSegment(segments.get(2), eventDto.getId().toString());

        Object ob = eventService.getRoute(eventDto.getId().toString());
        Assert.assertNotNull(ob);
        Assert.assertTrue(ob instanceof SegmentDto);

        GPX gpx2 = GisUtils.parseGpx(getClass().getResource(FilePaths.ROUTE_4_1).getFile());
        PracticeDto practiceDto2 = practiceService.add(gpx2, "mati");
        eventService.addPractice(eventDto.getId().toString(), practiceDto);
        eventService.addPractice(eventDto.getId().toString(), practiceDto2);

        BaseListDto<SegmentPracticeDto> resultList = eventService.getSegmentsResults(eventDto.getId().toString());
        for (int i = 0; i < resultList.size(); i++) {
            SegmentPracticeDto prdto = resultList.get(i);
            UserDto owner = practiceService.getById(prdto.getId().getPracticeId().toString()).getOwner();
            System.out.println(i + 1 + "º " + owner.getUsername() + " -> " + prdto.getDuration());
        }

        eventService.removeMember(eventDto.getId().toString(), "vilas");
        Assert.assertNotNull(eventDto.getOwner());
        eventService.remove(eventDto.getId().toString());
    }
}
