package com.lettrip.lettripbackend.controller.auth;

import com.lettrip.lettripbackend.controller.auth.dto.SignUpUser;
import com.lettrip.lettripbackend.controller.auth.dto.WithdrawUser;
import com.lettrip.lettripbackend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-up")
    public SignUpUser.Response signUp(
            @Valid @RequestBody SignUpUser.Request request
    ) {
        return authService.createUser(request);
    }

    @DeleteMapping("/withdraw")
    public void withdraw() {
        // TODO: 고려할 사항이 많아서 일단 보류, social login까지 구현한 뒤 구현 필요
    }

}
