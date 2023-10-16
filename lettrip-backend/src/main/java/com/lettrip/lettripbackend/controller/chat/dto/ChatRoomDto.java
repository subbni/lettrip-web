package com.lettrip.lettripbackend.controller.chat.dto;

import com.lettrip.lettripbackend.controller.user.dto.UserDto;
import com.lettrip.lettripbackend.controller.user.dto.UserProfileDto;
import com.lettrip.lettripbackend.domain.user.User;
import com.lettrip.lettripbackend.mongo.domain.ChatRoom;
import lombok.*;

import java.time.LocalDateTime;

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
        UserProfileDto.Response participant;
        String lastMessage;
        LocalDateTime lastMessageTime;

        public Response setParticipant(User participant) {
            this.participant = new UserProfileDto.Response(participant);
            return this;
        }

        public static Response fromEntity(ChatRoom chatRoom) {
            return Response.builder()
                    .roomId(chatRoom.getId())
                    .lastMessage(chatRoom.getLastMessage())
                    .lastMessageTime(chatRoom.getLastMessageTime())
                    .build();
        }
    }
}
