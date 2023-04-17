package com.lettrip.lettripbackend.security.userinfo;

import com.lettrip.lettripbackend.domain.user.ProviderType;

import java.security.Provider;
import java.util.Map;

public class NaverOAuth2UserInfo extends OAuth2UserInfo {
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_IMAGE_URL = "profile_image";
    private static final String KEY_EXTRA_DATA = "response";

    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        super(ProviderType.NAVER,attributes);
    }

    @Override
    public String getId() {
        return (String) this.getExtraData().get(KEY_ID);
    }

    @Override
    public String getName() {
        return (String) this.getExtraData().get(KEY_NAME);
    }

    @Override
    public String getEmail() {
        return (String) this.getExtraData().get(KEY_EMAIL);
    }

    @Override
    public String getImageUrl() {
        return (String) this.getExtraData().get(KEY_IMAGE_URL);
    }

    @Override
    public ProviderType getProviderType() {
        return this.providerType;
    }

    @SuppressWarnings({"unchecked"})
    public Map<String, Object> getExtraData() {
        return (Map<String, Object>) this.attributes.get(KEY_EXTRA_DATA);
    }
}
