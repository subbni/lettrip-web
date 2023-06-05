package com.lettrip.lettripbackend.controller.mission.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lettrip.lettripbackend.domain.user.User;
import com.querydsl.core.annotations.QueryProjection;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

public class RankingDto {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        @NotNull
        private String rankingType;

        @NotNull
        private Integer count;

        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate from;

        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate to;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        String userNickname;
        String userEmail;
        Long count;

        @QueryProjection
        public Response(User user, long count) {
            this.userNickname = user.getNickname();
            this.userEmail = user.getEmail();
            this.count = count;
        }
    }
}
