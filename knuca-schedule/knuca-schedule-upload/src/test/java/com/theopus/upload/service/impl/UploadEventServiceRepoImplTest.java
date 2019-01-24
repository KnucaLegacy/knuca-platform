package com.theopus.upload.service.impl;

import com.theopus.upload.config.PersistenceConfig;
import com.theopus.upload.constants.FileExtension;
import com.theopus.upload.entity.UploadEvent;
import com.theopus.upload.entity.UploadFile;
import com.theopus.upload.service.UploadEventService;
import com.theopus.upload.service.UploadFileService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = PersistenceConfig.class)
public class UploadEventServiceRepoImplTest {


    @Autowired
    private UploadEventService service;

    @Autowired
    private UploadFileService fileService;

    @Test
    public void get() throws Exception {
        //
    }
}