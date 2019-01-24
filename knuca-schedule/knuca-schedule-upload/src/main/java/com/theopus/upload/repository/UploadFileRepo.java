package com.theopus.upload.repository;

import com.theopus.upload.entity.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadFileRepo extends JpaRepository<UploadFile, Long> {
}
