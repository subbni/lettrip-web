package com.lettrip.lettripbackend.repository.comment;

import com.lettrip.lettripbackend.domain.community.Article;
import com.lettrip.lettripbackend.domain.community.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
    Page<Comment> findByArticle(Article article, Pageable pageable);
}
