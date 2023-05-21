package com.lettrip.lettripbackend.controller.travel;

import com.lettrip.lettripbackend.controller.ApiResponse;
import com.lettrip.lettripbackend.controller.travel.dto.TravelDto;
import com.lettrip.lettripbackend.security.CurrentUser;
import com.lettrip.lettripbackend.security.CustomUserDetails;
import com.lettrip.lettripbackend.service.FileService;
import com.lettrip.lettripbackend.service.TravelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * /api/travel/create	POST	여행 코스 등록
 * /api/travel/modify/{travel-id}	PUT	여행 코스 수정
 * /api/travel/delete/{travel-id}	DELETE	여행 코스 삭제
 * /api/travel	POST	여행 코스 리스트 조회
 * /api/travel/{travel-id}	GET	여행 코스 단일 조회
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/travel")
public class TravelController {
    private final TravelService travelService;
    @PostMapping("/create")
    public ApiResponse createTravel(
            @CurrentUser CustomUserDetails customUserDetails,
            @Valid @RequestPart(value="travel") TravelDto.Request travelDto,
            @RequestPart(value="files", required = false) List<MultipartFile> multipartFiles
            ) {
        FileService.multipartFiles = multipartFiles;
        return  travelService.saveTravel(customUserDetails.getId(),travelDto);
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
}
