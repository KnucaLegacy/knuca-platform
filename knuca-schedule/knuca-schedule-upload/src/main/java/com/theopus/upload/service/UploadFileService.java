package com.theopus.upload.service;

import com.theopus.upload.entity.UploadFile;

import java.util.List;

public interface UploadFileService {
    long count();

    UploadFile save(UploadFile uploadFile);

    UploadFile get(Long id);

    List<UploadFile> getAll();

    UploadFile delete(UploadFile uploadFile);
}
