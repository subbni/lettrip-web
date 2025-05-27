package com.lettrip.lettripbackend.repository.liked;

import com.lettrip.lettripbackend.constant.LikedType;
import com.lettrip.lettripbackend.domain.community.Article;
import com.lettrip.lettripbackend.domain.community.QArticle;
import com.lettrip.lettripbackend.domain.liked.QLiked;
import com.lettrip.lettripbackend.domain.travel.Place;
import com.lettrip.lettripbackend.domain.travel.QPlace;
import com.lettrip.lettripbackend.domain.travel.QTravel;
import com.lettrip.lettripbackend.domain.travel.Travel;
import com.lettrip.lettripbackend.domain.user.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LikedRepositoryImpl implements LikedRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    public LikedRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }
    @Override
    public Page<Travel> findUserLikedTravel(User user, Pageable pageable) {
        QLiked qLiked = QLiked.liked;
        QTravel qTravel = QTravel.travel;
        BooleanExpression condition = qLiked.user.eq(user).and(qLiked.likedType.eq(LikedType.TRAVEL_LIKE));
        List<Travel> result = queryFactory.select(qTravel)
                .from(qLiked)
                .where(condition)
                .rightJoin(qTravel).on(qLiked.targetId.eq(qTravel.id))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(qLiked.count())
                .from(qLiked)
                .where(condition)
                .fetchOne();
        if(total == null) {
            total = 0L;
        }
        return new PageImpl<>(result, pageable, total);
    }

    @Override
    public Page<Article> findUserLikedArticle(User user,Pageable pageable) {
        QLiked qLiked = QLiked.liked;
        QArticle qArticle = QArticle.article;
        BooleanExpression condition = qLiked.user.eq(user).and(qLiked.likedType.eq(LikedType.ARTICLE_LIKE));
        List<Article> result = queryFactory.select(qArticle)
                .from(qLiked)
                .where(condition)
                .rightJoin(qArticle).on(qLiked.targetId.eq(qArticle.id))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(qLiked.count())
                .from(qLiked)
                .where(condition)
                .fetchOne();
        if(total == null) {
            total = 0L;
        }
        return new PageImpl<>(result, pageable, total);
    }

    @Override
    public Page<Place> findUserLikedPlace(User user,Pageable pageable) {
        QLiked qLiked = QLiked.liked;
        QPlace qPlace = QPlace.place;
        BooleanExpression condition = qLiked.user.eq(user).and(qLiked.likedType.eq(LikedType.ARTICLE_LIKE));
        List<Place> result = queryFactory.select(qPlace)
                .from(qLiked)
                .where(condition)
                .rightJoin(qPlace).on(qLiked.targetId.eq(qPlace.id))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(qLiked.count())
                .from(qLiked)
                .where(condition)
                .fetchOne();
        if(total == null) {
            total = 0L;
        }
        return new PageImpl<>(result, pageable, total);
    }
}
