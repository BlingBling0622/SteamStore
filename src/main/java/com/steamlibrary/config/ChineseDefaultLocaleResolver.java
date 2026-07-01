package com.steamlibrary.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import java.util.Locale;

/**
 * Locale resolver that defaults to Simplified Chinese.
 *
 * Ignores the browser Accept-Language header so the UI is always presented in
 * Chinese unless the user has explicitly chosen another language via the
 * ?lang=... switcher (which is persisted in the locale cookie).
 */
public class ChineseDefaultLocaleResolver extends CookieLocaleResolver {

    public ChineseDefaultLocaleResolver() {
        super();
        setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
    }

    @Override
    protected Locale determineDefaultLocale(HttpServletRequest request) {
        // Always default to Simplified Chinese, ignore Accept-Language header
        return Locale.SIMPLIFIED_CHINESE;
    }
}
