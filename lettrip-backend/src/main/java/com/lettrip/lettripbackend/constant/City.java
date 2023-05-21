package com.lettrip.lettripbackend.constant;

import lombok.Getter;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@Getter
public class City {
    static final List<String> GYEONGGI_CITY = Arrays.asList(
            "수원","안양","안산","용인","부천","광명",
            "평택","과천","오산","시흥","군포","의왕",
            "하남","이천","안성","김포","화성","광주",
            "여주","양평","고양","의정부","동두천","구리",
            "남양주","파주","양주","포천","연천","가평"
    );

    static final List<String> GANGWON_CITY = Arrays.asList(
            "춘천","원주","강릉","동해","태백",
            "속초","삼척","홍천","횡성","영월",
            "평창","정선","철원","화천","양구",
            "인제","고성","양양"
    );

    static final List<String> CHUNGCHEONG_CITY = Arrays.asList(
            "청주","충주","제천","보은","옥천",
            "영동","증평","진천","괴산","음성",
            "단양","천안","공주","보령","아산",
            "서산","논산","계룡","당진","금산",
            "부여","서천","청양","홍성","예산","태안"
    );

    static final List<String> JEOLLA_CITY = Arrays.asList(
            "전주","군산","익산","정읍","남원"
            ,"김제","완주","진안","무주","장수",
            "임실","순창","고창","부안","목포",
            "여수","순천","나주","광양","담양",
            "곡성","구례","고흥","보성","화순",
            "장흥","강진","해남","영암","무안",
            "함평","영광","장성","완도","진도","신안"
    );

    static final List<String> GYEONGSANG_CITY = Arrays.asList(
            "포항","경주","김천","안동","구미",
            "영주","영천","상주","문경","경산",
            "군위","의성","청송","영양","영덕",
            "청도","고령","성주","칠곡","예천",
            "봉화","울진","울릉","창원","진주",
            "통영","사천","김해","밀양","거제",
            "양산","의령","함안","창녕","고성",
            "남해","하동","산청","함양","거창",
            "합천"
    );

    static final List<String> JEJU_CITY = Arrays.asList(
            "제주","서귀포"
    );

}
