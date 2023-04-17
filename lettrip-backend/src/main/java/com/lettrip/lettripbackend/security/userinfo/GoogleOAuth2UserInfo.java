package com.lettrip.lettripbackend.security.userinfo;

import com.lettrip.lettripbackend.domain.user.ProviderType;

import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {
    private static final String KEY_ID = "sub";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_IMAGE_URL = "picture";
    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(ProviderType.GOOGLE,attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get(KEY_ID);
    }

    @Override
    public String getName() {
        return (String) attributes.get(KEY_NAME);
    }

    @Override
    public String getEmail() {
        return (String) attributes.get(KEY_EMAIL);
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get(KEY_IMAGE_URL);
    }

    @Override
    public ProviderType getProviderType() {
        return this.providerType;
    }
}
