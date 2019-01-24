package com.theopus.upload.service;

import com.theopus.upload.entity.UploadEvent;

import java.util.List;

public interface UploadEventService {
    long count();

    UploadEvent save(UploadEvent uploadEvent);

    UploadEvent get(Long l);

    List<UploadEvent> getAll();

    UploadEvent delete(UploadEvent event);

    UploadEvent updateStatus(UploadEvent event, UploadEvent.Status status);
}
