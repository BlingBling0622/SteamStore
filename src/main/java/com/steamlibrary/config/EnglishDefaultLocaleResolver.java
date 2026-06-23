package com.steamlibrary.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import java.util.Locale;

public class EnglishDefaultLocaleResolver extends CookieLocaleResolver {

    public EnglishDefaultLocaleResolver() {
        super();
        setDefaultLocale(Locale.ENGLISH);
    }

    @Override
    protected Locale determineDefaultLocale(HttpServletRequest request) {
        // Always return English, ignore Accept-Language header
        return Locale.ENGLISH;
    }
}
