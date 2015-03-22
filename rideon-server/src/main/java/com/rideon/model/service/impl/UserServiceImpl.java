/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.service.impl;

import com.rideon.common.enums.UserRole;
import com.rideon.model.dao.MultimediaDao;
import com.rideon.model.dao.UserDao;
import com.rideon.model.dto.BicycleDto;
import com.rideon.model.dto.MultimediaDto;
import com.rideon.model.dto.NotificationDto;
import com.rideon.model.dto.SignFormDto;
import com.rideon.model.dto.UserDto;
import com.rideon.model.domain.Bicycle;
import com.rideon.model.domain.User;
import com.rideon.model.service.UserService;
import java.util.List;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rideon.model.dto.list.BaseListDto;
import com.rideon.model.domain.Multimedia;
import com.rideon.util.Constants;
import com.rideon.util.FilePaths;
import com.rideon.util.IOHelper;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Fer
 */
@Service(value = "userService")
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    UserDao userDao;
    @Autowired
    MultimediaDao multimediaDao;
    @Autowired
    MapperFacade mapper;

    @Override
    public UserDto getById(String id) {
        User user = userDao.getById(id);
        if (user != null) {
            return mapper.map(user, UserDto.class);
        }
        return null;
    }

    @Override
    public void remove(String id) {
        userDao.remove(id);
    }

    @Override
    public UserDto add(SignFormDto form) {
        User user = mapper.map(form, User.class);
        user.setUserRole(UserRole.USER);
        userDao.add(user);

        String url = getClass().getResource(FilePaths.USER_IMAGE).getFile();
        try {
            Multimedia image = IOHelper.readMultimediaFromFile(url);
            userDao.setImage(user.getUsername(), image);
            Multimedia thumbnail = IOHelper.resizeImage(image, Constants.THUMBNAIL_SIZE_PX);
            userDao.setThumbnail(user.getUsername(), thumbnail);
        } catch (IOException e) {
            LOGGER.error("", e);
        }

        return mapper.map(user, UserDto.class);
    }

    @Override
    public UserDto update(UserDto userDto) {
        User user = mapper.map(userDto, User.class);
        userDao.update(user);
        return mapper.map(user, UserDto.class);
    }

    @Override
    public MultimediaDto setImage(String userId, MultimediaDto image) {
        Multimedia img = mapper.map(image, Multimedia.class);
        userDao.setImage(userId, img);
        try {
            Multimedia thumbnail = IOHelper.resizeImage(img, Constants.THUMBNAIL_SIZE_PX);
            userDao.setThumbnail(userId, thumbnail);
        } catch (IOException ex) {
            LOGGER.error("", ex);
        }
        return image;
    }

    @Override
    public MultimediaDto getImage(String userId) {
        Multimedia img = userDao.getImage(userId);
        return mapper.map(img, MultimediaDto.class);
    }

    @Override
    public BicycleDto addBike(String userId, BicycleDto bikeDto) {
        Bicycle bike = mapper.map(bikeDto, Bicycle.class);
        bike = userDao.addBicycle(userId, bike);

        String url = getClass().getResource(FilePaths.BICYCLE_IMAGE).getFile();
        try {
            Multimedia image = IOHelper.readMultimediaFromFile(url);
            image = multimediaDao.add(image);
            userDao.setBycicleImage(userId, bike.getId(), image);
        } catch (IOException e) {
            LOGGER.error("", e);
        }

        return mapper.map(bike, BicycleDto.class);
    }

    @Override
    public void removeBike(String userId, String bikeId) {
        userDao.removeBicycle(userId, Long.valueOf(bikeId));
    }

    @Override
    public BicycleDto updateBike(String userId, BicycleDto bikeDto) {
        Bicycle bike = mapper.map(bikeDto, Bicycle.class);
        bike = userDao.updateBicycle(userId, bike);
        return mapper.map(bike, BicycleDto.class);
    }

    @Override
    public BaseListDto<BicycleDto> getAllBikes(String userId) {
        List<Bicycle> bikeList = userDao.getBycicles(userId);

        BaseListDto<BicycleDto> list = new BaseListDto<>();
        List<BicycleDto> data = list.getData();

        if (bikeList != null) {
            for (Bicycle bike : bikeList) {
                data.add(mapper.map(bike, BicycleDto.class));
            }
        }

        return list;
    }

    @Override
    public BicycleDto getBike(String userId, String bikeId) {
        Bicycle bike = userDao.getBycicle(userId, Long.valueOf(bikeId));
        if (bike != null) {
            return mapper.map(bike, BicycleDto.class);
        }
        return null;
    }

    @Override
    public MultimediaDto getBikeImage(String userId, String bikeId) {
        Multimedia img = userDao.getBycicleImage(userId, Long.valueOf(bikeId));
        if (img != null) {
            return mapper.map(img, MultimediaDto.class);
        }
        return null;
    }

    @Override
    public MultimediaDto setBikeImage(String userId, String bikeId, MultimediaDto image) {
        Multimedia img = mapper.map(image, Multimedia.class);
        img = userDao.setBycicleImage(userId, Long.valueOf(bikeId), img);
        return mapper.map(img, MultimediaDto.class);
    }

    @Override
    public BaseListDto<UserDto> getFriends(String userId) {
        List<User> userList = userDao.getFriends(userId);
        BaseListDto<UserDto> userDtolist = new BaseListDto<>();
        for (User user : userList) {
            userDtolist.add(mapper.map(user, UserDto.class));
        }
        return userDtolist;
    }

    @Override
    public MultimediaDto getThumbnail(String userId) {
        Multimedia m = userDao.getThumbnail(userId);
        return mapper.map(m, MultimediaDto.class);
    }

//    @Override
//    public BaseListDto<UserDto> addFriend(FriendshipRequestDto request) {
//        List<User> userList = userDao.addFriend(userId, targetId);
//        BaseListDto<UserDto> userDtolist = new BaseListDto<>();
//        for (User user : userList) {
//            userDtolist.add(mapper.map(user, UserDto.class));
//        }
//        return new BaseListDto<>();
//    }
    @Override
    public BaseListDto<UserDto> search(String query) {
        BaseListDto<UserDto> userDtolist = new BaseListDto<>();
        if (!query.isEmpty()) {
            String[] querys = query.split(" ");
            List<User> userList = userDao.search(querys);
            for (User user : userList) {
                userDtolist.add(mapper.map(user, UserDto.class));
            }
        }
        return userDtolist;
    }

    @Override
    public BaseListDto<NotificationDto> getNotifications(String userId) {
        List<NotificationDto> list = userDao.getNotifications(userId);
        BaseListDto<NotificationDto> nDtoList = new BaseListDto<>();
        nDtoList.setData(list);
        return nDtoList;
    }

    @Override
    public void removeFriend(String userId, String friendId) {
        userDao.removeFriend(userId, friendId);
    }
}
