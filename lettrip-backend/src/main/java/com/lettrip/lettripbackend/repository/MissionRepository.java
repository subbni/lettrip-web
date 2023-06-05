package com.lettrip.lettripbackend.repository;

import com.lettrip.lettripbackend.domain.mission.Mission;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MissionRepository extends JpaRepository<Mission, Long>, JpaSpecificationExecutor<Mission> {
    Page<Mission> findAll(@Nullable Specification<Mission> spec, Pageable pageable);
}
