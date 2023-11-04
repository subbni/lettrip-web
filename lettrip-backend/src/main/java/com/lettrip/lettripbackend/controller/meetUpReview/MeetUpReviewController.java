package com.lettrip.lettripbackend.controller.meetUpReview;

import com.lettrip.lettripbackend.controller.ApiResponse;
import com.lettrip.lettripbackend.controller.meetUpReview.dto.CreateMeetUpReview;
import com.lettrip.lettripbackend.controller.meetUpReview.dto.ShowMeetUpReview;
import com.lettrip.lettripbackend.security.CurrentUser;
import com.lettrip.lettripbackend.security.CustomUserDetails;
import com.lettrip.lettripbackend.service.MeetUpReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/meetUpReview")
@RestController
public class MeetUpReviewController {
    private final MeetUpReviewService meetUpReviewService;

    @PostMapping("/create")
    public ApiResponse createMeetUpReview(
            @CurrentUser CustomUserDetails customUserDetails,
            @RequestBody CreateMeetUpReview.Request request
            ) {
        return meetUpReviewService.saveMeetUpReview(request,customUserDetails.getId());
    }

    // 사용자별 조회
    @GetMapping("/user/{userId}")
    public Page<ShowMeetUpReview.Response> showUserMeetUpReview(
            @PathVariable Long userId,
            Pageable pageable
    ) {
        return meetUpReviewService.getUserMeetUpReview(userId,pageable);
    }
}
