package com.lettrip.lettripbackend.controller.meetUpPost.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lettrip.lettripbackend.domain.meetup.MeetUpPost;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Null;
import lombok.*;

import java.time.LocalDateTime;

public class CreateMeetUpPost {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        private boolean isGpsEnabled;
        @JsonFormat(shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime meetUpDate;
        private String province;
        private String city;
        private String title;
        private String content;

        @Nullable
        private Long placeId;
        @Nullable
        private Long travelId;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        Long meetUpPostId;

        public static Response fromEntity(MeetUpPost meetUpPost) {
            return Response.builder()
                    .meetUpPostId(meetUpPost.getId())
                    .build();
        }
    }
}
