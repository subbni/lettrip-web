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
        pokeRepository.save(
                Poke.builder()
                        .user(user)
                        .meetUpPost(
                                meetUpPostService.findMeetUpPostById(request.getMeetUpPostId())
                        )
                        .briefMessage(request.getBriefMessage())
                        .build()
        );
        return new ApiResponse(true,"찌르기 요청이 완료되었습니다.");
    }

    @Transactional
    public ApiResponse deletePoke(PokeDto.Request request, Long userId) {
        User user = userService.findUserById(userId);
        MeetUpPost meetUpPost = meetUpPostService.findMeetUpPostById(request.getMeetUpPostId());

        Poke poke = pokeRepository.findByUserAndAndMeetUpPost(
                user, meetUpPost
        ).orElseThrow(()-> {
            throw new ResourceNotFoundException("Poke");
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

    private List<PokeDto.Response> pokeToDto(List<Poke> pokeList) {
        return pokeList.stream()
                .map(PokeDto.Response::fromEntity)
                .collect(Collectors.toList());
    }
}
