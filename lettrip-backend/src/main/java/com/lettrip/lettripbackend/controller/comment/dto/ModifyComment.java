package com.lettrip.lettripbackend.controller.comment.dto;

import com.lettrip.lettripbackend.domain.community.Comment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

public class ModifyComment
{
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
        private String content;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        String content;

        public static Response fromEntity(Comment comment) {
            return Response.builder()
                    .content(comment.getContent())
                    .build();
        }
    }

}
