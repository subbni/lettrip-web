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

    @GetMapping("/{meetUpPostId}")
    public ShowMeetUpPost.Response showMeetUpPost(@PathVariable("meetUpPostId") Long meetUpPostId) {
        return meetUpPostService.showMeetUpPost(meetUpPostId);
    }

//    @GetMapping
//    public Page<ShowMeetUpPostList.Response> showMeetUpPostPage(Pageable pageable) {
//        return meetUpPostService.getAllMeetUpPostPage(pageable);
//    }

    /*
    전체 조회인 경우
    province = "all"
    city = "all"
    isGpsEnabled 생략
    */
    @GetMapping
    public Page<ShowMeetUpPostList.Response> showMeetUpPostPage(
            @RequestParam("province") String province,
            @RequestParam("city") String city,
            @RequestParam(value = "isGpsEnabled", required = false) Boolean isGpsEnabled,
            Pageable pageable
    ) {
        return meetUpPostService.getMeetUpPostPage(
                ShowMeetUpPostList.Request.builder()
                        .province(province)
                        .city(city)
                        .isGpsEnabled(isGpsEnabled)
                        .build(),
                pageable
        );
    }

    @PutMapping("/modify")
    public ApiResponse modifyMeetUpPost(
            @CurrentUser CustomUserDetails customUserDetails,
            @RequestBody ModifyMeetUpPost.Request request
            ) {
        return meetUpPostService.updateMeetUpPost(request,customUserDetails.getId());
    }

}
