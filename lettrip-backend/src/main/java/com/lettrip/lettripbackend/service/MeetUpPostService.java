package com.lettrip.lettripbackend.service;

import com.lettrip.lettripbackend.constant.MeetUpPostStatus;
import com.lettrip.lettripbackend.constant.Province;
import com.lettrip.lettripbackend.controller.ApiResponse;
import com.lettrip.lettripbackend.controller.meetUpPost.dto.CreateMeetUpPost;
import com.lettrip.lettripbackend.controller.meetUpPost.dto.ModifyMeetUpPost;
import com.lettrip.lettripbackend.controller.meetUpPost.dto.ShowMeetUpPost;
import com.lettrip.lettripbackend.controller.meetUpPost.dto.ShowMeetUpPostList;
import com.lettrip.lettripbackend.domain.meetup.MeetUpPost;
import com.lettrip.lettripbackend.domain.meetup.Poke;
import com.lettrip.lettripbackend.domain.meetup.QMeetUpPost;
import com.lettrip.lettripbackend.domain.user.User;
import com.lettrip.lettripbackend.exception.ResourceNotFoundException;
import com.lettrip.lettripbackend.repository.PokeRepository;
import com.lettrip.lettripbackend.repository.meetUpPost.MeetUpPostRepository;
import com.querydsl.core.BooleanBuilder;
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
public class MeetUpPostService {
    private final UserService userService;
    private final PlaceService placeService;
    private final TravelService travelService;
    private final PokeRepository pokeRepository;
    private final MeetUpPostRepository meetUpPostRepository;

    @Transactional
    public ApiResponse saveMeetUpPost(CreateMeetUpPost.Request request, Long userId) {
        User user = userService.findUserById(userId);
        MeetUpPost meetUpPost = meetUpPostRepository.save(
                MeetUpPost.builder()
                        .user(user)
                        .meetUpDate(request.getMeetUpDate())
                        .province(
                                Province.of(request.getProvince())
                        )
                        .city(
                                Province.getCityInProvince(
                                        Province.of(request.getProvince()), request.getCity()
                                )
                        )
                        .title(request.getTitle())
                        .content(request.getContent())
                        .isGpsEnabled(request.isGpsEnabled())
                        .place(request.getPlaceId() == null? null : placeService.findById(request.getPlaceId()))
                        .travel(request.getTravelId() == null? null : travelService.findTravelById(request.getTravelId()))
                        .build()
        );
        return new ApiResponse(true,"등록이 완료되었습니다.",meetUpPost.getId());
    }

    @Transactional
    public ApiResponse deleteMeetUpPost(Long meetUpPostId, Long userId) {
        MeetUpPost meetUpPost = findMeetUpPostById(meetUpPostId);
        checkIfWriter(meetUpPost, userId);
        // TODO: 삭제에 관련된 처리 필요
        meetUpPostRepository.delete(meetUpPost);
        return new ApiResponse(true,"삭제가 완료되었습니다.");
    }

    public ApiResponse updateMeetUpPost(ModifyMeetUpPost.Request request, Long userId) {
        MeetUpPost meetUpPost = findMeetUpPostById(request.getId());
        checkIfWriter(meetUpPost,userId);
        meetUpPostRepository.save(
                meetUpPost.update(
                        request.getIsGpsEnabled(),
                        request.getMeetUpDate(),
                        request.getTitle(),
                        request.getContent(),
                        request.getPlaceId() == null? null : placeService.findById(request.getPlaceId()),
                        request.getTravelId() == null? null : travelService.findTravelById(request.getTravelId())
                )
        );

        return new ApiResponse(true,"수정 완료되었습니다.");
    }

    // 단일 조회
    public ShowMeetUpPost.Response showMeetUpPost(Long meetUpPostId) {
        return ShowMeetUpPost.Response.fromEntity(
                findMeetUpPostById(meetUpPostId)
        );
    }

    public MeetUpPost findMeetUpPostById(Long meetUpPostId) {
        return meetUpPostRepository.findById(meetUpPostId)
                .orElseThrow(()-> {
                    throw new ResourceNotFoundException("MeetUpPost","meetUpPostId", meetUpPostId);
                });
    }


    // 필터링 조회 + 전체 조회
    public Page<ShowMeetUpPostList.Response> getMeetUpPostPage(ShowMeetUpPostList.Request request, Pageable pageable) {
        Page<MeetUpPost> page = meetUpPostRepository.findAll(buildCondition(request), pageable);
        return new PageImpl<ShowMeetUpPostList.Response>(
                meetUpPostToDto(page.getContent()),
                pageable,
                page.getTotalElements()
        );
    }


    // 사용자 작성 MeetUpPost 조회
    public Page<ShowMeetUpPostList.Response> getUserMeetUpPost(Long userId, Pageable pageable) {
        User user = userService.findUserById(userId);
        Page<MeetUpPost> page = meetUpPostRepository.findAllByUser(user,pageable);
        return new PageImpl<ShowMeetUpPostList.Response>(
                meetUpPostToDto(page.getContent()),
                pageable,
                page.getTotalElements()
        );
    }

    // 사용자가 Poked 한 MeetUpPost 조회
    public Page<ShowMeetUpPostList.Response> getPokedMeetUpPost(Long userId, Pageable pageable) {
        User user = userService.findUserById(userId);
        List<Poke> pokeList = pokeRepository.findAllByUser(user);
//        List<MeetUpPost> pokedPostList = pokeList.stream()
//                .map((poke)-> {
//                    return meetUpPostRepository.findById(poke.getMeetUpPost().getId())
//                            .orElse(null);
//                }).toList();
        Page<MeetUpPost> page = pokeRepository.findMeetUpPostsByPokeList(pokeList, pageable);
        return new PageImpl<ShowMeetUpPostList.Response>(
                meetUpPostToDto(page.getContent()),
                pageable,
                page.getTotalElements()
        );
    }

    private List<ShowMeetUpPostList.Response> meetUpPostToDto(List<MeetUpPost> meetUpPostList) {
        return meetUpPostList.stream()
                .map(ShowMeetUpPostList.Response::fromEntity)
                .collect(Collectors.toList());
    }

    public static void checkIfWriter(MeetUpPost meetUpPost, Long userId) {
        if(meetUpPost.getUser().getId()!= userId) {
            throw new SecurityException("작성자만 가능한 작업입니다.");
        }
    }

    private BooleanBuilder buildCondition(ShowMeetUpPostList.Request request) {
        QMeetUpPost qMeetUpPost = QMeetUpPost.meetUpPost;
        BooleanBuilder condition = new BooleanBuilder();
        if(request.getIsGpsEnabled()!=null) {
            condition.and(qMeetUpPost.isGpsEnabled.eq(request.getIsGpsEnabled()));
        }
        if(request.getMeetUpPostStatus()!=null) {
            condition.and(qMeetUpPost.meetUpPostStatus.eq(MeetUpPostStatus.valueOf(request.getMeetUpPostStatus())));
        }
        if(request.getProvince()!=null) {
            condition.and(qMeetUpPost.province.eq(Province.of(request.getProvince())));
            if(request.getCity()!=null) {
                condition.and(qMeetUpPost.city.eq(Province.getCityInProvince(Province.of(request.getProvince()),request.getCity())));;
            }
        }
        return condition;
    }
}
