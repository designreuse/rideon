/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author Fer
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/testContext.xml")
public abstract class BaseTest extends AbstractTransactionalJUnit4SpringContextTests {

}
