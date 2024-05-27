package com.lettrip.lettripbackend.controller.meetUpReview.dto;

import lombok.*;

public class CreateMeetUpReview {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        Long meetUpId;
        Long objectUserId;
        String content;
    }
}
