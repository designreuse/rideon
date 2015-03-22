/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.dao.impl;

import com.rideon.model.dao.BaseDao;
import java.lang.reflect.ParameterizedType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Fer
 */
public class BaseDaoImpl<T> implements BaseDao<T> {

    @PersistenceContext
    protected EntityManager entityManager;
    private Class<T> persistentClass;

    public BaseDaoImpl() {
        Class<?> clazz = getClass();
        while (!(clazz.getGenericSuperclass() instanceof ParameterizedType)) {
            clazz = clazz.getSuperclass();
        }
        this.persistentClass = ((Class) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    @Override
    @Transactional
    public T add(T object) {
        entityManager.persist(object);
        return object;
    }

    @Override
    @Transactional(readOnly = true)
    public T getById(Object objectId) {
        return entityManager.find(persistentClass, objectId);
    }

    @Override
    @Transactional
    public void remove(Object objectId) {
        T object = getById(objectId);
        if (object != null) {
            entityManager.remove(object);
        }
    }

    @Override
    @Transactional
    public T update(T object) {
        entityManager.merge(object);
        return object;
    }

//    @Override
//    public void createIndex(String query) {
//        Query q = entityManager.createNativeQuery(query);
//        q.getResultList();
//    }
}
