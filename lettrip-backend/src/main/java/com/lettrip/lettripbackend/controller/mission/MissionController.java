package com.lettrip.lettripbackend.controller.mission;


import com.lettrip.lettripbackend.controller.ApiResponse;
import com.lettrip.lettripbackend.controller.mission.dto.MissionDto;
import com.lettrip.lettripbackend.controller.mission.dto.RankingDto;
import com.lettrip.lettripbackend.service.MissionService;
import com.lettrip.lettripbackend.service.RankingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/mission")
public class MissionController {
    private final MissionService missionService;
    private final RankingService rankingService;
    @PostMapping("/create")
    public ApiResponse createMission(
            @RequestBody MissionDto.Request request
    ) {
        return missionService.saveMission(request);
    }

    @PostMapping("/ranking")
    public List<RankingDto.Response> showMissionRanking(
            @Valid @RequestBody RankingDto.Request request
    ) {
        return rankingService.getRankingList(request);
    }
}
