package com.lettrip.lettripbackend.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER("ROLE_USER", "일반 사용자"),
    NOUSER("ROLE_NO_USER","탈퇴한 사용자"),
    ADMIN("ROLE_ADMIN","관리자");

    private final String key;
    private final String description;

}
