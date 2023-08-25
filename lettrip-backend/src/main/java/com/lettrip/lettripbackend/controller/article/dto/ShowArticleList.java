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
        String writerNickname;
        String title;
        int hit;
        int likedCount;
        int commentCount;
        private LocalDateTime createdDate;

        public static ShowArticleList.Response fromEntity(Article article) {
            return Response.builder()
                    .id(article.getId())
                    .writerNickname(article.getUser().getNickname())
                    .title(article.getTitle())
                    .hit(article.getHit())
                    .likedCount(article.getLikeCount())
                    .commentCount(article.getCommentCount())
                    .createdDate(article.getCreatedDate())
                    .build();
        }
    }
}
