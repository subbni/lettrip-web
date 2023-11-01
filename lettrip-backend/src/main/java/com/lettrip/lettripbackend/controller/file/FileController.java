package com.lettrip.lettripbackend.controller.file;

import com.lettrip.lettripbackend.controller.ApiResponse;
import com.lettrip.lettripbackend.controller.travel.dto.TravelDto;
import com.lettrip.lettripbackend.security.CurrentUser;
import com.lettrip.lettripbackend.security.CustomUserDetails;
import com.lettrip.lettripbackend.service.FileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/file")
@RestController
public class FileController {
    private final FileService fileService;
    @PostMapping("/upload")
    public ApiResponse createTravel(
            @CurrentUser CustomUserDetails customUserDetails,
            @RequestPart(value="file", required = true) MultipartFile multipartfile
    ) {
        return new ApiResponse(true,"success",fileService.uploadImageFile(multipartfile,"chat/"));
    }
}
