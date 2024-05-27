package com.lettrip.lettripbackend.controller.meetUp.dto;

import lombok.*;

public class VerifyMeetUpCode {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        String code;
        Long meetUpId;
    }
}
