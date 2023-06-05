package com.lettrip.lettripbackend.service;

import com.lettrip.lettripbackend.constant.MissionType;
import com.lettrip.lettripbackend.constant.PlaceCategory;
import com.lettrip.lettripbackend.constant.RankingType;
import com.lettrip.lettripbackend.controller.ApiResponse;
import com.lettrip.lettripbackend.controller.mission.dto.MissionDto;
import com.lettrip.lettripbackend.controller.mission.dto.RankingDto;
import com.lettrip.lettripbackend.domain.mission.Mission;
import com.lettrip.lettripbackend.domain.travel.Place;
import com.lettrip.lettripbackend.domain.user.User;
import com.lettrip.lettripbackend.repository.MissionRepository;
import com.lettrip.lettripbackend.repository.queryDsl.MissionQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MissionService {
    public static final int QR_MISSION_POINT = 500;
    private final MissionRepository missionRepository;
    private final MissionQueryRepository missionQueryRepository;
    private final UserService userService;
    private final PlaceService placeService;

    @Transactional
    public ApiResponse saveMission(MissionDto.Request missionDto) {
        // 1. 미션 포인트 저장할 사용자 email로 찾기
        User user = userService.findUserByEmail(missionDto.getEmail());
        // 2. 미션 장소 찾기
        Place place = placeService.findPlaceByLocationPoint(
                placeService.getFormatLocationPoint(
                        String.valueOf(missionDto.getXpoint()),
                        String.valueOf(missionDto.getYpoint())
                )
        );
        // 3. 미션 저장
        missionRepository.save(
                Mission.builder()
                        .user(user)
                        .place(place)
                        .missionType(MissionType.QR_MISSION)
                        .accomplishedDate(missionDto.getAccomplishedDate())
                        .build()
        );
        // 4. 포인트 반영
        user.addPoint(QR_MISSION_POINT);

        return new ApiResponse(true,"미션 포인트 적립이 완료되었습니다.");
    }

    /*
    1. 미션 다득점자 (각 미션 별로)
    2. 음식점 & 카페 다방문자
    3. 다양한 지역을 여행다닌 사람
     */

    public List<RankingDto.Response> getRankingList(RankingDto.Request request) {
        RankingType rankingType = RankingType.valueOf(request.getRankingType());
        switch (rankingType) {
            case QR_MISSION -> {return getMissionRanking(MissionType.QR_MISSION, request);}
            case RANDOM_MISSION -> {return getMissionRanking(MissionType.RANDOM_MISSION, request);}
            case REVIEW_RESTAURANT -> {return getFoodPlaceVisitRanking(PlaceCategory.FD6,request);}
            case REVIEW_CAFE -> {return getFoodPlaceVisitRanking(PlaceCategory.CE7,request);}
            case TRAVEL_CITY -> {return getMostCityVisitRanking(request);}
            default -> {return new ArrayList<>();}
            }
    }

    public  List<RankingDto.Response> getMissionRanking(MissionType missionType, RankingDto.Request request) {
        return missionQueryRepository.getMissionCountByUser(missionType,request.getFrom(),request.getTo());
    }

    public List<RankingDto.Response> getFoodPlaceVisitRanking(PlaceCategory placeCategory, RankingDto.Request request) {
        return missionQueryRepository.getFoodReviewCountByUser(placeCategory,request.getFrom(),request.getTo());
    }

    public List<RankingDto.Response> getMostCityVisitRanking(RankingDto.Request request) {
        return missionQueryRepository.getMostCityCountByUser(request.getFrom(),request.getTo());
    }



}
