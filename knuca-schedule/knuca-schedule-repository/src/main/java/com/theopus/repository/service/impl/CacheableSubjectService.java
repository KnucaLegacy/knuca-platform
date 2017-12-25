package com.theopus.repository.service.impl;

import com.theopus.entity.schedule.Subject;
import com.theopus.repository.jparepo.SubjectRepository;
import com.theopus.repository.service.SubjectService;
import com.theopus.repository.specification.SubjectSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class CacheableSubjectService implements SubjectService {

    private static final Logger LOG = LoggerFactory.getLogger(CacheableSubjectService.class);

    private SubjectRepository repository;

    public CacheableSubjectService(SubjectRepository subjectRepository) {
        this.repository = subjectRepository;
    }

    @Cacheable("subjects")
    @Override
    public Subject save(Subject subject) {
        Subject saved = findByName(subject.getName());
        if (saved != null) {
            return saved;
        }
        return repository.save(subject);
    }

    @Cacheable("subjects")
    @Override
    public Subject findByName(String name) {
        return (Subject) repository.findOne(SubjectSpecification.getByName(name));
    }

    @Override
    public Long count() {
        return repository.count();
    }

    @Override
    public Collection<Subject> getAll() {
        return repository.findAll();
    }

    @Override
    public void delete(Subject object) {

    }

    @CacheEvict(value = "subjects", allEntries = true)
    @Override
    public void flush() {
        LOG.info("Cleared 'subjects' cache.");
    }
}
