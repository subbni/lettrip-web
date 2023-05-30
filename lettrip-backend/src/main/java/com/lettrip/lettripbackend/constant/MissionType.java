package com.lettrip.lettripbackend.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MissionType {
    QR_MISSION("QR 인증 미션"),
    RANDOM_MISSION("랜덤 장소 방문 미션");
    private final String description;

}
