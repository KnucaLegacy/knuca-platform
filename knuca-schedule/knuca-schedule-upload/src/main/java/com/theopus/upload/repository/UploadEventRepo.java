package com.theopus.upload.repository;

import com.theopus.upload.entity.UploadEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadEventRepo extends JpaRepository<UploadEvent, Long> {
}
