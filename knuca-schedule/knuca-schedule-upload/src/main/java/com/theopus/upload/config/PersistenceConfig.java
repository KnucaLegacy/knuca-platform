package com.theopus.upload.config;

import com.theopus.upload.handling.ReportService;
import com.theopus.upload.repository.ReportRepo;
import com.theopus.upload.repository.UploadEventRepo;
import com.theopus.upload.repository.UploadFileRepo;
import com.theopus.upload.service.UploadEventService;
import com.theopus.upload.service.UploadFileService;
import com.theopus.upload.service.impl.UploadEventServiceImpl;
import com.theopus.upload.service.impl.UploadFileServiceImpl;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("com.theopus.upload.repository")
@EntityScan(value = {"com.theopus.upload.entity"})
@Configuration
@EnableAutoConfiguration
public class PersistenceConfig {

    @Bean("reportService")
    public ReportService reportService(ReportRepo reportRepo) {
        return new ReportService(reportRepo);
    }

    @Bean("uploadEventService")
    public UploadEventService uploadEventService(UploadEventRepo repo) {
        return new UploadEventServiceImpl(repo);
    }

    @Bean("uploadFileService")
    public UploadFileService uploadFileService(UploadFileRepo repo, UploadEventRepo eventRepo) {
        return new UploadFileServiceImpl(repo, eventRepo);
    }
}
