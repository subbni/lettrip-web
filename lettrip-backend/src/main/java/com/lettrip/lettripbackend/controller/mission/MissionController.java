package com.lettrip.lettripbackend.controller.mission;


import com.lettrip.lettripbackend.controller.ApiResponse;
import com.lettrip.lettripbackend.controller.mission.dto.MissionDto;
import com.lettrip.lettripbackend.service.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/mission")
public class MissionController {
    private final MissionService missionService;
    @PostMapping("/create")
    public ApiResponse createMission(
            @RequestBody MissionDto.Request request
    ) {
        return missionService.saveMission(request);
    }
}
