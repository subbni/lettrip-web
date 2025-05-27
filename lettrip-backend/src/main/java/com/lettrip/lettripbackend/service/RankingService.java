package com.lettrip.lettripbackend.service;

import com.lettrip.lettripbackend.constant.MissionType;
import com.lettrip.lettripbackend.constant.PlaceCategory;
import com.lettrip.lettripbackend.constant.RankingType;
import com.lettrip.lettripbackend.controller.mission.dto.RankingDto;
import com.lettrip.lettripbackend.repository.mission.MissionRepository;
import com.lettrip.lettripbackend.repository.review.ReviewRepository;
import com.lettrip.lettripbackend.repository.travel.TravelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RankingService {
    private final MissionRepository missionRepository;
    private final ReviewRepository reviewRepository;
    private final TravelRepository travelRepository;

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
            case REVIEW_RESTAURANT -> {return getFoodReviewRanking(PlaceCategory.FD6,request);}
            case REVIEW_CAFE -> {return getFoodReviewRanking(PlaceCategory.CE7,request);}
            case TRAVEL_CITY -> {return getCityTravelRanking(request);}
            default -> {return new ArrayList<>();}
        }
    }

    public  List<RankingDto.Response> getMissionRanking(MissionType missionType, RankingDto.Request request) {
        return missionRepository.getMissionCountByUser(missionType,request.getFrom(),request.getTo());
    }

    public List<RankingDto.Response> getFoodReviewRanking(PlaceCategory placeCategory, RankingDto.Request request) {
        return reviewRepository.getFoodReviewCountByUser(placeCategory,request.getFrom(),request.getTo());
    }

    public List<RankingDto.Response> getCityTravelRanking(RankingDto.Request request) {
        return travelRepository.getDistinctCityCountByUser(request.getFrom(),request.getTo());
    }
}
