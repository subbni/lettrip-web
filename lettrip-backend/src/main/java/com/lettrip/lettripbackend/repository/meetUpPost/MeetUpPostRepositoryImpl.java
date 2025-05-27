package com.lettrip.lettripbackend.repository.meetUpPost;

import com.lettrip.lettripbackend.domain.meetup.MeetUpPost;
import com.lettrip.lettripbackend.domain.meetup.QMeetUpPost;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MeetUpPostRepositoryImpl implements MeetUpPostRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    public MeetUpPostRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<MeetUpPost> findAll(BooleanBuilder condition, Pageable pageable) {
        QMeetUpPost qMeetUpPost = QMeetUpPost.meetUpPost;
        List<MeetUpPost> result = queryFactory.select(qMeetUpPost)
                .from(qMeetUpPost)
                .where(condition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long total = queryFactory.select(qMeetUpPost.count())
                .from(qMeetUpPost)
                .where(condition)
                .fetchOne();
        if(total == null) {
            total = 0L;
        }
        return new PageImpl<>(result, pageable, total);
    }
}
