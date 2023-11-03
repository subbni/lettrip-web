package com.lettrip.lettripbackend.controller.meetUp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lettrip.lettripbackend.constant.MeetUpStatus;
import com.lettrip.lettripbackend.domain.meetup.MeetUp;
import lombok.*;

import java.time.LocalDate;

public class MeetUpDto {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response{
        Long id;
        @JsonFormat(shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate meetUpDate;
        MeetUpStatus meetUpStatus;
        public Response(MeetUp meetUp) {
            this.id = meetUp.getId();
            this.meetUpDate = meetUp.getMeetUpDate();
            this.meetUpStatus = meetUp.getMeetUpStatus();
        }
    }
}
