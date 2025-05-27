package com.lettrip.lettripbackend.repository.review;

import com.lettrip.lettripbackend.constant.PlaceCategory;
import com.lettrip.lettripbackend.controller.mission.dto.RankingDto;

import java.time.LocalDate;
import java.util.List;

public interface ReviewRepositoryCustom {
    List<RankingDto.Response> getFoodReviewCountByUser(PlaceCategory placeCategory, LocalDate from, LocalDate to);
}
