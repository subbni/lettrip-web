package com.lettrip.lettripbackend.controller.user;

import com.lettrip.lettripbackend.controller.user.dto.UserDto;
import com.lettrip.lettripbackend.security.CurrentUser;
import com.lettrip.lettripbackend.security.CustomUserDetails;
import com.lettrip.lettripbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
}
