package com.lettrip.lettripbackend.service;

import com.lettrip.lettripbackend.constant.MissionType;
import com.lettrip.lettripbackend.controller.ApiResponse;
import com.lettrip.lettripbackend.controller.mission.dto.MissionDto;
import com.lettrip.lettripbackend.domain.mission.Mission;
import com.lettrip.lettripbackend.domain.travel.Place;
import com.lettrip.lettripbackend.domain.user.User;
import com.lettrip.lettripbackend.repository.mission.MissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MissionService {
    public static final int QR_MISSION_POINT = 500;
    private final MissionRepository missionRepository;
    private final UserService userService;
    private final PlaceService placeService;

    @Transactional
    public ApiResponse saveMission(MissionDto.Request missionDto) {
        // 1. 미션 포인트 저장할 사용자 email로 찾기
        User user = userService.findUserByEmail(missionDto.getEmail());
        // 2. 미션 장소 찾기
        Place place = placeService.findPlaceByLocationPoint(
                placeService.getFormatLocationPoint(
                        String.valueOf(missionDto.getXpoint()),
                        String.valueOf(missionDto.getYpoint())
                )
        );
        // 3. 미션 저장
        missionRepository.save(
                Mission.builder()
                        .user(user)
                        .place(place)
                        .missionType(MissionType.QR_MISSION)
                        .accomplishedDate(missionDto.getAccomplishedDate())
                        .build()
        );
        // 4. 포인트 반영
        user.addPoint(QR_MISSION_POINT);

        return new ApiResponse(true,"미션 포인트 적립이 완료되었습니다.");
    }
}
