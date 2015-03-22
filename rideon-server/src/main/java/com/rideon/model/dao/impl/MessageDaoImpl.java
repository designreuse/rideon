/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.dao.impl;

import com.rideon.model.dao.MessageDao;
import com.rideon.model.dao.UserDao;
import com.rideon.model.domain.Message;
import com.rideon.model.domain.User;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Fer
 */
@Repository(value = "messageDao")
public class MessageDaoImpl extends BaseDaoImpl<Message> implements MessageDao {

    @Autowired
    UserDao userDao;
    
    @Override
    @Transactional
    public Message getById(Object objectId) {
        Message message = entityManager.find(Message.class, objectId);
        if (message != null){
            User owner = userDao.getById(message.getOwner().getUsername());
            message.setOwner(owner);
        }
        return message;
    }
    
    
}
