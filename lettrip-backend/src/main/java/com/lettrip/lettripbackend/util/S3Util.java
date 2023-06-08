package com.lettrip.lettripbackend.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.lettrip.lettripbackend.domain.ImageFile;
import com.lettrip.lettripbackend.exception.LettripErrorCode;
import com.lettrip.lettripbackend.exception.LettripException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Util {
    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.s3.bucket}")
    private String bucket;

    @Transactional
    public List<ImageFile> uploadFiles(List<MultipartFile> multipartFiles) {
        List<ImageFile> imageFiles = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            String originalFileName = multipartFile.getOriginalFilename();
            long fileSize = multipartFile.getSize();

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(multipartFile.getContentType());
            objectMetadata.setContentLength(fileSize);

            String ext = originalFileName.substring(originalFileName.lastIndexOf('.') + 1);
            String storedFileName = UUID.randomUUID() + "." + ext;
            String key = "review/" + storedFileName;

            // putObjectRequest ( 저장할 bucket 이름, 저장할 때 사용할 key, 저장하려는 파일의 inputStream, 파일 metadata)
            try (InputStream inputStream = multipartFile.getInputStream()) {
                amazonS3Client.putObject(
                        new PutObjectRequest(bucket, key, inputStream, objectMetadata)
                                .withCannedAcl(CannedAccessControlList.PublicRead)
                );
                imageFiles.add(ImageFile.builder()
                        .originalFileName(originalFileName)
                        .storedFileName(storedFileName)
                        .storedFileUrl(amazonS3Client.getUrl(bucket, key).toString())
                        .fileSize(fileSize)
                        .build()
                );
            } catch (IOException e) {
                log.error("failed to upload MultiPartFile to S3 => "+e.getMessage());
                throw new LettripException(LettripErrorCode.INTERNAL_SERVER_ERROR);
            }
        }
        return imageFiles;
    }

    @Transactional
    public ImageFile uploadFile(MultipartFile multipartFile,String dirPath) {
        String originalFileName = multipartFile.getOriginalFilename();
        long fileSize = multipartFile.getSize();

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(fileSize);

        String ext = originalFileName.substring(originalFileName.lastIndexOf('.') + 1);
        String storedFileName = UUID.randomUUID() + "." + ext;
        String key = dirPath + storedFileName;

        // putObjectRequest ( 저장할 bucket 이름, 저장할 때 사용할 key, 저장하려는 파일의 inputStream, 파일 metadata)
        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(
                    new PutObjectRequest(bucket, key, inputStream, objectMetadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead)
            );

            return ImageFile.builder()
                    .originalFileName(originalFileName)
                    .storedFileName(storedFileName)
                    .storedFileUrl(amazonS3Client.getUrl(bucket, key).toString())
                    .fileSize(fileSize)
                    .build();

        } catch (IOException e) {
            log.error("failed to upload MultiPartFile to S3 => "+e.getMessage());
            throw new LettripException(LettripErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public void deleteFiles(List<String> keys) {
        try {
            for (String key : keys) {
                amazonS3Client.deleteObject(bucket, "review/"+key);
            }
        } catch (Exception e) {
            log.error("failed to delete S3 files => "+e.getMessage());
            throw new LettripException(LettripErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
