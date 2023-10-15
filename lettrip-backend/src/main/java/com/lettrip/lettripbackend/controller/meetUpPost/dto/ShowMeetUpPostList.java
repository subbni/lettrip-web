package com.lettrip.lettripbackend.controller.meetUpPost.dto;

import com.lettrip.lettripbackend.constant.Province;
import com.lettrip.lettripbackend.controller.user.dto.UserDto;
import com.lettrip.lettripbackend.domain.meetup.MeetUpPost;
import lombok.*;

import java.time.LocalDateTime;

public class ShowMeetUpPostList {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        Long id;
        UserDto.Response userDto;
        Boolean isGPSEnabled;
        LocalDateTime meetUpDate;
        Province province;
        String city;
        String title;
        LocalDateTime createdDate;

        public static ShowMeetUpPostList.Response fromEntity(MeetUpPost meetUpPost) {
            return ShowMeetUpPostList.Response.builder()
                    .id(meetUpPost.getId())
                    .userDto(
                            new UserDto.Response(meetUpPost.getUser())
                    )
                    .isGPSEnabled(meetUpPost.isGPSEnabled())
                    .meetUpDate(meetUpPost.getMeetUpDate())
                    .province(meetUpPost.getProvince())
                    .city(meetUpPost.getCity())
                    .title(meetUpPost.getTitle())
                    .createdDate(meetUpPost.getCreatedDate())
                    .build();
        }
    }
}
