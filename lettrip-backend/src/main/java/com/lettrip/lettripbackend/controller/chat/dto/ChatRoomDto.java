package com.lettrip.lettripbackend.controller.chat.dto;

import com.lettrip.lettripbackend.mongo.domain.ChatRoom;
import lombok.*;

@Getter
public class ChatRoomDto {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        private Long meetUpPostId;
        private Long writeUserId;
        private Long requestUserId;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        String roomId;
        String lastMessage;

        public static Response fromEntity(ChatRoom chatRoom) {
            return Response.builder()
                    .roomId(chatRoom.getId())
                    .lastMessage(chatRoom.getLastMessage())
                    .build();
        }
    }
}
