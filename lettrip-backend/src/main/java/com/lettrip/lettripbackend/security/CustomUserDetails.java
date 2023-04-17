package com.lettrip.lettripbackend.security;

import com.lettrip.lettripbackend.domain.user.Role;
import com.lettrip.lettripbackend.domain.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

public class CustomUserDetails implements UserDetails, OAuth2User {

    private Long id;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String,Object> attributes;

    public CustomUserDetails(
            Long id, String email, String password,
            Collection<? extends GrantedAuthority> authorities
    ) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * Form 로그인 시
     */
    public static CustomUserDetails create(User user) {
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(Role.USER.getKey()));
        return new CustomUserDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }

    /**
     * OAuth2 로그인 시
     */

    public static CustomUserDetails create(User user, Map<String,Object> attributes) {
        CustomUserDetails customUserDetails = CustomUserDetails.create(user);
        customUserDetails.setAttributes(attributes);
        return customUserDetails;
    }

    /**
     * UserDetails 구현
     */

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    // PK값 반환
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    // 계정 만료 여부
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠김 여부
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 계정 비밀번호 만료 여부
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //  계정 활성화 여부
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * OAuth2User 구현
     */

    @Override
    public Map<String,Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

}
