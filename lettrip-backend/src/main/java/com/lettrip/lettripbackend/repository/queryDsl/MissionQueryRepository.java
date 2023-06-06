package com.lettrip.lettripbackend.repository.queryDsl;

import com.lettrip.lettripbackend.constant.MissionType;
import com.lettrip.lettripbackend.constant.PlaceCategory;
import com.lettrip.lettripbackend.controller.mission.dto.QRankingDto_Response;
import com.lettrip.lettripbackend.controller.mission.dto.RankingDto;
import com.lettrip.lettripbackend.domain.mission.QMission;
import com.lettrip.lettripbackend.domain.travel.QReview;
import com.lettrip.lettripbackend.domain.travel.QTravel;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MissionQueryRepository {
    private final JPAQueryFactory queryFactory;

    public MissionQueryRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public List<RankingDto.Response> getMissionCountByUser(MissionType missionType, LocalDate from, LocalDate to) {
        QMission qMission = QMission.mission;
        List<RankingDto.Response> result = queryFactory.select(
                        new QRankingDto_Response(qMission.user, qMission.count())
                ).from(qMission)
                .where(qMission.missionType.eq(missionType)
                        .and(qMission.accomplishedDate.between(from,to)))
                .groupBy(qMission.user)
                .stream().sorted(new Comparator<RankingDto.Response>() {
                    @Override
                    public int compare(RankingDto.Response r1, RankingDto.Response r2) {
                        if(r1.getCount().equals(r2.getCount())) {
                            return 0;
                        }
                        return r1.getCount() < r2.getCount() ? 1 : -1;
                    }
                }).limit(5).collect(Collectors.toList());
        return result;
    }

    public List<RankingDto.Response> getFoodReviewCountByUser(PlaceCategory placeCategory, LocalDate from, LocalDate to) {
        QReview qReview = QReview.review;
        List<RankingDto.Response> result = queryFactory.select(
                        new QRankingDto_Response(qReview.user, qReview.count())
        ).from(qReview)
                .where(qReview.place.categoryCode.eq(placeCategory))
                .groupBy(qReview.user)
                .stream().sorted(new Comparator<>() {
                    @Override
                    public int compare(RankingDto.Response r1, RankingDto.Response r2) {
                        if (r1.getCount().equals(r2.getCount())) {
                            return 0;
                        }
                        return r1.getCount() < r2.getCount() ? 1 : -1;
                    }
                }).limit(5).collect(Collectors.toList());

        // TODO: 기간을 어떻게 필터링?

        return result;
    }

    public List<RankingDto.Response> getMostCityCountByUser(LocalDate from, LocalDate to) {
        QTravel qTravel = QTravel.travel;
        List<RankingDto.Response> result = queryFactory.select(
                new QRankingDto_Response(qTravel.user, qTravel.city.countDistinct())
        ).from(qTravel)
                .where(qTravel.isVisited.eq(true)
                .and(qTravel.departDate.between(from,to)))
                .groupBy(qTravel.user)
                .stream().sorted(new Comparator<>() {
                    @Override
                    public int compare(RankingDto.Response r1, RankingDto.Response r2) {
                        if (r1.getCount().equals(r2.getCount())) {
                            return 0;
                        }
                        return r1.getCount() < r2.getCount() ? 1 : -1;
                    }
                }).limit(5).collect(Collectors.toList());

        return result;
    }
}
