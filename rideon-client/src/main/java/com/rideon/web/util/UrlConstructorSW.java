package com.rideon.web.util;

public interface UrlConstructorSW {

    public String getUrl();

    public UrlConstructorSW addParameter(Object parametro);

    public UrlConstructorSW deleteParameters();

    public UrlConstructorSW deleteLastParameter();
}
