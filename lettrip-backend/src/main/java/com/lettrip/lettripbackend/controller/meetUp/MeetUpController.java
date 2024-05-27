package com.lettrip.lettripbackend.controller.meetUp;

import com.lettrip.lettripbackend.controller.ApiResponse;
import com.lettrip.lettripbackend.controller.meetUp.dto.CreateMeetUp;
import com.lettrip.lettripbackend.controller.meetUp.dto.MeetUpCodeDto;
import com.lettrip.lettripbackend.controller.meetUp.dto.MeetUpDto;
import com.lettrip.lettripbackend.controller.meetUp.dto.VerifyMeetUpCode;
import com.lettrip.lettripbackend.security.CurrentUser;
import com.lettrip.lettripbackend.security.CustomUserDetails;
import com.lettrip.lettripbackend.service.MeetUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/cancel/{meetUpId}")
    public ApiResponse cancelMeetUp(
            @CurrentUser CustomUserDetails customUserDetails,
            @PathVariable Long meetUpId
    ) {
        return meetUpService.cancelMeetUp(meetUpId,customUserDetails.getId());
    }

    // 인증코드 요청
    @GetMapping("/meetUp-code/{meetUpId}")
    public MeetUpCodeDto.Response sendCode(
            @CurrentUser CustomUserDetails customUserDetails,
            @PathVariable Long meetUpId
    ) {
        return meetUpService.sendMeetUpCode(meetUpId,customUserDetails.getId());
    }

    // 인증 요청
    @PostMapping("/meetUp-verify")
    public ApiResponse verifyCode(
            @CurrentUser CustomUserDetails customUserDetails,
            @RequestBody VerifyMeetUpCode.Request request
            ) {
        return meetUpService.verifyMeetUpCode(request,customUserDetails.getId());
    }

    @GetMapping("/{meetUpId}")
    public MeetUpDto.Response showMeetUp(
            @CurrentUser CustomUserDetails customUserDetails,
            @PathVariable Long meetUpId
    ) {
        return meetUpService.getMeetUpById(meetUpId,customUserDetails.getId());
    }
}