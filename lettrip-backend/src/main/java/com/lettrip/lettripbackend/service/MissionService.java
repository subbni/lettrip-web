package com.lettrip.lettripbackend.service;

import com.lettrip.lettripbackend.constant.MissionType;
import com.lettrip.lettripbackend.controller.ApiResponse;
import com.lettrip.lettripbackend.controller.mission.dto.MissionDto;
import com.lettrip.lettripbackend.domain.mission.Mission;
import com.lettrip.lettripbackend.domain.travel.Place;
import com.lettrip.lettripbackend.domain.user.User;
import com.lettrip.lettripbackend.repository.MissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Service
public class MissionService {

    private final MissionRepository missionRepository;
    private final UserService userService;
    private final PlaceService placeService;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ApiResponse saveMission(MissionDto.Request missionDto) {
        User user = userService.findUserByEmail(missionDto.getEmail());
        Place place = placeService.findPlaceByLocationPoint(
                placeService.getFormatLocationPoint(
                        String.valueOf(missionDto.getXpoint()),
                        String.valueOf(missionDto.getYpoint())
                )
        );
        missionRepository.save(
                Mission.builder()
                        .user(user)
                        .place(place)
                        .missionType(MissionType.QR_MISSION)
                        .accomplishedDate(LocalDate.parse(LocalDate.now().format(formatter)))
                        .build()
        );

        return new ApiResponse(true,"미션 적립이 완료되었습니다.");
    }
}
