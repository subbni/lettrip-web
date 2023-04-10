package com.lettrip.lettripbackend.service;

import com.lettrip.lettripbackend.controller.auth.dto.SignUpUser;
import com.lettrip.lettripbackend.domain.user.ProviderType;
import com.lettrip.lettripbackend.domain.user.User;
import com.lettrip.lettripbackend.exception.LettripErrorCode;
import com.lettrip.lettripbackend.exception.LettripException;
import com.lettrip.lettripbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    @Transactional
    public SignUpUser.Response createUser(SignUpUser.Request request) {
        checkIfDuplicatedEmail(request.getEmail());
        return SignUpUser.Response.fromEntity(createUserFromRequest(request));
    }

    private User createUserFromRequest(SignUpUser.Request request) {
        return userRepository.save(User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .name(request.getName())
                .nickname(request.getNickname())
                .imageUrl(request.getImageUrl())
                .providerType(ProviderType.LOCAL)
                .build());
    }

    public void checkIfDuplicatedEmail(String email) {
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    throw new LettripException(LettripErrorCode.DUPLICATED_EMAIL);
                });
    }
}


