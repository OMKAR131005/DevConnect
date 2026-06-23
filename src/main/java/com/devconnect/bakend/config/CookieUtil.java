package com.devconnect.bakend.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {
    @Value("${app.cookie.name}")
    private String cookieName;

    @Value("${app.cookie.max-age}")
    private long maxAge;

    @Value("${app.cookie.secure}")
    private boolean secure;
    public void addHttpOnlyCookies(HttpServletResponse httpServletResponse,String token){
        ResponseCookie responseCookie= ResponseCookie.
                from(cookieName,token)
                .httpOnly(true).path("/").secure(secure).
                maxAge(maxAge).sameSite("Lax")
                .build();
        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE,responseCookie.toString());
    }
    public void deleteHttpOnlyCookies(HttpServletResponse httpServletResponse){
        ResponseCookie responseCookie= ResponseCookie.from(cookieName,"")
                .httpOnly(true).path("/").secure(secure).
                maxAge(0).sameSite("Lax")
                .build();
        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE,responseCookie.toString());
    }
    public String getCookieValue(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
