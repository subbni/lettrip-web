package com.lettrip.lettripbackend.controller.liked;

import com.lettrip.lettripbackend.controller.ApiResponse;
import com.lettrip.lettripbackend.controller.liked.dto.LikedDto;
import com.lettrip.lettripbackend.constant.LikedType;
import com.lettrip.lettripbackend.security.CurrentUser;
import com.lettrip.lettripbackend.security.CustomUserDetails;
import com.lettrip.lettripbackend.service.LikedService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/liked")
@RequiredArgsConstructor
@RestController
public class LikedController {

    private final LikedService likedService;

    @GetMapping("/check")
    public LikedDto.Response checkLiked(
            @CurrentUser CustomUserDetails customUserDetails,
            @RequestParam("likedType") LikedType likedType,
            @RequestParam("targetId") Long targetId
            ) {
        return likedService.checkIfLiked(customUserDetails.getId(), likedType, targetId);
    }

    @PostMapping("/push")
    public ApiResponse pushLiked(
            @CurrentUser CustomUserDetails customUserDetails,
            @RequestBody LikedDto.Request request) {
        return likedService.saveLiked(customUserDetails.getId(), request);
    }

    @PostMapping("/delete")
    public ApiResponse deleteLiked(
            @CurrentUser CustomUserDetails customUserDetails,
            @RequestBody LikedDto.Request request
    ) {
        // TODO : delete 매핑은 requestBody를 사용할 수 없음 수정 필요
        return likedService.deleteLiked(customUserDetails.getId(), request);
    }
}
