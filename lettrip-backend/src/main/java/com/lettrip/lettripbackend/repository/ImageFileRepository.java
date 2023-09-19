package com.lettrip.lettripbackend.repository;

import com.lettrip.lettripbackend.domain.ImageFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.*;
import java.util.Optional;

public interface ImageFileRepository extends JpaRepository<ImageFile,Long> {
    Optional<ImageFile> findByOriginalFileName(String fileName);

}
