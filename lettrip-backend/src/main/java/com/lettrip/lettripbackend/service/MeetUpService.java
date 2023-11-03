package com.lettrip.lettripbackend.service;

import com.lettrip.lettripbackend.constant.MeetUpStatus;
import com.lettrip.lettripbackend.controller.ApiResponse;
import com.lettrip.lettripbackend.controller.meetUp.dto.CreateMeetUp;
import com.lettrip.lettripbackend.controller.meetUp.dto.MeetUpDto;
import com.lettrip.lettripbackend.controller.meetUp.dto.VerifyMeetUpCode;
import com.lettrip.lettripbackend.domain.meetup.MeetUp;
import com.lettrip.lettripbackend.domain.meetup.MeetUpPost;
import com.lettrip.lettripbackend.domain.user.User;
import com.lettrip.lettripbackend.exception.LettripErrorCode;
import com.lettrip.lettripbackend.exception.LettripException;
import com.lettrip.lettripbackend.exception.ResourceNotFoundException;
import com.lettrip.lettripbackend.mongo.domain.MeetUpCode;
import com.lettrip.lettripbackend.mongo.repository.MeetUpCodeRepository;
import com.lettrip.lettripbackend.repository.MeetUpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MeetUpService {
    private final UserService userService;
    private final MeetUpPostService meetUpPostService;
    private final PokeService pokeService;
    private final MeetUpRepository meetUpRepository;
    private final MeetUpCodeRepository meetUpCodeRepository;

    @Transactional
    public ApiResponse saveMeetUp(CreateMeetUp.Request request, Long userId) {
        // 1. 현재 요청하는 사람이 meetUpPost 작성자인지 확인
        MeetUpPost meetUpPost = meetUpPostService.findMeetUpPostById(request.getMeetUpPostId());
        MeetUpPostService.checkIfWriter(meetUpPost, userId);
        // 2. 이미 MeetUp이 존재하는지 확인
        if (hasMeetUp(meetUpPost)) {
            throw new LettripException(LettripErrorCode.CANNOT_BE_CREATED_MULTIPLE_TIMES,
                    "이미 생성된 약속이 존재합니다.");
        }
        // 3. 존재하지 않는다면, MeetUp을 새로 생성
        MeetUp meetUp = meetUpRepository.save(
                MeetUp.builder()
                        .meetUpPost(meetUpPost)
                        .meetUpDate(request.getMeetUpDate())
                        .writeUser(meetUpPost.getUser())
                        .requestUser(userService.findUserById(request.getParticipantId()))
                        .build()

        );
        // 3. MeetUpPost에 등록
        meetUpPost.setMeetUp(meetUp);
        // 4. 관련된 Poke 상태 변경
        pokeService.updatePokeStatusOnMeetUpCreation(meetUp);
        return new ApiResponse(true, "약속이 생성되었습니다.");
    }

    @Transactional
    public ApiResponse cancelMeetUp(Long meetUpId, Long userId) {
        User user = userService.findUserById(userId);
        MeetUp meetUp = findMeetUpById(meetUpId);
        if(meetUp.getWriteUser()!=user && meetUp.getRequestUser()!=user) {
            throw new LettripException(LettripErrorCode.UNAUTHORIZED_ACCESS,"당사자만 가능한 작업입니다.");
        }
        meetUpRepository.save(meetUp.setMeetUpStatus(MeetUpStatus.CANCELLED));
        return new ApiResponse(true,"약속이 취소되었습니다.");
    }

    public ApiResponse sendMeetUpCode(Long meetUpId, Long userId) {
        User user = userService.findUserById(userId);
        MeetUp meetUp = findMeetUpById(meetUpId);
        if(meetUp.getWriteUser()!=user && meetUp.getRequestUser()!=user) {
            throw new LettripException(LettripErrorCode.UNAUTHORIZED_ACCESS,"당사자만 가능한 작업입니다.");
        }
        if(!meetUp.getMeetUpStatus().equals(MeetUpStatus.PENDING)) {
            throw new LettripException(LettripErrorCode.CANNOT_BE_CREATED_MULTIPLE_TIMES,"더 이상 약속을 생성할 수 없습니다.");
        }
        if(hasMeetUpCode(meetUp)) {
            return new ApiResponse(true,"인증코드 입니다.", findMeetUpCodeByMeetUpId(meetUp.getId()).getCode());
        } else {
            return new ApiResponse(true,"인증코드가 생성되었습니다..",createMeetUpCode(user,meetUp));
        }
    }

    @Transactional
    public String createMeetUpCode(User user, MeetUp meetUp) {
        String code = MailService.createRandomCode(6);
        MeetUpCode meetUpCode = meetUpCodeRepository.save(
                MeetUpCode.builder()
                        .meetUpId(meetUp.getId())
                        .requesterId(user.getId())
                        .performerId(meetUp.getRequestUser().equals(user)? meetUp.getWriteUser().getId() : meetUp.getRequestUser().getId())
                        .code(code)
                        .build()
        );
        return meetUpCode.getCode();
    }

    @Transactional
    public ApiResponse verifyMeetUpCode(VerifyMeetUpCode.Request request, Long userId) {
        MeetUp meetUp = findMeetUpById(request.getMeetUpId());
        MeetUpCode meetUpCode = findMeetUpCodeByMeetUpId(meetUp.getId());
        if(meetUpCode.getCode().equals(request.getCode())) {
            meetUpRepository.save(meetUp.setMeetUpStatus(MeetUpStatus.COMPLETED));
            return new ApiResponse(true,"인증이 완료되었습니다.");
        } else {
            return new ApiResponse(false,"인증에 실패했습니다. 잘못된 인증코드입니다.");
        }
    }

    public MeetUpDto.Response getMeetUpById(Long meetUpId, Long userId) {
        User user = userService.findUserById(userId);
        MeetUp meetUp = findMeetUpById(meetUpId);
        return new MeetUpDto.Response(meetUp);
    }

    private MeetUp findMeetUpByMeetUpPost(MeetUpPost meetUpPost) {
        return meetUpRepository.findByMeetUpPost(meetUpPost).orElse(null);
    }

    private MeetUp findMeetUpById(Long meetUpId) {
        return meetUpRepository.findById(meetUpId)
                .orElseThrow(()-> {
                    throw new ResourceNotFoundException("MeetUp","meetUpId", meetUpId);
                });
    }

    private MeetUpCode findMeetUpCodeByMeetUpId(Long meetUpId) {
        return meetUpCodeRepository.findByMeetUpId(meetUpId)
                .orElseThrow(()-> {
                    throw new ResourceNotFoundException("MeetUp","meetUpId", meetUpId);
                });
    }

    private Boolean hasMeetUp(MeetUpPost meetUpPost) {
        MeetUp meetUp = meetUpRepository.findByMeetUpPost(meetUpPost).orElse(null);
        return meetUp != null;
    }

    private Boolean hasMeetUpCode(MeetUp meetUp) {
        MeetUpCode meetUpCode = meetUpCodeRepository.findByMeetUpId(meetUp.getId()).orElse(null);
        return meetUpCode != null;
    }
}
