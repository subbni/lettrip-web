package com.lettrip.lettripbackend.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix="auth")
public class AuthProperties {
    private final OAuth2 oauth = new OAuth2();

    public static final class OAuth2 {

        private String defaultRedirectUri;
        private List<String> authorizedRedirectUris = new ArrayList<>();

        public void setDefaultRedirectUri(String defaultRedirectUri) {
            this.defaultRedirectUri = defaultRedirectUri;
        }
        public String getDefaultRedirectUri() {
            return defaultRedirectUri;
        }
        public List<String> getAuthorizedRedirectUris() {
            return authorizedRedirectUris;
        }

        public OAuth2 setAuthorizedRedirectUris(List<String> authorizedRedirectUris) {
            this.authorizedRedirectUris = authorizedRedirectUris;
            return this;
        }

        /**
         * OAuth2 인증 요청 시 redirect_uri 를 변조하려는 불법적인 시도를 막기 위한 보안적인 조치
         * @param uri 프론트에서 입력한 redirect_uri
         * @return api 에 등록되어 있는 uri 이면 true.
         */
        public boolean isAuthorizedRedirectUri(String uri) {
            URI clientRedirectUri = URI.create(uri);
            return this.authorizedRedirectUris
                    .stream()
                    .anyMatch(authorizedRedirectUri-> {
                        // host, port만 비교함. yml에 설정해 놓은 리다이렉션 url와 일치하는지 확인
                        URI authroizedURI = URI.create(authorizedRedirectUri);
                        return authroizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                                && authroizedURI.getPort() == clientRedirectUri.getPort();
                    });
        }

    }
    public OAuth2 getOAuth2() {
        return oauth;
    }


}
