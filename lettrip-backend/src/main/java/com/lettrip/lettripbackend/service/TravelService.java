package com.lettrip.lettripbackend.service;

import com.lettrip.lettripbackend.constant.Province;
import com.lettrip.lettripbackend.constant.TravelTheme;
import com.lettrip.lettripbackend.controller.ApiResponse;
import com.lettrip.lettripbackend.controller.travel.dto.ShowTravelList;
import com.lettrip.lettripbackend.controller.travel.dto.TravelDto;
import com.lettrip.lettripbackend.constant.LikedType;
import com.lettrip.lettripbackend.domain.travel.QTravel;
import com.lettrip.lettripbackend.domain.travel.Travel;
import com.lettrip.lettripbackend.domain.user.User;
import com.lettrip.lettripbackend.exception.ResourceNotFoundException;
import com.lettrip.lettripbackend.repository.liked.LikedRepository;
import com.lettrip.lettripbackend.repository.travel.TravelRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TravelService {
    private final TravelRepository travelRepository;
    private final LikedRepository likedRepository;
    private final UserService userService;
    private final CourseService courseService;
    private final LikedService likedService;
    private final FileService fileService;

    @Transactional
    public ApiResponse saveTravel(Long userId, TravelDto.Request travelDto,List<MultipartFile> multipartFiles) {
        User user = userService.findUserById(userId);
        FileService.multipartFiles = multipartFiles;
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
        travel.setMainImageUrl(fileService.getImageFileUrl(travelDto.getMainImageName()));
        fileService.resetMultipartFiles();
        return new ApiResponse(true, "여행 코스가 저장되었습니다.",travel.getId());
    }

    @Transactional
    public ApiResponse deleteTravel(Long travelId, Long userId) {
        Travel travel = findTravelById(travelId);
        checkIfWriter(travel,userId);
        likedService.deleteLiked(userId, LikedType.TRAVEL_LIKE, travelId);
        travelRepository.delete(travel);
        return new ApiResponse(true,"여행 코스가 삭제되었습니다.",travel.getId());
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
        Page<Travel> page = travelRepository.findAllByDynamicCondition(buildCondition(request),pageable);
        return new PageImpl<ShowTravelList.Response>(
                travelToListDto(page.getContent()),
                pageable,
                page.getTotalElements()
        );
    }

    public Page<ShowTravelList.Response> getLikedTravels(Long userId, Pageable pageable) {
        User user = userService.findUserById(userId);
        Page<Travel> travelPage = likedRepository.findUserLikedTravel(user, pageable);
        return travelPage.map((ShowTravelList.Response::fromEntity));
    }

    // 사용자 작성 여행 조회
    public Page<ShowTravelList.Response> getUserTravelPlanPage(Long userId, Boolean isVisited, Pageable pageable) {
        User user = userService.findUserById(userId);

        Page<Travel> page =  travelRepository.findAllByUserAndIsVisited(user, isVisited, pageable);
        return new PageImpl<>(
                travelToListDto(page.getContent()),
                pageable,
                page.getTotalElements()
        );
    }

    private BooleanBuilder buildCondition(ShowTravelList.Request request) {
        QTravel qTravel = QTravel.travel;
        BooleanBuilder condition = new BooleanBuilder();

        if (request.getProvince() != null) {
            condition.and(qTravel.province.eq(Province.of(request.getProvince())));
        }
        if (request.getCity() != null) {
            condition.and(qTravel.city.eq(request.getCity()));
        }
        if (request.getTravelTheme() != null) {
            condition.and(qTravel.travelTheme.eq(TravelTheme.of(request.getTravelTheme())));
        }
        if (request.getMinCost() != null && request.getMaxCost() != null) {
            condition.and(qTravel.totalCost.between(request.getMinCost(), request.getMaxCost()));
        }
        if (request.getMinNumberOfCourses() != null && request.getMaxNumberOfCourses() != null) {
            condition.and(qTravel.numberOfCourses.between(request.getMinNumberOfCourses(), request.getMaxNumberOfCourses()));
        }

        return condition;
    }

    private List<ShowTravelList.Response> travelToListDto(List<Travel> travelList) {
        return travelList.stream()
                .map(ShowTravelList.Response::fromEntity)
                .collect(Collectors.toList());
    }
}
