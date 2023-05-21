package com.lettrip.lettripbackend.controller.auth.dto;

import com.lettrip.lettripbackend.domain.user.User;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

public class SignUpUser {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        @NotNull
        @Email
        private String email;
        @NotNull
        @Size(min=5, max=20, message="password size must be 5 ~ 20")
        private String password;
        @NotNull
        @Size(min=2, max=20, message="name size must be 2 ~ 20")
        private String name;
        @NotNull
        @Size(min=2, max=30, message="nickname size must be 2 ~ 20")
        private String nickname;

        @Nullable
        private String imageUrl;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        private String email;
        private String name;
        private String nickname;
        private String imageUrl;

        public static Response fromEntity(User user) {
            return Response.builder()
                    .email(user.getEmail())
                    .name(user.getName())
                    .nickname(user.getNickname())
                    .imageUrl(user.getImageUrl())
                    .build();
        }
    }
}
