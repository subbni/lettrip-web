package com.lettrip.lettripbackend.security;

import com.lettrip.lettripbackend.domain.user.ProviderType;
import com.lettrip.lettripbackend.domain.user.Role;
import com.lettrip.lettripbackend.domain.user.User;
import com.lettrip.lettripbackend.repository.UserRepository;
import com.lettrip.lettripbackend.security.userinfo.OAuth2UserInfo;
import com.lettrip.lettripbackend.security.userinfo.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 1. OAuth2 인증 결과를 이용해서 사용자 정보를 정제, 추출한다.
 * 2. 로그인 및 회원가입에 필요한 필수 값이 다 들어있는지 검사한다.
 * 3. 첫 로그인 -> 해당 소셜 계정을 DB에 저장한다.
 * 4. 재 로그인 -> 해당 소셜 계정의 변경사항을 DB에 업데이트한다.
 */
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        return loginProcess(userRequest,oAuth2User);
    }

    private OAuth2User loginProcess(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                userRequest.getClientRegistration().getRegistrationId(),
                oAuth2User.getAttributes()
        );

        userInfo.validateNecessaryFields();
        User user = userRepository.findByEmail(userInfo.getEmail()).orElse(null);

        if(user!=null) {
            // 이미 회원가입 된 이메일
            checkProviderTypeMatch(user,userInfo.getProviderType());
            user = updateUser(user,userInfo);
        } else {
            user = signUpUser(userInfo);
        }
        return CustomUserDetails.create(user,oAuth2User.getAttributes());
    }

    // 같은 이메일, 다른 Provider로 로그인하지 못 하도록 한다.
    private void checkProviderTypeMatch(User user, ProviderType providerType) {
        if(user.getProviderType() != providerType) {
            throw new OAuth2AuthenticationException(new OAuth2Error("중복 이메일"),"duplicated email, please try with "
                    + user.getProviderType());
        }
    }

    @Transactional
    protected User signUpUser(OAuth2UserInfo userInfo) {
        User savedUser = userRepository.save(
                User.builder()
                        .email(userInfo.getEmail())
                        .name(userInfo.getName())
                        .nickname(userInfo.getName())
                        .role(Role.USER)
                        .imageUrl(userInfo.getImageUrl())
                        .providerType(userInfo.getProviderType())
                        .build()
        );
        return savedUser;
    }

    @Transactional
    protected User updateUser(User user,OAuth2UserInfo userInfo) {
        if(!user.getName().equals(userInfo.getName())) {
            user.setName(userInfo.getName());
        }
        if(!user.getImageUrl().equals(userInfo.getImageUrl())) {
            user.setImageUrl(userInfo.getImageUrl());
        }
        return user;
    }
}
