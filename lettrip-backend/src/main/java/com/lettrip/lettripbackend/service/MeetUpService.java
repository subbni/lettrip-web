package com.lettrip.lettripbackend.service;

import com.lettrip.lettripbackend.controller.ApiResponse;
import com.lettrip.lettripbackend.controller.meetUp.dto.CreateMeetUp;
import com.lettrip.lettripbackend.domain.meetup.MeetUp;
import com.lettrip.lettripbackend.domain.meetup.MeetUpPost;
import com.lettrip.lettripbackend.exception.LettripErrorCode;
import com.lettrip.lettripbackend.exception.LettripException;
import com.lettrip.lettripbackend.exception.ResourceNotFoundException;
import com.lettrip.lettripbackend.repository.MeetUpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MeetUpService {
    private final UserService userService;
    private final MeetUpPostService meetUpPostService;
    private final MeetUpRepository meetUpRepository;

    @Transactional
    public ApiResponse saveMeetUp(CreateMeetUp.Request request, Long userId) {
        // 1. 현재 요청하는 사람이 meetUpPost 작성자인지 확인
        MeetUpPost meetUpPost = meetUpPostService.findMeetUpPostById(request.getMeetUpPostId());
        MeetUpPostService.checkIfWriter(meetUpPost, userId);
        // 2. 이미 MeetUp이 존재하는지 확인
        if (findMeetUpByMeetUpPostElseNull(meetUpPost) == null) {
            throw new LettripException(LettripErrorCode.CANNOT_BE_CREATED_MULTIPLE_TIMES,
                    "이미 생성된 약속이 존재합니다.");
        }
        // 3. 존재하지 않는다면, MeetUp을 새로 생성
        meetUpRepository.save(
                MeetUp.builder()
                        .meetUpPost(meetUpPost)
                        .meetUpDate(request.getMeetUpDate())
                        .writeUser(meetUpPost.getUser())
                        .requestUser(userService.findUserById(request.getParticipantId()))
                        .build()

        );
        return new ApiResponse(true, "약속이 생성되었습니다.");
    }


    private MeetUp findMeetUpByMeetUpPostElseNull(MeetUpPost meetUpPost) {
        return meetUpRepository.findByMeetUpPost(meetUpPost).orElse(null);
    }
}
