package com.lettrip.lettripbackend.repository;

import com.lettrip.lettripbackend.domain.community.Article;
import com.lettrip.lettripbackend.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article,Long> {
    Page<Article> findByUser(User user, Pageable pageable);
}
