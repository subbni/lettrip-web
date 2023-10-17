package com.lettrip.lettripbackend.controller.poke.dto;

import com.lettrip.lettripbackend.constant.PokeStatus;
import com.lettrip.lettripbackend.controller.user.dto.UserProfileDto;
import com.lettrip.lettripbackend.domain.meetup.Poke;
import lombok.*;

public class PokeDto {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        private Long meetUpPostId;
        private String briefMessage;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        Long id;
        Long meetUpPostId;
        String briefMessage;
        PokeStatus pokeStatus;
        UserProfileDto.Response userProfile;

        public static Response fromEntity(Poke poke) {
            return Response.builder()
                    .id(poke.getId())
                    .meetUpPostId(poke.getMeetUpPost().getId())
                    .briefMessage(poke.getBriefMessage())
                    .pokeStatus(poke.getPokeStatus())
                    .userProfile(new UserProfileDto.Response(poke.getUser()))
                    .build();
        }
    }
}
