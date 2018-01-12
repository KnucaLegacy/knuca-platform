package com.theopus.restservice.config;

import com.theopus.repository.service.CurriculumService;
import com.theopus.upload.controller.UploadController;
import com.theopus.upload.service.UploadService;
import com.theopus.upload.service.UploadServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(Profiles.UPLOAD)
public class UploadConfig {

    @Bean
    public UploadController controller(UploadService uploadService) {
        return new UploadController(uploadService);
    }

    @Bean
    @Autowired
    public UploadService service(CurriculumService service) {
        return new UploadServiceImpl(service);
    }
}
