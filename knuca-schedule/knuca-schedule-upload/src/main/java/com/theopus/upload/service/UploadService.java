package com.theopus.upload.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface UploadService {

    Integer upload(MultipartFile multipartFile) throws IOException;

    Integer upload(MultipartFile multipartFile, Integer year) throws IOException;
}
