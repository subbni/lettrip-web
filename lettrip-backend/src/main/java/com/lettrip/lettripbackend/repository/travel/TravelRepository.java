package com.lettrip.lettripbackend.repository.travel;

import com.lettrip.lettripbackend.domain.travel.Travel;
import com.lettrip.lettripbackend.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelRepository extends JpaRepository<Travel, Long>, TravelRepositoryCustom {
    Page<Travel> findByUser(User user, Pageable pageable);
    Page<Travel> findAllByUserAndIsVisited(User user, boolean isVisited, Pageable pageable);
}
