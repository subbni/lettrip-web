package com.lettrip.lettripbackend.repository;

import com.lettrip.lettripbackend.domain.community.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article,Long> {
}
