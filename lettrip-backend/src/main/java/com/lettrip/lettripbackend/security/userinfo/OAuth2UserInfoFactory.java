package com.lettrip.lettripbackend.security.userinfo;

import com.lettrip.lettripbackend.domain.user.ProviderType;

import java.util.Locale;
import java.util.Map;

public interface OAuth2UserInfoFactory {
    static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String,Object> attributes) {
        ProviderType providerType = ProviderType.valueOf(registrationId.toUpperCase());
        return switch (providerType) {
            case GOOGLE -> new GoogleOAuth2UserInfo(attributes);
            case NAVER -> new NaverOAuth2UserInfo(attributes);
            case KAKAO -> new KakaoOAuth2UserInfo(attributes);
            default -> throw new IllegalArgumentException("Invalid Provider Type.");
        };
    }
}
