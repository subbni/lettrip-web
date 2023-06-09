package com.lettrip.lettripbackend.service;

import com.lettrip.lettripbackend.controller.ApiResponse;
import com.lettrip.lettripbackend.controller.auth.dto.LogInUser;
import com.lettrip.lettripbackend.controller.auth.dto.SignUpUser;
import com.lettrip.lettripbackend.domain.user.ProviderType;
import com.lettrip.lettripbackend.domain.user.Role;
import com.lettrip.lettripbackend.domain.user.User;
import com.lettrip.lettripbackend.exception.LettripErrorCode;
import com.lettrip.lettripbackend.exception.LettripException;
import com.lettrip.lettripbackend.repository.UserRepository;
import com.lettrip.lettripbackend.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtProvider jwtProvider;

    @Transactional
    public SignUpUser.Response createUser(SignUpUser.Request request) {
        checkIfDuplicatedEmail(request.getEmail());
        return SignUpUser.Response.fromEntity(createUserFromRequest(request));
    }

    private User createUserFromRequest(SignUpUser.Request request) {
        return userRepository.save(User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .nickname(request.getNickname())
                .imageUrl(request.getImageUrl())
                .providerType(ProviderType.LOCAL)
                .role(Role.USER)
                .build());
    }

    public void checkIfDuplicatedEmail(String email) {
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    throw new LettripException(LettripErrorCode.DUPLICATED_EMAIL);
                });
    }

    @Transactional
    public LogInUser.Response logInUser(LogInUser.Request request) {
        // 1. 요청 이메일로 회원가입 된 사용자가 있는지 확인
        User user = findUserByEmail(request.getEmail());
        // 2. 사용자 비밀번호와 로그인 요청된 비밀번호가 일치하는지 확인
        comparePassword(user, request.getPassword());
        // 3. 일치할 경우 인증 -> 인증 결과로 토큰 만들어서 응답
        Authentication authentication = authenticateUser(request);
        return new LogInUser.Response(user, jwtProvider.createToken(authentication));
    }

    private Authentication authenticateUser(LogInUser.Request request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }
    private void comparePassword(User user, String requestedPassword) {
        if(!passwordEncoder.matches(requestedPassword,user.getPassword())) {
            throw new LettripException(LettripErrorCode.PASSWORD_NOT_MATCH);
        }
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(()-> {
                    throw new LettripException(LettripErrorCode.USER_NOT_FOUND);
                });

    }


    @Transactional
    public ApiResponse withdrawUser(Long userId) {
        User user = userService.findUserById(userId);
        user.withdraw();
        return new ApiResponse(true,"탈퇴되었습니다.");
    }
}


