package com.lettrip.lettripbackend.repository;

import com.lettrip.lettripbackend.domain.meetup.MeetUpPost;
import com.lettrip.lettripbackend.domain.meetup.Poke;
import com.lettrip.lettripbackend.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PokeRepository extends JpaRepository<Poke,Long> {
    Optional<Poke> findByUser(User user);
    Optional<Poke> findByUserAndMeetUpPost(User user, MeetUpPost meetUpPost);
    Page<Poke> findAllByUser(User user, Pageable pageable);
    List<Poke> findAllByUser(User user);
    Page<Poke> findAllByMeetUpPost(MeetUpPost meetUpPost, Pageable pageable);
    List<Poke> findAllByMeetUpPost(MeetUpPost meetUpPost);
    @Query("SELECT p.meetUpPost FROM Poke p WHERE p IN :pokeList")
    Page<MeetUpPost> findMeetUpPostsByPokeList(List<Poke> pokeList, Pageable pageable);
}
