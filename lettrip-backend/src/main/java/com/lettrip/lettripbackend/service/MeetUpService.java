package com.lettrip.lettripbackend.service;

import com.lettrip.lettripbackend.constant.MeetUpPostStatus;
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
import com.lettrip.lettripbackend.mongo.domain.ChatRoom;
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
    private final ChatRoomService chatRoomService;
    private final MeetUpRepository meetUpRepository;
    private final MeetUpCodeRepository meetUpCodeRepository;

    @Transactional
    public ApiResponse saveMeetUp(CreateMeetUp.Request request, Long userId) {
        MeetUpPost meetUpPost = meetUpPostService.findMeetUpPostById(request.getMeetUpPostId());
        checkProcessBeforeMeetUpCreation(meetUpPost,userId);
        MeetUp meetUp = meetUpRepository.save(
                MeetUp.builder()
                        .meetUpPost(meetUpPost)
                        .meetUpDate(request.getMeetUpDate())
                        .writeUser(meetUpPost.getUser())
                        .requestUser(userService.findUserById(request.getParticipantId()))
                        .build()

        );
        processAfterMeetUpCreation(meetUp,meetUpPost,request.getRoomId());
        return new ApiResponse(true, "약속이 생성되었습니다.");
    }

    private void checkProcessBeforeMeetUpCreation(MeetUpPost meetUpPost, Long userId) {
        // 1. 현재 요청하는 사람이 meetUpPost 작성자인지 확인
        MeetUpPostService.checkIfWriter(meetUpPost, userId);
        // 2. 이미 MeetUp이 존재하는지 확인
        if (hasMeetUp(meetUpPost)) {
            throw new LettripException(LettripErrorCode.CANNOT_BE_CREATED_MULTIPLE_TIMES,
                    "이미 생성된 약속이 존재합니다.");
        }
    }

    private void processAfterMeetUpCreation(MeetUp meetUp, MeetUpPost meetUpPost,String roomId) {
        // 1. MeetUpPost 관련 처리
        meetUpPost.setMeetUp(meetUp);
        meetUpPost.setMeetUpPostStatus(MeetUpPostStatus.SCHEDULED);
        // 2. 관련된 Poke 상태 변경
        pokeService.updatePokeStatusOnMeetUpCreation(meetUp);
        // 3. 채팅방 관련 처리
        ChatRoom chatRoom = chatRoomService.findChatRoomById(roomId);
        chatRoom.setMeetUpId(meetUp.getId());
    }

    @Transactional
    public ApiResponse cancelMeetUp(Long meetUpId, Long userId) {
        User user = userService.findUserById(userId);
        MeetUp meetUp = findMeetUpById(meetUpId);
        if(meetUp.getWriteUser()!=user && meetUp.getRequestUser()!=user) {
            throw new LettripException(LettripErrorCode.UNAUTHORIZED_ACCESS,"당사자만 가능한 작업입니다.");
        }
        meetUpRepository.save(meetUp.setMeetUpStatus(MeetUpStatus.CANCELLED));
        processAfterMeetUpCancel(meetUp,user);
        return new ApiResponse(true,"약속이 취소되었습니다.");
    }

    @Transactional
    public void processAfterMeetUpCancel(MeetUp meetUp,User user) {
        user.addMeetUpCancelledCount();
        User participant = meetUp.getWriteUser().equals(user)? meetUp.getRequestUser(): meetUp.getWriteUser();
        participant.addMeetUpCancelledCount();
    }
    @Transactional
    public ApiResponse sendMeetUpCode(Long meetUpId, Long userId) {
        User user = userService.findUserById(userId);
        MeetUp meetUp = findMeetUpById(meetUpId);

        if(hasMeetUpCode(meetUp)) {
            return new ApiResponse(true,"인증코드입니다.", findMeetUpCodeByMeetUpId(meetUp.getId()).getCode());
        } else {
            return new ApiResponse(true,"인증코드가 생성되었습니다.",createMeetUpCode(user,meetUp));
        }
    }

    private void checkProcessBeforeMeetUpCodeCreation(User user, MeetUp meetUp) {
        if(meetUp.getWriteUser()!=user && meetUp.getRequestUser()!=user) {
            throw new LettripException(LettripErrorCode.UNAUTHORIZED_ACCESS,"당사자만 가능한 작업입니다.");
        }
        if(!meetUp.getMeetUpStatus().equals(MeetUpStatus.PENDING)) {
            throw new LettripException(LettripErrorCode.CANNOT_BE_CREATED_MULTIPLE_TIMES,"더 이상 약속을 생성할 수 없습니다.");
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
        User user = userService.findUserById(userId);
        MeetUp meetUp = findMeetUpById(request.getMeetUpId());
        MeetUpCode meetUpCode = findMeetUpCodeByMeetUpId(meetUp.getId());
        if(meetUpCode.getCode().equals(request.getCode())) {
            meetUpRepository.save(meetUp.setMeetUpStatus(MeetUpStatus.COMPLETED));
            return new ApiResponse(true,"인증이 완료되었습니다.");
        } else {
            return new ApiResponse(false,"인증에 실패했습니다. 잘못된 인증코드입니다.");
        }
    }

    @Transactional
    public void processAfterMeetUpCompletion(MeetUp meetUp,User user) {
        user.addMeetUpCompletedCount();
        User participant = meetUp.getWriteUser().equals(user)? meetUp.getRequestUser(): meetUp.getWriteUser();
        participant.addMeetUpCompletedCount();
    }
    public MeetUpDto.Response getMeetUpById(Long meetUpId, Long userId) {
        User user = userService.findUserById(userId);
        MeetUp meetUp = findMeetUpById(meetUpId);
        return new MeetUpDto.Response(meetUp);
    }

    public MeetUp findMeetUpById(Long meetUpId) {
        return meetUpRepository.findById(meetUpId)
                .orElseThrow(()-> {
                    throw new ResourceNotFoundException("MeetUp","meetUpId", meetUpId);
                });
    }

    private MeetUp findMeetUpByMeetUpPost(MeetUpPost meetUpPost) {
        return meetUpRepository.findByMeetUpPost(meetUpPost).orElse(null);
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
