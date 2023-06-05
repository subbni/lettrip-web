package com.lettrip.lettripbackend.service;

import com.lettrip.lettripbackend.constant.Province;
import com.lettrip.lettripbackend.constant.TravelTheme;
import com.lettrip.lettripbackend.controller.ApiResponse;
import com.lettrip.lettripbackend.controller.travel.dto.ShowTravelList;
import com.lettrip.lettripbackend.controller.travel.dto.TravelDto;
import com.lettrip.lettripbackend.domain.liked.Liked;
import com.lettrip.lettripbackend.constant.LikedType;
import com.lettrip.lettripbackend.domain.travel.Travel;
import com.lettrip.lettripbackend.domain.user.User;
import com.lettrip.lettripbackend.exception.ResourceNotFoundException;
import com.lettrip.lettripbackend.repository.TravelRepository;
import com.lettrip.lettripbackend.repository.specification.TravelSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TravelService {
    private final TravelRepository travelRepository;
    private final UserService userService;
    private final CourseService courseService;

    private final LikedService likedService;

    @Transactional
    public ApiResponse saveTravel(Long userId, TravelDto.Request travelDto) {
        User user = userService.findUserById(userId);
        Travel travel = travelRepository.save(
                Travel.builder()
                        .user(user)
                        .title(travelDto.getTitle())
                        .province(
                                Province.of(travelDto.getProvince())
                        )
                        .city(Province.getCityInProvince(
                                Province.of(travelDto.getProvince()), travelDto.getCity()
                        ))
                        .isVisited(travelDto.getIsVisited())
                        .departDate(travelDto.getDepartDate())
                        .lastDate(travelDto.getLastDate())
                        .totalCost(travelDto.getTotalCost())
                        .numberOfCourses(travelDto.getNumberOfCourses())
                        .travelTheme(
                                TravelTheme.of(travelDto.getTravelTheme())
                        )
                        .build()
        );
        // 각 Course들을 Travel에 저장
        courseService.saveCourses(travel, travelDto.getCourses());
        FileService.resetMultipartFiles();
        return new ApiResponse(true, "여행 코스가 저장되었습니다.");
    }

    public ApiResponse deleteTravel(Long travelId, Long userId) {
        Travel travel = findTravelById(travelId);
        checkIfWriter(travel,userId);
        travelRepository.delete(travel);
        return new ApiResponse(true,"여행 코스가 삭제되었습니다.");
    }

    public TravelDto.Response showTravel(Long travelId) {
        Travel travel = findTravelById(travelId);
        return new TravelDto.Response(travel);
    }

    public void checkIfWriter(Travel travel, Long userId) {
        if(travel.getUser().getId() != userId) {
            throw new SecurityException("작성자만 가능한 작업입니다.");
        }
    }
    public Travel findTravelById(Long travelId) {
        return travelRepository.findById(travelId)
                .orElseThrow(()-> {
            throw new ResourceNotFoundException("Travel","travelId",travelId);
        });
    }

    // 여행 전체 조회
    public Page<ShowTravelList.Response> showAllTravelPage(Pageable pageable) {
        Page<Travel> page = travelRepository.findAll(pageable);
        return new PageImpl<ShowTravelList.Response>(
                travelToListDto(page.getContent()),
                pageable,
                page.getTotalElements()
        );
    }

    // 여행 부분 조회
    public Page<ShowTravelList.Response> showTravelPage(ShowTravelList.Request request, Pageable pageable) {
        Page<Travel> page = travelRepository.findAll(getTravelPageSpec(request),pageable);
        return new PageImpl<ShowTravelList.Response>(
                travelToListDto(page.getContent()),
                pageable,
                page.getTotalElements()
        );
    }

    // 좋아요 누른 여행 조회
    public Page<ShowTravelList.Response> getLikedTravels(Long userId, Pageable pageable) {
        User user = userService.findUserById(userId);
        List<Liked> likedList = likedService.findUserLikedList(user, LikedType.TRAVEL_LIKE);
        List<Travel> travelList = likedList.stream()
                .map((liked)-> {
                    return travelRepository.findById(liked.getTargetId()).
                            orElse(null);
                })
                .toList();
        return new PageImpl<ShowTravelList.Response>(
                travelToListDto(travelList),
                pageable,
                travelList.size()
        );
    }

    // 사용자 작성 여행 조회
    public Page<ShowTravelList.Response> getUserTravelPlanPage(Long userId, Boolean isVisited, Pageable pageable) {
        User user = userService.findUserById(userId);
        Page<Travel> page =  travelRepository.findAll(TravelSpecification.getTravelEqualUserAndIsVisited(user,isVisited),pageable);
        return new PageImpl<>(
                travelToListDto(page.getContent()),
                pageable,
                page.getTotalElements()
        );
    }

    private Specification<Travel> getTravelPageSpec(ShowTravelList.Request request) {

        Specification<Travel> spec
                = Specification.where(TravelSpecification.equalIsVisited(true))
                .and(TravelSpecification.equalProvince((Province.of(request.getProvince()))))
                .and(TravelSpecification.equalCity(request.getCity()));

        if(!request.getTravelTheme().equals("all")) {
            spec = spec.and(TravelSpecification.equalTravelTheme(TravelTheme.of(request.getTravelTheme())));
        }

        if(request.getMinCost()>0 || request.getMaxCost()>0) {
            if(request.getMinCost() <0) {
                spec = spec.and(TravelSpecification.betweenTotalCost(0,request.getMaxCost()));
            } else if(request.getMaxCost() <0) {
                spec = spec.and(TravelSpecification.betweenTotalCost(request.getMinCost(),Long.MAX_VALUE));
            } else {
                spec = spec.and(TravelSpecification.betweenTotalCost(request.getMinCost(), request.getMaxCost()));
            }
        }

        if(request.getMinNumberOfCourses()>0|| request.getMaxNumberOfCourses()>0) {
            if(request.getMinNumberOfCourses() <0) {
                spec = spec.and(TravelSpecification.betweenNumberOfCourses(0,request.getMaxNumberOfCourses()));
            } else if(request.getMaxNumberOfCourses() <0) {
                spec = spec.and(TravelSpecification.betweenNumberOfCourses(request.getMinNumberOfCourses(),Integer.MAX_VALUE));
            } else {
                spec = spec.and(TravelSpecification.betweenNumberOfCourses(request.getMinNumberOfCourses(), request.getMaxNumberOfCourses()));
            }
        }
        return spec;
    }



    private List<ShowTravelList.Response> travelToListDto(List<Travel> travelList) {
        return travelList.stream()
                .map(ShowTravelList.Response::fromEntity)
                .collect(Collectors.toList());
    }
}
