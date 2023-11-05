package com.lettrip.lettripbackend.controller.user.dto;

import com.lettrip.lettripbackend.domain.user.Sex;
import com.lettrip.lettripbackend.domain.user.User;
import lombok.*;

import java.time.LocalDate;

/*
    유저 페이지가 아닌 프로필 용도로 사용
 */
public class UserProfileDto {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        Long id;
        String nickname;
        String email;
        String imageUrl;
        Sex sex;
        LocalDate birthDate;

        public Response(User user) {
            this.id = user.getId();
            this.nickname = user.getNickname();
            this.email = user.getEmail();
            this.imageUrl = user.getImageUrl();
            this.sex = user.getSex();
            this.birthDate = user.getBirthDate();
        }

    }
}
