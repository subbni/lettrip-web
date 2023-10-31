package com.lettrip.lettripbackend.controller.meetUpPost.dto;

import lombok.*;

public class CreateMeetUp {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        private String nothing;
    }
}
