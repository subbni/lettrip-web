package com.lettrip.lettripbackend.controller.chat.dto;

import com.lettrip.lettripbackend.mongo.domain.Chat;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
public class ChatDto {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        @NotNull
        private String roomId;
        @NotNull
        private Long senderId;
        @NotNull
        private Long receiverId;
        @NotNull
        private String message;
        @NotNull
        private Boolean isImage;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        String message;
        Long senderId;
        Long receiverId;
        LocalDateTime createdAt;
        Boolean isImage;

        public static Response fromEntity(Chat chat) {
            return Response.builder()
                    .message(chat.getMessage())
                    .senderId(chat.getSendUserId())
                    .receiverId(chat.getReceiveUserId())
                    .createdAt(chat.getCreatedAt())
                    .build();
        }
    }
}
