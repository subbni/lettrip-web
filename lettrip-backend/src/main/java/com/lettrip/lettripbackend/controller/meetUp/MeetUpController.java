package com.lettrip.lettripbackend.controller.meetUp;

import com.lettrip.lettripbackend.controller.ApiResponse;
import com.lettrip.lettripbackend.controller.meetUp.dto.CreateMeetUp;
import com.lettrip.lettripbackend.security.CurrentUser;
import com.lettrip.lettripbackend.security.CustomUserDetails;
import com.lettrip.lettripbackend.service.MeetUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/meetUp")
@RestController
public class MeetUpController {
    private final MeetUpService meetUpService;

    @PostMapping("/create")
    public ApiResponse createMeetUp(
            @CurrentUser CustomUserDetails customUserDetails,
            @RequestBody CreateMeetUp.Request request
            ) {
        return meetUpService.saveMeetUp(request,customUserDetails.getId());
    }
}