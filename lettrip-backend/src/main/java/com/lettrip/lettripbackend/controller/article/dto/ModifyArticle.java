package com.lettrip.lettripbackend.controller.article.dto;

import com.lettrip.lettripbackend.domain.community.Article;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

public class ModifyArticle {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        @NotNull
        private Long id;
        @NotNull
        @NotBlank
        private String title;

        @NotNull
        @NotBlank
        private String content;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        String title;
        String content;

        public static Response fromEntity(Article article) {
            return Response.builder()
                    .title(article.getTitle())
                    .content(article.getContent())
                    .build();
        }
    }
}
