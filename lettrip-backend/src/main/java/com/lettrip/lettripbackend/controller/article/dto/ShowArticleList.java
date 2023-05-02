package com.lettrip.lettripbackend.controller.article.dto;

import com.lettrip.lettripbackend.domain.community.Article;
import lombok.*;

import java.time.LocalDateTime;

public class ShowArticleList {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        Long id;
        String writerName;
        String title;
        int hit;
        int likedCount;
        private LocalDateTime createdDate;

        public static ShowArticle.Response fromEntity(Article article) {
            return ShowArticle.Response.builder()
                    .id(article.getId())
                    .writerName(article.getUser().getName())
                    .title(article.getTitle())
                    .hit(article.getHit())
                    .likedCount(article.getLikeCount())
                    .createdDate(article.getCreatedDate())
                    .build();
        }
    }
}
