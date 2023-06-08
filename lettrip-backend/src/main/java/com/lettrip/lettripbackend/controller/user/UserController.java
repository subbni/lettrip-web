package com.lettrip.lettripbackend.controller.user;

import com.lettrip.lettripbackend.controller.ApiResponse;
import com.lettrip.lettripbackend.controller.user.dto.UserDto;
import com.lettrip.lettripbackend.security.CurrentUser;
import com.lettrip.lettripbackend.security.CustomUserDetails;
import com.lettrip.lettripbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public UserDto.Response showUserProfile(
            @CurrentUser CustomUserDetails customUserDetails
    ) {
        return userService.getUserProfile(customUserDetails.getId());
    }

    @PostMapping("/update/image")
    public ApiResponse updateProfileImage(
            @CurrentUser CustomUserDetails customUserDetails,
            @RequestPart(value = "file", required = true) MultipartFile multipartFile
    ) {
        return userService.updateImageUrl(customUserDetails.getId(), multipartFile);
    }

    @PostMapping("/update/nickname")
    public ApiResponse updateNickname(
            @CurrentUser CustomUserDetails customUserDetails,
            @RequestBody UserDto.Request request
    ) {
        return userService.updateNickname(customUserDetails.getId(), request);
    }

    @PostMapping("/update/password")
    public ApiResponse updatePassword(
            @CurrentUser CustomUserDetails customUserDetails,
            @RequestBody UserDto.Request request
    ) {
        return userService.updateNickname(customUserDetails.getId(), request);
    }

    @PostMapping("/check/password")
    public ApiResponse checkPassword(
            @CurrentUser CustomUserDetails customUserDetails,
            @RequestBody UserDto.Request request
    ) {
        return userService.checkIfPasswordCorrect(customUserDetails.getId(), request);
    }
}
