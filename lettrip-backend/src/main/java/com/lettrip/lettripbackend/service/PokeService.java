package com.lettrip.lettripbackend.service;

import com.lettrip.lettripbackend.controller.ApiResponse;
import com.lettrip.lettripbackend.controller.poke.dto.PokeDto;
import com.lettrip.lettripbackend.domain.meetup.MeetUpPost;
import com.lettrip.lettripbackend.domain.meetup.Poke;
import com.lettrip.lettripbackend.domain.user.User;
import com.lettrip.lettripbackend.exception.ResourceNotFoundException;
import com.lettrip.lettripbackend.repository.PokeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PokeService {
    private final UserService userService;
    private final MeetUpPostService meetUpPostService;
    private final PokeRepository pokeRepository;

    @Transactional
    public ApiResponse savePoke(PokeDto.Request request, Long userId) {
        User user = userService.findUserById(userId);
        MeetUpPost meetUpPost = meetUpPostService.findMeetUpPostById(request.getMeetUpPostId());
        if(!hasPoke(user,meetUpPost)) {
            pokeRepository.save(
                    Poke.builder()
                            .user(user)
                            .meetUpPost(meetUpPost)
                            .briefMessage(request.getBriefMessage())
                            .build()
            );
        }

        return new ApiResponse(true,"찌르기 요청이 완료되었습니다.");
    }

    @Transactional
    public ApiResponse deletePoke(Long meetUpPostId, Long userId) {
        User user = userService.findUserById(userId);
        MeetUpPost meetUpPost = meetUpPostService.findMeetUpPostById(meetUpPostId);

        Poke poke = pokeRepository.findByUserAndMeetUpPost(
                user, meetUpPost
        ).orElseThrow(()-> {
            throw new ResourceNotFoundException("존재하지 않는 Poke입니다.");
        });
        pokeRepository.delete(poke);
        return new ApiResponse(true,"찌르기 요청이 취소되었습니다.");
    }

    public Page<PokeDto.Response> getAllPokesInMeetUpPost(Long meetUpPostId, Pageable pageable) {
        MeetUpPost meetUpPost = meetUpPostService.findMeetUpPostById(meetUpPostId);
        Page<Poke> page = pokeRepository.findAllByMeetUpPost(meetUpPost, pageable);
        return new PageImpl<>(
                pokeToDto(page.getContent()),
                pageable,
                page.getTotalElements()
        );
    }

    public Page<PokeDto.Response> getAllPokesInUser(Long userId, Pageable pageable) {
        User user = userService.findUserById(userId);
        Page<Poke> page = pokeRepository.findAllByUser(user, pageable);
        return new PageImpl<>(
                pokeToDto(page.getContent()),
                pageable,
                page.getTotalElements()
        );
    }

    private List<PokeDto.Response> pokeToDto(List<Poke> pokeList) {
        return pokeList.stream()
                .map(PokeDto.Response::fromEntity)
                .collect(Collectors.toList());
    }

    public ApiResponse checkPoke(Long meetUpPostId, Long userId) {
        MeetUpPost meetUpPost = meetUpPostService.findMeetUpPostById(meetUpPostId);
        User user = userService.findUserById(userId);
        Poke poke = pokeRepository.findByUserAndMeetUpPost(user, meetUpPost)
                .orElse(null);

        if(hasPoke(user,meetUpPost)) {
            return new ApiResponse(true,"쿸찌른 게시글입니다.");
        } else {
            return new ApiResponse(false,"쿸찌른 적 없는 게시글입니다.");
        }
    }

    public boolean hasPoke(User user, MeetUpPost meetUpPost) {
        Poke poke = pokeRepository.findByUserAndMeetUpPost(user, meetUpPost)
                .orElse(null);

        if(poke == null) {
            return false;
        } else {
            return true;
        }
    }
}
