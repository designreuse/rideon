package com.rideon.web.util;

public class UrlConstructorSWImpl implements UrlConstructorSW {

    private static final String pathSerparador = "/";
    private String url;
    private String urlbase;

    public UrlConstructorSWImpl(String urlBaseSW) {
        this.url = urlBaseSW;
        this.urlbase = urlBaseSW;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public UrlConstructorSW addParameter(Object parametro) {
        url = url + pathSerparador + parametro.toString();
        return this;
    }

    @Override
    public UrlConstructorSW deleteParameters() {
        url = urlbase;
        return this;
    }

    @Override
    public UrlConstructorSW deleteLastParameter() {
        url = url.substring(0, url.lastIndexOf(pathSerparador));
        return this;
    }

    @Override
    public String toString() {
        return getUrl();
    }
}
