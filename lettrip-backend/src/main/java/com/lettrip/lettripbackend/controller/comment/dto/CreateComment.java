package com.lettrip.lettripbackend.controller.comment.dto;

import com.lettrip.lettripbackend.domain.community.Comment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

public class CreateComment {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        @NotNull
        @NotBlank
        private Long article_id;
        @NotNull
        @NotBlank
        private String content;
        private Long parent_comment_id;
        private String mentioned_user_email;
        // TODO : email로 요청? 닉네임으로 요청?
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        String email;
        String content;

        public static Response fromEntity(Comment comment) {
            return Response.builder()
                    .email(comment.getUser().getEmail())
                    .content(comment.getContent())
                    .build();
        }
    }
}
