package com.lettrip.lettripbackend.controller.auth.dto;

import com.lettrip.lettripbackend.domain.user.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

public class LogInUser {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        @NotNull
        @NotBlank
        private String email;
        @NotNull
        @NotBlank
        @Size(min = 4, max = 30)
        private String password;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        String name;
        String email;
        String nickname;
        String imageUrl;
        String accessToken;

        public Response(User user, String token) {
            this.name = user.getName();
            this.email = user.getEmail();
            this.nickname = user.getNickname();
            this.imageUrl = user.getImageUrl();
            this.accessToken = token;
        }
    }
}
