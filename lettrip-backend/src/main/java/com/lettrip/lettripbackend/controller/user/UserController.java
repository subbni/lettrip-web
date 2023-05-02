package com.lettrip.lettripbackend.controller.user;

import com.lettrip.lettripbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    @GetMapping("/user")
    public String forTest() {
        return "This is for test";
    }

    @GetMapping("/admin")
   public String forAdminTest() {
        return "For admin only";
    }
}
