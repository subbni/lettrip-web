package com.lettrip.lettripbackend.util;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.SerializationUtils;

import javax.swing.text.html.Option;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

public class CookieUtil {

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie: cookies) {
            if(cookie.getName().equals(name)) {
                return Optional.of(cookie);
            }
        }
        return Optional.empty();
    }

    public static void addCookie(HttpServletResponse response, String key, String value, int maxAge) {
        Cookie cookie = new Cookie(key,value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies  = request.getCookies();
        for(Cookie cookie: cookies) {
            if (cookie.getName().equals(name)) {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
    }

    // 인코딩 => 암호화하기
    public static String serialize(Object object) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(object));
    }
    // 디코딩 => 암호화 풀기

    /**
     * @param cookie HttpServletRequest 로부터 resolve한 쿠키
     * @param cls 반환 타입
     * @return String으로 쿠키의 값을 cls 타입으로 반환
     * @param <T>
     */
    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        if (isDeleted(cookie)) {
            return null;
        }
        return cls.cast(SerializationUtils.deserialize(
                Base64.getUrlDecoder().decode(cookie.getValue())));
    }

    private static boolean isDeleted(Cookie cookie) {
        return StringUtils.isBlank(cookie.getValue())
                || Objects.isNull(cookie.getValue());
    }
}
