/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.model.dao;

/**
 *
 * @author Fer
 */
public interface BaseDao<T> {

    public T add(T object);

    public T getById(Object objectId);

    public void remove(Object objectId);

    public T update(T object);
}
