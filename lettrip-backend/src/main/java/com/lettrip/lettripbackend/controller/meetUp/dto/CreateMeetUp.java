package com.lettrip.lettripbackend.controller.meetUp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

public class CreateMeetUp {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        @NotNull
        String roomId;
        @NotNull
        Long meetUpPostId;
        @NotNull
        Long participantId;
        @JsonFormat(shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        @NotNull
        LocalDate meetUpDate;

    }
}
