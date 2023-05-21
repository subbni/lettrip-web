package com.lettrip.lettripbackend.repository;

import com.lettrip.lettripbackend.domain.ImageFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageFileRepository extends JpaRepository<ImageFile,Long> {
}
