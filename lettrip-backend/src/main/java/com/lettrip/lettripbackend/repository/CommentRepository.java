package com.lettrip.lettripbackend.repository;

import com.lettrip.lettripbackend.domain.community.Article;
import com.lettrip.lettripbackend.domain.community.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    Page<Comment> findByArticle(Article article, Pageable pageable);
    void deleteByArticle(Article article);
}
