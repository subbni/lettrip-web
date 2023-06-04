package com.lettrip.lettripbackend.service;

import com.lettrip.lettripbackend.controller.user.dto.UserDto;
import com.lettrip.lettripbackend.domain.user.User;
import com.lettrip.lettripbackend.exception.LettripErrorCode;
import com.lettrip.lettripbackend.exception.LettripException;
import com.lettrip.lettripbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(()-> {
                    throw new LettripException(LettripErrorCode.USER_NOT_FOUND);
                });

    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(()-> {
                    throw new LettripException(LettripErrorCode.USER_NOT_FOUND);
                });
    }

    public UserDto.Response getUserProfile(Long id) {
        return new UserDto.Response(findUserById(id));
    }
}
