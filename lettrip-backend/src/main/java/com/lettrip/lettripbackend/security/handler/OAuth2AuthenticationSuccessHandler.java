package com.lettrip.lettripbackend.security.handler;

import com.lettrip.lettripbackend.configuration.AuthProperties;
import com.lettrip.lettripbackend.exception.LettripErrorCode;
import com.lettrip.lettripbackend.exception.LettripException;
import com.lettrip.lettripbackend.security.HttpCookieOAuth2AuthorizationRequestRepository;
import com.lettrip.lettripbackend.security.jwt.JwtProvider;
import com.lettrip.lettripbackend.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

import static com.lettrip.lettripbackend.security.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URL_PARAM_COOKIE_NAME;

/**
 * OAuth2 인증이 성공적으로 완료된다면 실행된다.
 * 1. request cookie에서 {redirect_url}을 꺼낸다.
 * 2. redirect_url 유효성 검증을을 한다.
 * 2-1. 유효하지 않은 url이라면 UnauthorizedRedirectUrlException을 발생
 * 3. 로그인에 필요한 정보들과 토큰을 발급한다.
 * 4. request와 response에 남아있을 cookie를 다 지운다.
 * 5. {redirect_url}?access_token={}&...로 리다이렉트 시킨다.
 */
@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final AuthProperties authProperties;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        String targetUrl = determineTargetUrl(request,response,authentication);

        if(response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to "+targetUrl);
            return;
        }

        this.clearAuthenticationAttributes(request);
        getRedirectStrategy().sendRedirect(request,response,targetUrl);
    }

    /**
     * 최종 리다이렉션 시킬 Url를 작성
     * @param authentication 인증 완료된 결과
     * @return 인증 결과를 사용해서 access 토큰을 발급하고, 쿠키에 저장되어 있던 redirect_uri(프론트에서 적어준 것)와 합쳐서 반환.
     * 명시되지 않으면 설정파일({@link AuthProperties})에 명시된 default redirect url 값 적용
     */
    @Override
    protected String determineTargetUrl(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        String targetUrl = CookieUtil.getCookie(request, REDIRECT_URL_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse(authProperties.getOAuth2().getDefaultRedirectUri());

        if (notAuthorized(targetUrl)) {
            throw new LettripException(LettripErrorCode.INVALID_REQUEST); //TODO: 수정 !!!! auth 관련 exception 만들어서 관리하기
        }


        String accessToken = jwtProvider.createToken(authentication);
        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("access_token",jwtProvider.createToken(authentication))
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(
            HttpServletRequest request, HttpServletResponse response
    ) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.clearCookies(request,response);
    }

    private boolean notAuthorized(String redirectUrl) {
        return !redirectUrl.isBlank() &&
                !authProperties.getOAuth2().isAuthorizedRedirectUri(redirectUrl);
    }
}


