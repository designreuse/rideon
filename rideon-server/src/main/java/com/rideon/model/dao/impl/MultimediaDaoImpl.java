/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.dao.impl;

import com.rideon.model.dao.MultimediaDao;
import com.rideon.model.domain.Multimedia;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Fer
 */
@Repository(value = "multimediaDao")
public class MultimediaDaoImpl extends BaseDaoImpl<Multimedia> implements MultimediaDao {
}
