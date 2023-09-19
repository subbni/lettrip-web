package com.lettrip.lettripbackend.service;

import com.lettrip.lettripbackend.domain.ImageFile;
import com.lettrip.lettripbackend.domain.travel.Review;
import com.lettrip.lettripbackend.domain.travel.Travel;
import com.lettrip.lettripbackend.exception.LettripErrorCode;
import com.lettrip.lettripbackend.exception.LettripException;
import com.lettrip.lettripbackend.exception.ResourceNotFoundException;
import com.lettrip.lettripbackend.repository.ImageFileRepository;
import com.lettrip.lettripbackend.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FileService {
    public static List<MultipartFile> multipartFiles;
    public final S3Util s3Util;
    private final ImageFileRepository imageFileRepository;
    @Transactional
    public void uploadImageFiles(Review review, List<String> fileNames) {
        if(multipartFiles ==  null) {
            return;
        }
        // multipartFiles 내에서 해당 fileName를 가지고 있는 것들만 list로 모아서
        List<ImageFile> imageFiles =
                s3Util.uploadFiles(multipartFiles.stream().filter(file-> fileNames.contains(file.getOriginalFilename())).toList());
        for(ImageFile imageFile : imageFiles) {
            imageFile.setReview(review);
        }
        imageFileRepository.saveAll(imageFiles);
    }

    public String uploadProfileImageFile(MultipartFile multipartFile) {
        ImageFile imageFile =
                s3Util.uploadFile(multipartFile,"profile/");
        return imageFile.getStoredFileUrl();
    }

    public void resetMultipartFiles() {
        FileService.multipartFiles = null;
    }

    public static List<String> getImageFileUrls(List<ImageFile> imageFiles) {
        if(imageFiles == null) {
            return null;
        }
        List<String> fileUrls = new ArrayList<>();
        for(ImageFile imageFile : imageFiles) {
            fileUrls.add(imageFile.getStoredFileUrl());
        }
        return fileUrls;
    }

    public String getImageFileUrl(String fileName) {
        if(fileName == null || fileName.isEmpty()) {
            return null;
        }
        ImageFile imageFile = imageFileRepository.findByOriginalFileName(fileName)
                .orElseThrow(()-> {
                            throw new ResourceNotFoundException("imageFile", "originalFileName", fileName);
                        });
        return imageFile.getStoredFileUrl();
    }

    public void deleteImageFiles(List<ImageFile> imageFiles) {
        if(imageFiles != null) {
            s3Util.deleteFiles(imageFiles.stream().map(ImageFile::getStoredFileName).collect(Collectors.toList()));
        }
    }
}
