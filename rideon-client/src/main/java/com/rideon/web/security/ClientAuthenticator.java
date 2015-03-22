package com.rideon.web.security;

import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.web.client.RestTemplate;

public class ClientAuthenticator {

    public ClientAuthenticator() {
        super();
    }

    // API
    public static void setAuthentication(final RestTemplate restTemplate, final String username, final String password) {
        basicAuth(restTemplate, username, password);
    }

    private static void basicAuth(final RestTemplate restTemplate, final String username, final String password) {

        final HttpComponentsClientHttpRequestFactoryBasicAuth requestFactory = 
                ((HttpComponentsClientHttpRequestFactoryBasicAuth) restTemplate.getRequestFactory());
        
        DefaultHttpClient httpClient = (DefaultHttpClient) requestFactory.getHttpClient();
        CredentialsProvider prov = httpClient.getCredentialsProvider();
        prov.setCredentials(requestFactory.getAuthScope(), new UsernamePasswordCredentials(username, password));
    }
}
