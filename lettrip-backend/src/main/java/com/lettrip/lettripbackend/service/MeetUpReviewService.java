package com.lettrip.lettripbackend.service;

import com.lettrip.lettripbackend.constant.MeetUpStatus;
import com.lettrip.lettripbackend.controller.ApiResponse;
import com.lettrip.lettripbackend.controller.meetUpReview.dto.CreateMeetUpReview;
import com.lettrip.lettripbackend.controller.meetUpReview.dto.ShowMeetUpReview;
import com.lettrip.lettripbackend.domain.meetup.MeetUp;
import com.lettrip.lettripbackend.domain.meetup.MeetUpReview;
import com.lettrip.lettripbackend.domain.user.User;
import com.lettrip.lettripbackend.exception.LettripErrorCode;
import com.lettrip.lettripbackend.exception.LettripException;
import com.lettrip.lettripbackend.repository.MeetUpReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MeetUpReviewService {
    private final UserService userService;
    private final MeetUpService meetUpService;
    private final MeetUpReviewRepository meetUpReviewRepository;
    @Transactional
    public ApiResponse saveMeetUpReview(CreateMeetUpReview.Request request, Long userId) {
        User user = userService.findUserById(userId);
        MeetUp meetUp = meetUpService.findMeetUpById(request.getMeetUpId());
        checkProcessBeforeMeetUpReviewCreation(user,meetUp);
        meetUpReviewRepository.save(
                MeetUpReview.builder()
                        .meetUp(meetUp)
                        .meetUpStatus(meetUp.getMeetUpStatus())
                        .writeUser(user)
                        .objectUser(getObjectUser(user,meetUp))
                        .content(request.getContent())
                        .build()
        );
        return new ApiResponse(true,"한줄평 생성이 완료되었습니다.");
    }

    private void checkProcessBeforeMeetUpReviewCreation(User user, MeetUp meetUp) {
        if(meetUp.getWriteUser()!=user && meetUp.getRequestUser()!=user) {
            throw new LettripException(LettripErrorCode.UNAUTHORIZED_ACCESS,"당사자만 가능한 작업입니다.");
        }
        if(meetUp.getMeetUpStatus() == MeetUpStatus.PENDING) {
            throw  new LettripException(LettripErrorCode.INVALID_REQUEST,"약속이 취소되거나 성사된 후에만 가능한 작업입니다.");
        }
        if(userHasMeetUpReview(user,meetUp)) {
            throw new LettripException(LettripErrorCode.CANNOT_BE_CREATED_MULTIPLE_TIMES,
                    "이미 생성된 한줄평이 존재합니다.");
        }
    }

    private User getObjectUser(User currentUser, MeetUp meetUp) {
        return meetUp.getWriteUser() == currentUser? meetUp.getRequestUser() : meetUp.getWriteUser();
    }

    // 사용자가 받은 meetUpReview 조회 : 필터링 by MeetUpStatus
    public Page<ShowMeetUpReview.Response> getUserMeetUpReviewByMeetUpStatus(Long userId, String meetUpStatus, Pageable pageable) {
        User user = userService.findUserById(userId);
        Page<MeetUpReview> page = meetUpReviewRepository.findAllByObjectUserAndMeetUpStatus(
                user, MeetUpStatus.valueOf(meetUpStatus), pageable
        );
        return new PageImpl<>(
                meetUpReviewToDto(page.getContent()),
                pageable,
                page.getTotalElements()
        );
    }

    private List<ShowMeetUpReview.Response> meetUpReviewToDto(List<MeetUpReview> meetUpReviewList) {
        return meetUpReviewList.stream()
                .map(ShowMeetUpReview.Response::new)
                .collect(Collectors.toList());
    }

    private Boolean userHasMeetUpReview(User user, MeetUp meetUp) {
        MeetUpReview meetUpReview = meetUpReviewRepository.findByMeetUpAndWriteUser(meetUp,user).orElse(null);
        return meetUpReview!=null;
    }
}
