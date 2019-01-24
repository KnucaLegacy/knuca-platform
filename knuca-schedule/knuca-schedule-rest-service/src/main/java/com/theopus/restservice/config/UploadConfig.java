package com.theopus.restservice.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(Profiles.UPLOAD)
@ComponentScan("com.theopus.upload.config")
public class UploadConfig {

}
