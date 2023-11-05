package com.lettrip.lettripbackend.controller.meetUp.dto;

import com.lettrip.lettripbackend.mongo.domain.MeetUpCode;
import lombok.*;

public class MeetUpCodeDto {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response{
        Long meetUpId;
        Long requesterId;
        Long performerId;
        String code;

        public Response(MeetUpCode meetUpCode) {
            this.meetUpId = meetUpCode.getMeetUpId();
            this.requesterId = meetUpCode.getRequesterId();
            this.performerId = meetUpCode.getPerformerId();
            this.code = meetUpCode.getCode();
        }
    }
}
