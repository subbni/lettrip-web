package com.lettrip.lettripbackend.controller.meetUp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

public class CreateMeetUp {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        String roomId;
        Long meetUpPostId;
        Long participantId;
        @JsonFormat(shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate meetUpDate;

    }
}
