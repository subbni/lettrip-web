package com.lettrip.lettripbackend.controller.poke;

import com.lettrip.lettripbackend.controller.ApiResponse;
import com.lettrip.lettripbackend.controller.poke.dto.PokeDto;
import com.lettrip.lettripbackend.security.CurrentUser;
import com.lettrip.lettripbackend.security.CustomUserDetails;
import com.lettrip.lettripbackend.service.PokeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/poke")
@RestController
public class PokeController {
    private final PokeService pokeService;
    @PostMapping("/create")
    public ApiResponse createPoke(
            @CurrentUser CustomUserDetails customUserDetails,
            @RequestBody PokeDto.Request request
            ) {
        return pokeService.savePoke(request, customUserDetails.getId());
    }

    @DeleteMapping("/delete/{meetUpPostId}")
    public ApiResponse deletePoke(
            @CurrentUser CustomUserDetails customUserDetails,
            @PathVariable("meetUpPostId") Long meetUpPostId
            ) {
        return pokeService.deletePoke(meetUpPostId, customUserDetails.getId());
    }

    @GetMapping("/check/{meetUpPostId}")
    public ApiResponse checkPoke(
        @CurrentUser CustomUserDetails customUserDetails,
        @PathVariable("meetUpPostId") Long meetUpPostId
    ) {
        return pokeService.checkPoke(meetUpPostId, customUserDetails.getId());
    }

    @GetMapping("/{meetUpPostId}")
    public Page<PokeDto.Response> showAllPokesInMeetUpPost(
            @PathVariable("meetUpPostId") Long meetUpPostId,
            Pageable pageable
    ) {
        return pokeService.getAllPokesInMeetUpPost(meetUpPostId, pageable);
    }
}
