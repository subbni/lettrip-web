package com.lettrip.lettripbackend.controller.meetUpPost;

import com.lettrip.lettripbackend.controller.ApiResponse;
import com.lettrip.lettripbackend.controller.meetUpPost.dto.CreateMeetUpPost;
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
@RequestMapping("api/meetUpPost")
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

    public Page<ShowMeetUpPostList.Response> showMeetUpPostPage(Pageable pageable) {
        return meetUpPostService.getAllMeetUpPostPage(pageable);
    }

}
