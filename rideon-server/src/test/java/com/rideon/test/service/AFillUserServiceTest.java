/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.test.service;

import com.rideon.model.dao.RouteDao;
import com.rideon.model.dao.SegmentDao;
import com.rideon.model.domain.Multimedia;
import com.rideon.model.dto.BicycleDto;
import com.rideon.model.dto.FriendshipRequestDto;
import com.rideon.test.BaseTest;
import com.rideon.model.dto.SignFormDto;
import com.rideon.model.dto.UserDto;
import com.rideon.model.service.FriendshipRequestService;
import com.rideon.model.service.UserService;
import com.rideon.util.FilePaths;
import com.rideon.util.IOHelper;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.codec.binary.Hex;
import org.springframework.test.context.transaction.BeforeTransaction;

/**
 *
 * @author Fer
 */
public class AFillUserServiceTest extends BaseTest {

    @Autowired
    UserService userService;
    @Autowired
    FriendshipRequestService friendshipRequestService;
    @Autowired
    RouteDao routeDao;
    @Autowired
    SegmentDao segmentDao;
    Logger LOGGER = LoggerFactory.getLogger(AFillUserServiceTest.class);
    private static SimpleDateFormat parserSDF = new SimpleDateFormat("yyyy-MM-dd");
    private static final String PASS = "rideon123";

    @BeforeTransaction
    public void fillUserDb() throws ParseException, NoSuchAlgorithmException, UnsupportedEncodingException, IOException {

        addUsers();
        addBikes();
        addFriends();
    }

    private void addUsers() throws ParseException, NoSuchAlgorithmException, UnsupportedEncodingException, IOException {

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] passBytes = PASS.getBytes("UTF-8");
        passBytes = md.digest(passBytes);
        BigInteger bigInt = new BigInteger(1, passBytes);
        String pass = Hex.encodeHexString(passBytes);

        SignFormDto from;
        Set<SignFormDto> list = new HashSet<>();

        from = new SignFormDto();
        from.setUsername("admin");
        from.setPassword(pass);
        from.setEmail("admin@rideon.com");
        from.setFullName("Admin");
        list.add(from);

        from = new SignFormDto();
        from.setUsername("vilas");
        from.setPassword(pass);
        from.setEmail("vilasmaciel@gmail.com");
        from.setFullName("Fernando Vilas");
        list.add(from);

        from = new SignFormDto();
        from.setUsername("artur");
        from.setPassword(pass);
        from.setEmail("user@user.com");
        from.setFullName("Arturo Costa");
        list.add(from);

        from = new SignFormDto();
        from.setUsername("mati");
        from.setPassword(pass);
        from.setEmail("user@user.com");
        from.setFullName("Mati Shuster");
        list.add(from);

        from = new SignFormDto();
        from.setUsername("manu");
        from.setPassword(pass);
        from.setEmail("user@user.com");
        from.setFullName("Manuel Vilas");
        list.add(from);

        for (SignFormDto u : list) {
            if (userService.getById(u.getUsername()) == null) {
                userService.add(u);
            } else {
                UserDto userDto = new UserDto();
                userDto.setUsername(u.getUsername());
                userDto.setEmail(u.getEmail());
                userDto.setFullName(u.getFullName());
                userService.update(userDto);
            }
        }


        Date d;
        UserDto vilas = userService.getById("vilas");
        vilas.setProvince("Pontevedra");
        vilas.setTown("Nigrán");
        vilas.setCountry("España");
        d = parserSDF.parse("1986-09-26");
        vilas.setBornDate(d);
        userService.update(vilas);

        UserDto artur = userService.getById("artur");
        artur.setProvince("Lugo");
        artur.setTown("Rabade");
        d = parserSDF.parse("1990-01-16");
        artur.setBornDate(d);
        userService.update(artur);
        String url = getClass().getResource(FilePaths.USER_2_IMAGE).getFile();
        userService.setImage("artur", IOHelper.readMultimediaDtoFromFile(url));

        UserDto mati = userService.getById("mati");
        mati.setProvince("Ourense");
        mati.setTown("Celanova");
        d = parserSDF.parse("1980-12-20");
        mati.setBornDate(d);
        userService.update(mati);
        url = getClass().getResource(FilePaths.USER_3_IMAGE).getFile();
        userService.setImage("mati", IOHelper.readMultimediaDtoFromFile(url));

        UserDto manu = userService.getById("manu");
        manu.setProvince("Pontevedra");
        manu.setTown("Vigo");
        d = parserSDF.parse("1985-05-03");
        manu.setBornDate(d);
        userService.update(manu);
        url = getClass().getResource(FilePaths.USER_4_IMAGE).getFile();
        userService.setImage("manu", IOHelper.readMultimediaDtoFromFile(url));
    }

    private void addBikes() throws ParseException {
        BicycleDto principal = new BicycleDto();
        principal.setBrand("Conor");
        principal.setModel("SH340");
        principal.setIsPrincipal(Boolean.TRUE);
        principal.setKilometers(0d);
        Date d = parserSDF.parse("2012-12-25");
        principal.setBuyDate(d);

        BicycleDto bike = new BicycleDto();
        bike.setBrand("Peugeot");
        bike.setModel("4000");
        d = parserSDF.parse("2010-09-06");
        bike.setBuyDate(d);
        bike.setIsPrincipal(Boolean.FALSE);
        bike.setKilometers(0d);

        if (userService.getAllBikes("vilas").isEmpty()) {
            userService.addBike("vilas", principal);
            userService.addBike("vilas", bike);
        }
        if (userService.getAllBikes("artur").isEmpty()) {
            userService.addBike("artur", principal);
            userService.addBike("artur", bike);
        }
        if (userService.getAllBikes("mati").isEmpty()) {
            userService.addBike("mati", principal);
            userService.addBike("mati", bike);
        }
        if (userService.getAllBikes("manu").isEmpty()) {
            userService.addBike("manu", principal);
            userService.addBike("manu", bike);
        }
    }

    private void addFriends() {
        UserDto vilas = new UserDto();
        vilas.setUsername("vilas");
        UserDto artur = new UserDto();
        artur.setUsername("artur");
        UserDto mati = new UserDto();
        mati.setUsername("mati");
        UserDto manu = new UserDto();
        manu.setUsername("manu");

        FriendshipRequestDto fsh = new FriendshipRequestDto();
        fsh.setTarget(vilas);
        fsh.setPetitioner(artur);
        if (userService.getFriends("vilas").isEmpty()) {
            friendshipRequestService.add(fsh);
            fsh.setPetitioner(mati);
            friendshipRequestService.add(fsh);
            fsh.setPetitioner(manu);
            friendshipRequestService.add(fsh);
        }
    }

    @Test
    public void userTest() throws Exception {
    }
}
