package com.lettrip.lettripbackend.controller.article.dto;


import com.lettrip.lettripbackend.domain.community.Article;
import lombok.*;

import java.time.LocalDateTime;

public class ShowArticle {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        Long id;
        String writerEmail;
        String writerName;
        String title;
        String content;
        int hit;
        int likedCount;
        private LocalDateTime createdDate;
        private LocalDateTime modifiedDate;

        public static Response fromEntity(Article article) {
            return Response.builder()
                    .id(article.getId())
                    .writerEmail(article.getUser().getEmail())
                    .writerName(article.getUser().getName())
                    .title(article.getTitle())
                    .content(article.getContent())
                    .hit(article.getHit())
                    .likedCount(article.getLikeCount())
                    .createdDate(article.getCreatedDate())
                    .modifiedDate(article.getModifiedDate())
                    .build();
        }
    }
}
