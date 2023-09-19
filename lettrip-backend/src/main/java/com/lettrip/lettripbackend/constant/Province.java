package com.lettrip.lettripbackend.constant;

import com.lettrip.lettripbackend.exception.LettripErrorCode;
import com.lettrip.lettripbackend.exception.LettripException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum Province {
    SEOUL("서울특별시", List.of("서울")),
    BUSAN("부산광역시", List.of("부산")),
    DAEGU("대구광역시", List.of("대구")),
    INCHEON("인천광역시", List.of("인천")),
    GWANGJU("광주광역시", List.of("광주")),
    DAEJEON("대전광역시", List.of("대전")),
    ULSAN("울산광역시", List.of("울산")),
    SEJONG("세종특별자치시", List.of("세종")),
    GYEONGGI("경기도",City.GYEONGGI_CITY),
    GANGWON("강원도",City.GANGWON_CITY),
    CHUNGCHEONGBUKDO("충청북도",City.CHUNGCHEONG_BUKDO_CITY),
    CHUNGCHEONGNAMDO("충청남도",City.CHUNGCHEONG_NAMDO_CITY),
    // 충청남도와 충청북도로 구분
    JEOLLABUKDO("전라도",City.JEOLLA_BUKDO_CITY),
    JEOLLANAMDO("전라도",City.JEOLLA_NAMDO_CITY),
    // 전라남도와 전라북도로 구분
    GYEONGSANGBUKDO("경상북도",City.GYEONGSANG_BUKDO_CITY),
    GYEONGSANGNAMDO("경상남도",City.GYEONGSANG_NAMDO_CITY),
    // 경상남도와 경상북도로 구분
    JEJU("제주특별자치도",City.JEJU_CITY);


    private final String koreanName;
    private final List<String> cities;

    private static final Map<String, String> KOREAN_NAME_MAP =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(Province::getKoreanName, Province::name)
            ));

    public static String getCityInProvince(Province province, String city) {
        if(province.cities.contains(city)){
            return city;
        }
        else{
            throw new LettripException(LettripErrorCode.INVALID_CITY_NAME);
        }
    }

    public static Province of(String KoreanName) {
        return Province.valueOf(KOREAN_NAME_MAP.get(KoreanName));
    }
}
