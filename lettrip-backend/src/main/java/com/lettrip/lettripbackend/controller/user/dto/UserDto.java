package com.lettrip.lettripbackend.controller.user.dto;

import com.lettrip.lettripbackend.domain.user.User;
import lombok.*;


public class UserDto {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        String email;
        String name;
        String nickname;
        String imageUrl;
        int point;
        public Response(User user) {
            this.email = user.getEmail();
            this.name = user.getName();
            this.nickname = user.getNickname();
            this.imageUrl = user.getImageUrl();
            this.point = user.getPoint();
        }
    }
}
