package com.lettrip.lettripbackend.repository.travel;

import com.lettrip.lettripbackend.controller.mission.dto.QRankingDto_Response;
import com.lettrip.lettripbackend.controller.mission.dto.RankingDto;
import com.lettrip.lettripbackend.domain.travel.QTravel;
import com.lettrip.lettripbackend.domain.travel.Travel;
import com.lettrip.lettripbackend.domain.user.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class TravelRepositoryImpl implements TravelRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    public TravelRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<RankingDto.Response> getDistinctCityCountByUser(LocalDate from, LocalDate to) {
        QTravel qTravel = QTravel.travel;
        List<RankingDto.Response> result = queryFactory.select(
                        new QRankingDto_Response(qTravel.user, qTravel.city.countDistinct())
                ).from(qTravel)
                .where(qTravel.isVisited.eq(true)
                        .and(qTravel.departDate.between(from,to)))
                .groupBy(qTravel.user)
                .orderBy(qTravel.city.countDistinct().desc())
                .limit(5)
                .fetch();

        return result;
    }

    @Override
    public Page<Travel> findAllByDynamicCondition(BooleanBuilder condition, Pageable pageable) {
        QTravel qTravel = QTravel.travel;
        QUser qUser = QUser.user;
        List<Travel> result = queryFactory.select(qTravel)
                .from(qTravel)
                .where(condition)
                .leftJoin(qUser).on(qTravel.user.id.eq(qUser.id))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qTravel.id.desc()) // 최근 작성순
                .fetch();

        Long total = queryFactory.select(qTravel.count())
                .from(qTravel)
                .where(condition)
                .fetchOne();

        if(total == null) {
            total = 0L;
        }

        return new PageImpl<>(result, pageable, total);
    }
}
