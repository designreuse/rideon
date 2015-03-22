/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.web.security;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpHost;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Fer
 */
public class RestTemplateFactory implements FactoryBean<RestTemplate>, InitializingBean {

    private RestTemplate restTemplate;
    @Value("${service.configuration.host}")
    private String host;
    @Value("${service.configuration.port}")
    private int port;
    private List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();

    @Override
    public RestTemplate getObject() throws Exception {
        return restTemplate;
    }

    @Override
    public Class<?> getObjectType() {
        return RestTemplate.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        HttpHost h = new HttpHost(host, port);
        HttpComponentsClientHttpRequestFactoryBasicAuth requestFactory = new HttpComponentsClientHttpRequestFactoryBasicAuth(h);

        restTemplate = new RestTemplate((ClientHttpRequestFactory) requestFactory);
        restTemplate.setMessageConverters(messageConverters);
    }

    public List<HttpMessageConverter<?>> getMessageConverters() {
        return messageConverters;
    }

    public void setMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
        this.messageConverters = messageConverters;
    }
}
