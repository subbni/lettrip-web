package com.lettrip.lettripbackend.service;

import com.lettrip.lettripbackend.controller.ApiResponse;
import com.lettrip.lettripbackend.controller.user.dto.UserDto;
import com.lettrip.lettripbackend.domain.user.User;
import com.lettrip.lettripbackend.exception.LettripErrorCode;
import com.lettrip.lettripbackend.exception.LettripException;
import com.lettrip.lettripbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileService fileService;

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

    @Transactional
    public ApiResponse updateNickname(Long userId, UserDto.Request request) {
        User user = findUserById(userId);
        if(!user.getNickname().equals(request.getNickname())) {
            user.setNickname(request.getNickname());
        }
        userRepository.save(user);
        return new ApiResponse(true,"닉네임 변경이 완료되었습니다.");
    }

    @Transactional
    public ApiResponse updateImageUrl(Long userId, MultipartFile multipartFile) {
        User user = findUserById(userId);
        user.setImageUrl(fileService.uploadImageFile(multipartFile,"profile/"));
        userRepository.save(user);
        return new ApiResponse(true,"프로필 사진 변경이 완료되었습니다.");
    }

    @Transactional
    public ApiResponse changePassword(Long userId, UserDto.Request request) {
        User user = findUserById(userId);
        user.setPassword(request.getPassword());
        userRepository.save(user);
        return new ApiResponse(true,"비밀번호 변경이 완료되었습니다.");
    }

    public ApiResponse checkIfPasswordCorrect(Long userId, UserDto.Request request) {
        User user = findUserById(userId);
        String targetPassword = request.getPassword();
        String realPassword = user.getPassword();
        if(realPassword.isEmpty()) {
            throw new LettripException(LettripErrorCode.PASSWORD_CAN_NOT_BE_CHANGED, "SNS 간편 로그인 계정은 비밀번호 설정이 불가합니다.");
        }
         return passwordEncoder.matches(targetPassword,realPassword) ? new ApiResponse(true,"비밀번호 일치 확인")
                 :  new ApiResponse(false, "비밀번호 불일치");
    }
}
