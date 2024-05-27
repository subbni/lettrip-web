package com.lettrip.lettripbackend.controller.meetUpPost.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Null;
import lombok.*;

import java.time.LocalDateTime;

public class ModifyMeetUpPost {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        Long id;
        Boolean isGpsEnabled;
        @JsonFormat(shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime meetUpDate;
        String title;
        String content;

        @Nullable
        Long placeId;
        @Nullable
        Long travelId;
    }


}
