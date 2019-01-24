package com.theopus.upload.service.impl;

import com.theopus.upload.entity.UploadEvent;
import com.theopus.upload.repository.UploadEventRepo;
import com.theopus.upload.service.UploadEventService;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UploadEventServiceImpl implements UploadEventService {
    private final UploadEventRepo repo;

    public UploadEventServiceImpl(UploadEventRepo repo) {
        this.repo = repo;
    }

    @Override
    public long count() {
        return repo.count();
    }

    @Override
    public UploadEvent save(UploadEvent uploadEvent) {
        UploadEvent save = repo.save(uploadEvent);
        return save;
    }

    @Override
    public UploadEvent get(Long id) {
        return Optional.ofNullable(repo.findOne(id))
                .orElseThrow(() -> new EntityNotFoundException("Upload Event with id " + id + "not found"));
    }

    @Override
    public List<UploadEvent> getAll() {
        return repo.findAll();
    }

    @Override
    public UploadEvent delete(UploadEvent event) {
        repo.delete(event);
        return event;
    }

    @Override
    public UploadEvent updateStatus(UploadEvent event, UploadEvent.Status status) {
        event.setStatus(status);
        return repo.save(event);
    }
}
