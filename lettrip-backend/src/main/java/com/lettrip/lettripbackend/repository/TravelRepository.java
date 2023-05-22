package com.lettrip.lettripbackend.repository;

import com.lettrip.lettripbackend.domain.travel.Travel;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TravelRepository
        extends JpaRepository<Travel,Long>, JpaSpecificationExecutor<Travel>
{
    Page<Travel> findAll(@Nullable Specification<Travel> spec, Pageable pageable);
}
