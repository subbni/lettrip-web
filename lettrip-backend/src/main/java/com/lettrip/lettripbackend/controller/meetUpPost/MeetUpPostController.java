package com.lettrip.lettripbackend.controller.meetUpPost;

import com.lettrip.lettripbackend.controller.ApiResponse;
import com.lettrip.lettripbackend.controller.meetUpPost.dto.CreateMeetUpPost;
import com.lettrip.lettripbackend.controller.meetUpPost.dto.ModifyMeetUpPost;
import com.lettrip.lettripbackend.controller.meetUpPost.dto.ShowMeetUpPost;
import com.lettrip.lettripbackend.controller.meetUpPost.dto.ShowMeetUpPostList;
import com.lettrip.lettripbackend.security.CurrentUser;
import com.lettrip.lettripbackend.security.CustomUserDetails;
import com.lettrip.lettripbackend.service.MeetUpPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/meetUpPost")
@RestController
public class MeetUpPostController {
    private final MeetUpPostService meetUpPostService;

    @PostMapping("/create")
    public ApiResponse createMeetUpPost(
            @CurrentUser CustomUserDetails customUserDetails,
            @RequestBody CreateMeetUpPost.Request request
            ) {
        return meetUpPostService.saveMeetUpPost(request, customUserDetails.getId());
    }

    @PutMapping("/modify")
    public ApiResponse modifyMeetUpPost(
            @CurrentUser CustomUserDetails customUserDetails,
            @RequestBody ModifyMeetUpPost.Request request
    ) {
        return meetUpPostService.updateMeetUpPost(request,customUserDetails.getId());
    }

    @GetMapping("/{meetUpPostId}")
    public ShowMeetUpPost.Response showMeetUpPost(@PathVariable("meetUpPostId") Long meetUpPostId) {
        return meetUpPostService.showMeetUpPost(meetUpPostId);
    }

    @GetMapping
    public Page<ShowMeetUpPostList.Response> showMeetUpPostPage(
            @RequestParam(value="province" ,required = false) String province,
            @RequestParam(value="city", required = false) String city,
            @RequestParam(value = "meetUpPostStatus", required = false) String meetUpPostStatus,
            @RequestParam(value = "isGpsEnabled", required = false) Boolean isGpsEnabled,
            Pageable pageable
    ) {
        return meetUpPostService.getMeetUpPostPage(
                ShowMeetUpPostList.Request.builder()
                        .province(province)
                        .city(city)
                        .meetUpPostStatus(meetUpPostStatus)
                        .isGpsEnabled(isGpsEnabled)
                        .build(),
                pageable
        );
    }

    // 사용자 작성 MeetUpPost 조회
    @GetMapping("/my")
    public Page<ShowMeetUpPostList.Response> showUserMeetUpPostPage(
            @CurrentUser CustomUserDetails customUserDetails,
            Pageable pageable
    ) {
        return meetUpPostService.getUserMeetUpPost(customUserDetails.getId(),pageable);
    }

    // 사용자가 찌른 MeetUpPost 조회
    @GetMapping("/my/poked")
    public Page<ShowMeetUpPostList.Response> showUserPokedMeetUpPostPage(
            @CurrentUser CustomUserDetails customUserDetails,
            Pageable pageable
    ) {
        return meetUpPostService.getPokedMeetUpPost(customUserDetails.getId(),pageable);
    }
}
