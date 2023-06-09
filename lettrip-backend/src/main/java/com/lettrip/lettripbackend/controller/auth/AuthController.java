package com.lettrip.lettripbackend.controller.auth;

import com.lettrip.lettripbackend.controller.ApiResponse;
import com.lettrip.lettripbackend.controller.auth.dto.LogInUser;
import com.lettrip.lettripbackend.controller.auth.dto.SignUpUser;
import com.lettrip.lettripbackend.security.CurrentUser;
import com.lettrip.lettripbackend.security.CustomUserDetails;
import com.lettrip.lettripbackend.service.AuthService;
import com.lettrip.lettripbackend.service.MailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final MailService mailService;

    @PostMapping("/sign-up")
    public SignUpUser.Response signUp(
            @Valid @RequestBody SignUpUser.Request request
    ) {
        return authService.createUser(request);
    }

    @PostMapping("/login")
    public LogInUser.Response logIn(
            @Valid @RequestBody LogInUser.Request request
    ) {
        return authService.logInUser(request);
    }

    @DeleteMapping("/withdraw")
    public ApiResponse withdraw(
            @CurrentUser CustomUserDetails customUserDetails
            ) {
        return authService.withdrawUser(customUserDetails.getId());
        // TODO: 고려할 사항이 많아서 일단 보류, social login까지 구현한 뒤 구현 필요
    }

    @GetMapping("/email-code/{email}")
    public ApiResponse sendEmailVerificationCode(@PathVariable String email) throws Exception {
        authService.checkIfDuplicatedEmail(email);
        mailService.sendVerificationEmail(email);
        return new ApiResponse(true, "이메일 인증 코드가 메일로 전송되었습니다.");
    }

    @GetMapping("/email-verify/{code}")
    public ApiResponse verifyEmailCode(@PathVariable String code) {
        mailService.verifyEmailCode(code);
        return new ApiResponse(true, "이메일 인증이 완료되었습니다.");
    }
}
