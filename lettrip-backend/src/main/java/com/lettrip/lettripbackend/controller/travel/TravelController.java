package com.lettrip.lettripbackend.controller.travel;

import com.lettrip.lettripbackend.controller.ApiResponse;
import com.lettrip.lettripbackend.controller.travel.dto.ShowTravelList;
import com.lettrip.lettripbackend.controller.travel.dto.TravelDto;
import com.lettrip.lettripbackend.security.CurrentUser;
import com.lettrip.lettripbackend.security.CustomUserDetails;
import com.lettrip.lettripbackend.service.FileService;
import com.lettrip.lettripbackend.service.TravelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * /api/travel/create	POST	여행 코스 등록
 * /api/travel/modify/{travel-id}	PUT	여행 코스 수정
 * /api/travel/delete/{travel-id}	DELETE	여행 코스 삭제
 * /api/travel	GET	여행 코스 리스트 조회
 * /api/travel/{travel-id}	GET	여행 코스 단일 조회
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/travel")
public class TravelController {
    private final TravelService travelService;
    @PostMapping("/review/create")
    public ApiResponse createTravel(
            @CurrentUser CustomUserDetails customUserDetails,
            @Valid @RequestPart(value="travel") TravelDto.Request travelDto,
            @RequestPart(value="files", required = false) List<MultipartFile> multipartFiles
            ) {
        // FileService.multipartFiles = multipartFiles;
        return  travelService.saveTravel(customUserDetails.getId(),travelDto,multipartFiles);
    }

    @PostMapping("/plan/create")
    public ApiResponse createTravelPlan(
            @CurrentUser CustomUserDetails customUserDetails,
            @Valid @RequestBody  TravelDto.Request travelDto
    ) {
        return  travelService.saveTravel(customUserDetails.getId(),travelDto,null);
    }

    @PutMapping("/modify")
    public void modifyTravel(
            @CurrentUser CustomUserDetails customUserDetails
    ) {

    }

    @DeleteMapping("/delete/{travelId}")
    public ApiResponse deleteTravel(
            @CurrentUser CustomUserDetails customUserDetails,
            @PathVariable("travelId") Long travelId
    ) {
        return travelService.deleteTravel(travelId, customUserDetails.getId());
    }

    @GetMapping("/{travelId}")
    public TravelDto.Response showTravel(
            @CurrentUser CustomUserDetails customUserDetails,
            @PathVariable("travelId") Long travelId
    ) {
        return travelService.showTravel(travelId);
    }

    @GetMapping("/all")
    public Page<ShowTravelList.Response> showAllTravelPage(
            Pageable pageable
    ) {
        return travelService.showAllTravelPage(pageable);
    }

    @GetMapping
    public Page<ShowTravelList.Response> showTravelPage(
        @RequestParam("province") String province,
        @RequestParam("city") String city,
        @RequestParam(value = "minCost", required = false) long minCost,
        @RequestParam(value="maxCost",required = false) long maxCost,
        @RequestParam(value="minNumberOfCourses", required = false) int minNumberOfCourses,
        @RequestParam(value="maxNumberOfCourses", required = false) int maxNumberOfCourses,
        @RequestParam(value="travelTheme", required = false) String travelTheme,
        Pageable pageable
    ) {
        return travelService.showTravelPage(
                ShowTravelList.Request.builder()
                        .province(province)
                        .city(city)
                        .minCost(minCost)
                        .maxCost(maxCost)
                        .minNumberOfCourses(minNumberOfCourses)
                        .maxNumberOfCourses(maxNumberOfCourses)
                        .travelTheme(travelTheme)
                        .build(),
                pageable
        );
    }

    @GetMapping("/my")
    public Page<ShowTravelList.Response> showMyTravelPage(
            @CurrentUser CustomUserDetails customUserDetails,
            @RequestParam("isVisited") Boolean isVisited,
            Pageable pageable
    ) {
        return travelService.getUserTravelPlanPage(customUserDetails.getId(), isVisited, pageable);
    }

    /* 다른 유저의 여행 계획, 기록 글 조회 */
    @GetMapping("/user/{userId}")
    public Page<ShowTravelList.Response> showUserTravelPage(
            @PathVariable Long userId,
            @RequestParam("isVisited") Boolean isVisited,
            Pageable pageable
    ) {
        return travelService.getUserTravelPlanPage(userId, isVisited, pageable);
    }

    @GetMapping("/my/liked")
    public Page<ShowTravelList.Response> showMyLikedTravelPage(
            @CurrentUser CustomUserDetails customUserDetails,
            Pageable pageable
    ) {
        return travelService.getLikedTravels(customUserDetails.getId(),pageable);
    }

}
