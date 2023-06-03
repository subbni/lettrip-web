package com.lettrip.lettripbackend.controller.travel;


import com.lettrip.lettripbackend.controller.ApiResponse;
import com.lettrip.lettripbackend.controller.travel.dto.PlaceDto;
import com.lettrip.lettripbackend.service.PlaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/place")
public class PlaceController {
    private final PlaceService placeService;

    @PostMapping("/create")
    public ApiResponse createPlace(
           @Valid @RequestBody PlaceDto.Request request
    ) {
       placeService.savePlace(request);
       return new ApiResponse(true,"해당 장소가 저장되었습니다.");
    }
}
