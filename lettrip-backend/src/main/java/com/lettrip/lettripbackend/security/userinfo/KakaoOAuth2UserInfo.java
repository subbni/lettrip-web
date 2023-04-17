package com.lettrip.lettripbackend.security.userinfo;

import com.lettrip.lettripbackend.domain.user.ProviderType;

import java.util.Map;
import java.util.Objects;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "nickname";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_IMAGE_URL = "profile_image_url";
    private static final String KEY_EXTRA_DATA = "kakao_account";

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(ProviderType.KAKAO,attributes);
    }

    @Override
    public String getId() {

        return Objects.toString(attributes.get(KEY_ID));
    }

    @Override
    public String getName() {

        return (String) this.getProfile().get(KEY_NAME);
    }

    @Override
    public String getEmail() {

        return (String) this.getExtraData().get(KEY_EMAIL);
    }

    @Override
    public String getImageUrl() {

        return (String) this.getProfile().get(KEY_IMAGE_URL);
    }

    @Override
    public ProviderType getProviderType() {
        return this.providerType;
    }

    @SuppressWarnings({"unchecked"})
    public Map<String,Object> getExtraData() {
        return (Map<String,Object>) attributes.get(KEY_EXTRA_DATA);
    }

    @SuppressWarnings({"unchecked"})
    private Map<String,Object> getProfile() {
        return (Map<String,Object>) this.getExtraData().get("profile");
    }

}
