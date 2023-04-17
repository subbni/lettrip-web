package com.lettrip.lettripbackend.security;

import com.lettrip.lettripbackend.util.CookieUtil;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

/**
 * 로그인 과정 종료 후에 프론트로 리다이렉션 필요 -> 프론트 최초 요청 파라미터 (redirect_url)로 콜백 주소를 지정해주기 위함.
 * OAuth2 Authorization Code Grant 과정에서 리다이렉션이 발생하므로 요청 파라미터를 브라우저 쿠키에 저장해야 함.
 * -> OAuth2AuthorizationRequest를 쿠키에 저장.
 * @see org.springframework.security.oauth2.client.web.AuthorizationRequestRepository
 */
@Component
public class HttpCookieOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    public static final String REDIRECT_URL_PARAM_COOKIE_NAME = "redirect_uri";
    private static final int cookieExpireSeconds = 180;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return CookieUtil.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
                .map(cookie-> CookieUtil.deserialize(cookie, OAuth2AuthorizationRequest.class))
                .orElse(null);
    }

    /**
     * `OAuth2AuthorizationRequest`는 OAuth2.0 표준 프로토콜에 따라서 api <-> provider 간 통신 할 때 SpringSecurity가 사용하는 객체이다.
     *  -> 이 객체는 OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME 이라는 key 값으로 따로 구분하여 저장
     *  {redirect_uri} 값은 프론트엔드에서 보내준 값이므로, -> REDIRECT_URL_PARAM_COOKIE_NAME 이라는 key 값으로 따로 구분하여 저장한다.
     */
    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            CookieUtil.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
            CookieUtil.deleteCookie(request, response, REDIRECT_URL_PARAM_COOKIE_NAME);
            return;
        }
        CookieUtil.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, CookieUtil.serialize(authorizationRequest), cookieExpireSeconds);
        String redirectUriAfterLogin = request.getParameter(REDIRECT_URL_PARAM_COOKIE_NAME);
        if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
            CookieUtil.addCookie(response, REDIRECT_URL_PARAM_COOKIE_NAME, redirectUriAfterLogin, cookieExpireSeconds);
        }
    }



    /**
     * OAuth2AuthorizationRequest 를 쿠키에서 제거
     * @return OAuth2AuthorizationRequest
     */
    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response){
        OAuth2AuthorizationRequest authorizationRequest = this.loadAuthorizationRequest(request);
        CookieUtil.deleteCookie(request,response,OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);

        return authorizationRequest;
    }

    /**
     * redirect_uri가 담긴 쿠키는 인증이 완전히 완료된 후에 제거되어야 한다.
     */
    public void clearCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        CookieUtil.deleteCookie(request, response, REDIRECT_URL_PARAM_COOKIE_NAME);
    }


}
