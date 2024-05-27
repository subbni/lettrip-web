package com.lettrip.lettripbackend.controller.meetUpReview.dto;

import com.lettrip.lettripbackend.constant.MeetUpStatus;
import com.lettrip.lettripbackend.controller.poke.dto.PokeDto;
import com.lettrip.lettripbackend.controller.user.dto.UserProfileDto;
import com.lettrip.lettripbackend.domain.meetup.MeetUpReview;
import com.lettrip.lettripbackend.domain.meetup.Poke;
import lombok.*;

public class ShowMeetUpReview {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        UserProfileDto.Response userProfile;
        MeetUpStatus meetUpStatus;
        String content;

        public Response(MeetUpReview meetUpReview) {
            this.userProfile =new UserProfileDto.Response(meetUpReview.getWriteUser());
            this.meetUpStatus = meetUpReview.getMeetUpStatus();
            this.content = meetUpReview.getContent();
        }
    }
}
