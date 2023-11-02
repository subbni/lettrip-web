package com.lettrip.lettripbackend.controller.travel;


import com.lettrip.lettripbackend.controller.ApiResponse;
import com.lettrip.lettripbackend.controller.travel.dto.PlaceDto;
import com.lettrip.lettripbackend.domain.travel.Place;
import com.lettrip.lettripbackend.service.PlaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/place")
public class PlaceController {
    private final PlaceService placeService;

    @PostMapping("/create")
    public ApiResponse createPlace(
           @Valid @RequestBody PlaceDto.Request request
    ) {
       return new ApiResponse(true,
               "해당 장소가 저장되었습니다.",
               placeService.savePlace(request).getId());
    }

    @GetMapping
    public PlaceDto.Response showPlaceByLocationPoint(
            @RequestParam String xpoint,
            @RequestParam String ypoint
    ) {
        return placeService.getPlaceByLocationPoint(xpoint,ypoint);
    }

    @GetMapping("/{placeId}")
    public PlaceDto.Response showPlaceById(
            @PathVariable("placeId") Long placeId
    ) {
        return placeService.getPlaceById(placeId);
    }
}
