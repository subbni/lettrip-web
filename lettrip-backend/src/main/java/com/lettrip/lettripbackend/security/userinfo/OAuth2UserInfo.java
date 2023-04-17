package com.lettrip.lettripbackend.security.userinfo;

import com.lettrip.lettripbackend.domain.user.ProviderType;
import com.lettrip.lettripbackend.exception.LettripException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public abstract class OAuth2UserInfo {
    ProviderType providerType;
    protected Map<String,Object> attributes;
    public OAuth2UserInfo(ProviderType providerType, Map<String,Object> attributes) {
        this.providerType = providerType;
        this.attributes = attributes;
    }

    public Map<String,Object> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    public abstract String getId();
    public abstract String getName();
    public abstract String getEmail();
    public abstract String getImageUrl();
    public abstract ProviderType getProviderType();

    public void validateNecessaryFields() {
         if(!(StringUtils.hasText(this.getEmail()) && StringUtils.hasText(this.getId())
                && StringUtils.hasText(this.getName()) && StringUtils.hasText(this.getImageUrl()))
         ) throw new OAuth2AuthenticationException("Invalid Userinfo");
    }

}
