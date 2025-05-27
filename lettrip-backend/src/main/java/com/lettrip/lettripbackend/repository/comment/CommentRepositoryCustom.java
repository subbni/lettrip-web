package com.lettrip.lettripbackend.repository.comment;

import com.lettrip.lettripbackend.domain.community.Comment;
import com.querydsl.core.BooleanBuilder;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustom {
    Page<Comment> findAll(@Nullable BooleanBuilder condition, Pageable pageable);
}
