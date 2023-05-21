package com.lettrip.lettripbackend.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum TravelTheme {
    CULTURAL("문화 여행"),
    NATURE("자연 여행"),
    FOOD("식도락 여행"),
    ACTIVITY("액티비티 여행"),
    RELIGIOUS("종교 여행"),
    LEISURE("휴양 여행"),
    SHOPPING("쇼핑 여행");
    private final String KoreanName;

    private static final Map<String, String> KOREAN_NAME_MAP =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(TravelTheme::getKoreanName, TravelTheme::name)
                    ));

    public static TravelTheme of(String KoreanName) {
        return TravelTheme.valueOf(KOREAN_NAME_MAP.get(KoreanName));
    }
}