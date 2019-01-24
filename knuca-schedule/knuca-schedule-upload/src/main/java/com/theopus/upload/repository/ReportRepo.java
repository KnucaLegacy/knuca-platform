package com.theopus.upload.repository;

import com.theopus.upload.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepo extends JpaRepository<Report, Long> {
}
