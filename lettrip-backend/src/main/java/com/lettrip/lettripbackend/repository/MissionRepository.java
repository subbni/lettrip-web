package com.lettrip.lettripbackend.repository;

import com.lettrip.lettripbackend.domain.mission.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionRepository extends JpaRepository<Mission, Long> {

}