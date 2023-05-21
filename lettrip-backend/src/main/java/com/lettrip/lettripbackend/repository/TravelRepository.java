package com.lettrip.lettripbackend.repository;

import com.lettrip.lettripbackend.domain.travel.Travel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelRepository extends JpaRepository<Travel,Long> {

}
