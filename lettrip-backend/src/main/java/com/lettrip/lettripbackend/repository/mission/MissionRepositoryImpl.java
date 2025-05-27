package com.lettrip.lettripbackend.repository.mission;


import com.lettrip.lettripbackend.constant.MissionType;
import com.lettrip.lettripbackend.controller.mission.dto.QRankingDto_Response;
import com.lettrip.lettripbackend.controller.mission.dto.RankingDto;
import com.lettrip.lettripbackend.domain.mission.QMission;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class MissionRepositoryImpl implements MissionRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    public MissionRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<RankingDto.Response> getMissionCountByUser(MissionType missionType, LocalDate from, LocalDate to) {
        QMission qMission = QMission.mission;
        List<RankingDto.Response> result = queryFactory.select(
                        new QRankingDto_Response(qMission.user, qMission.count())
                ).from(qMission)
                .where(qMission.missionType.eq(missionType)
                        .and(qMission.accomplishedDate.between(from,to)))
                .groupBy(qMission.user)
                .orderBy(qMission.count().desc())
                .limit(5)
                .fetch();
        return result;
    }
}
