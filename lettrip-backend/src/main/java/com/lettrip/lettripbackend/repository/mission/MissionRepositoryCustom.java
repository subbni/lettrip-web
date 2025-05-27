package com.lettrip.lettripbackend.repository.mission;

import com.lettrip.lettripbackend.constant.MissionType;
import com.lettrip.lettripbackend.controller.mission.dto.RankingDto;

import java.time.LocalDate;
import java.util.List;

public interface MissionRepositoryCustom {
    List<RankingDto.Response> getMissionCountByUser(MissionType missionType, LocalDate from, LocalDate to);

}
