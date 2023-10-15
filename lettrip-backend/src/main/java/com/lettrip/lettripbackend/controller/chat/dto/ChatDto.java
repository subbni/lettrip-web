package com.lettrip.lettripbackend.controller.chat.dto;

import com.lettrip.lettripbackend.mongo.domain.Chat;
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
        private String roomId;
        private Long senderId;
        private Long receiverId;
        private String message;
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
