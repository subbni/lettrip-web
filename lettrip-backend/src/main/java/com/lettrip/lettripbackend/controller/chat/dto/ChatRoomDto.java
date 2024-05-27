package com.lettrip.lettripbackend.controller.chat.dto;

import com.lettrip.lettripbackend.constant.MeetUpStatus;
import com.lettrip.lettripbackend.controller.user.dto.UserDto;
import com.lettrip.lettripbackend.controller.user.dto.UserProfileDto;
import com.lettrip.lettripbackend.domain.user.User;
import com.lettrip.lettripbackend.mongo.domain.Chat;
import com.lettrip.lettripbackend.mongo.domain.ChatRoom;
import jakarta.validation.constraints.NotNull;
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
        @NotNull
        private Long meetUpPostId;
        @NotNull
        private Long writeUserId;
        @NotNull
        private Long requestUserId;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        String roomId;
        Long currentUserId;
        UserProfileDto.Response participant;
        Long meetUpPostId;
        Long meetUpId;
        MeetUpStatus meetUpStatus;
        String lastMessage;
        LocalDateTime lastMessageTime;

        public Response setParticipant(User participant) {
            this.participant = new UserProfileDto.Response(participant);
            return this;
        }

        public Response setCurrentUserId(Long currentUserId) {
            this.currentUserId = currentUserId;
            return this;
        }

        public static Response fromEntity(ChatRoom chatRoom) {
            return Response.builder()
                    .roomId(chatRoom.getId())
                    .meetUpPostId(chatRoom.getMeetUpPostId())
                    .meetUpId(chatRoom.getMeetUpId())
                    .meetUpStatus(chatRoom.getMeetUpStatus())
                    .lastMessage(chatRoom.getLastMessage())
                    .lastMessageTime(chatRoom.getLastMessageTime())
                    .build();
        }
    }
}
