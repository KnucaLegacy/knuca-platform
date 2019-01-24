package com.theopus.upload.service.impl;

import com.theopus.upload.entity.UploadEvent;
import com.theopus.upload.entity.UploadFile;
import com.theopus.upload.repository.UploadEventRepo;
import com.theopus.upload.repository.UploadFileRepo;
import com.theopus.upload.service.UploadFileService;

import java.util.List;
import java.util.stream.Collectors;

public class UploadFileServiceImpl implements UploadFileService {

    private final UploadFileRepo repo;
    private final UploadEventRepo eventRepo;

    public UploadFileServiceImpl(UploadFileRepo repo, UploadEventRepo eventRepo) {
        this.repo = repo;
        this.eventRepo = eventRepo;
    }

    @Override
    public long count() {
        return repo.count();
    }

    @Override
    public UploadFile save(UploadFile uploadFile) {
        UploadFile save = repo.save(uploadFile);
        return save;
    }

    @Override
    public UploadFile get(Long id) {
        return repo.findOne(id);
    }

    @Override
    public List<UploadFile> getAll() {
        return repo.findAll();
    }

    @Override
    public UploadFile delete(UploadFile uploadFile) {
        List<UploadEvent> events = uploadFile.getEvents().stream()
                .peek(uploadEvent -> uploadEvent.getFiles().removeIf(uf -> uf.equals(uploadFile)))
                .collect(Collectors.toList());
        eventRepo.save(events);
        repo.delete(uploadFile);
        return uploadFile;
    }
}
