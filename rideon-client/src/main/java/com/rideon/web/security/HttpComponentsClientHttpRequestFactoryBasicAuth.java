/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.web.security;

import java.net.URI;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.client.AuthCache;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

/**
 *
 * @author Fer
 */
public class HttpComponentsClientHttpRequestFactoryBasicAuth extends HttpComponentsClientHttpRequestFactory {

    HttpHost host;

    public HttpComponentsClientHttpRequestFactoryBasicAuth(HttpHost host) {
        super();
        this.host = host;
    }

    @Override
    protected HttpContext createHttpContext(HttpMethod httpMethod, URI uri) {
        return createHttpContext();
    }

    private HttpContext createHttpContext() {
        // Create AuthCache instance
        AuthCache authCache = new BasicAuthCache();
        // Generate BASIC scheme object and add it to the local auth cache
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(host, basicAuth);

        // Add AuthCache to the execution context
        BasicHttpContext localcontext = new BasicHttpContext();
        localcontext.setAttribute(ClientContext.AUTH_CACHE, authCache);
        return localcontext;
    }

    public AuthScope getAuthScope() {
        return new AuthScope(host.getHostName(), host.getPort(), AuthScope.ANY_REALM);
    }
}
