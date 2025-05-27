package com.lettrip.lettripbackend.repository.review;

import com.lettrip.lettripbackend.constant.PlaceCategory;
import com.lettrip.lettripbackend.controller.mission.dto.QRankingDto_Response;
import com.lettrip.lettripbackend.controller.mission.dto.RankingDto;
import com.lettrip.lettripbackend.domain.travel.QReview;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    public ReviewRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<RankingDto.Response> getFoodReviewCountByUser(PlaceCategory placeCategory, LocalDate from, LocalDate to) {
        QReview qReview = QReview.review;
        List<RankingDto.Response> result = queryFactory.select(
                        new QRankingDto_Response(qReview.user, qReview.count())
                ).from(qReview)
                .where(qReview.place.categoryCode.eq(placeCategory))
                .groupBy(qReview.user)
                .orderBy(qReview.count().desc())
                .fetch();
        return result;
    }
}
