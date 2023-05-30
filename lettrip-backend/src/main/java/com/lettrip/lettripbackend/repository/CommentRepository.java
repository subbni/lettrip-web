package com.lettrip.lettripbackend.repository;

import com.lettrip.lettripbackend.domain.community.Article;
import com.lettrip.lettripbackend.domain.community.Comment;
import com.lettrip.lettripbackend.domain.travel.Travel;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long>, JpaSpecificationExecutor<Comment> {
    Page<Comment> findByArticle(Article article, Pageable pageable);
    Page<Comment> findAll(@Nullable Specification<Comment> spec, Pageable pageable);
    void deleteByArticle(Article article);
}
