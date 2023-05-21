package com.lettrip.lettripbackend.service;

import com.lettrip.lettripbackend.constant.Province;
import com.lettrip.lettripbackend.constant.TravelTheme;
import com.lettrip.lettripbackend.controller.ApiResponse;
import com.lettrip.lettripbackend.controller.travel.dto.TravelDto;
import com.lettrip.lettripbackend.domain.travel.Travel;
import com.lettrip.lettripbackend.domain.user.User;
import com.lettrip.lettripbackend.exception.ResourceNotFoundException;
import com.lettrip.lettripbackend.repository.TravelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TravelService {
    private final TravelRepository travelRepository;
    private final UserService userService;
    private final CourseService courseService;

    private final FileService fileService;

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
                        .isVisited(travelDto.isVisited())
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


}
