package com.lettrip.lettripbackend.controller.liked.dto;

import com.lettrip.lettripbackend.constant.LikedType;
import lombok.*;

public class LikedDto {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        private Long targetId;
        private LikedType likedType;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        private Long targetId;
        private Boolean liked;
    }
}
