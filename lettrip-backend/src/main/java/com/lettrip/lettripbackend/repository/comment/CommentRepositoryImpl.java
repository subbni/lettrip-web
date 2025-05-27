package com.lettrip.lettripbackend.repository.comment;

import com.lettrip.lettripbackend.domain.community.Comment;
import com.lettrip.lettripbackend.domain.community.QComment;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentRepositoryImpl implements CommentRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    public CommentRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }
    @Override
    public Page<Comment> findAll(BooleanBuilder condition, Pageable pageable) {
        QComment qComment = QComment.comment;
        List<Comment> result = queryFactory.select(qComment)
                .from(qComment)
                .where(condition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qComment.id.desc())
                .fetch();
        Long total = queryFactory.select(qComment.count())
                .from(qComment)
                .where(condition)
                .fetchOne();

        if(total == null) {
            total = 0L;
        }
        return new PageImpl<>(result, pageable, total);
    }
}
