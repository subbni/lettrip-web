package com.lettrip.lettripbackend.repository.travel;

import com.lettrip.lettripbackend.controller.mission.dto.RankingDto;
import com.lettrip.lettripbackend.domain.travel.Travel;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface TravelRepositoryCustom {
    List<RankingDto.Response> getDistinctCityCountByUser(LocalDate from, LocalDate to);
    Page<Travel> findAllByDynamicCondition(BooleanBuilder condition, Pageable pageable);

}
