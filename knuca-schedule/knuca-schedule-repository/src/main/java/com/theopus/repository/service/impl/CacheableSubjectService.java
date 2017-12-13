package com.theopus.repository.service.impl;

import com.theopus.entity.schedule.Subject;
import com.theopus.repository.jparepo.SubjectRepository;
import com.theopus.repository.service.SimpleService;
import com.theopus.repository.specification.SubjectSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class CacheableSubjectService implements SimpleService<Subject> {

    private SubjectRepository repository;

    @Autowired
    public CacheableSubjectService(SubjectRepository subjectRepository) {
        this.repository = subjectRepository;
    }

    @Cacheable
    @Override
    public Subject save(Subject subject){
        Subject saved = findByName(subject.getName());
        if (saved != null){
            return saved;
        }
        return repository.save(subject);
    }

    @Cacheable
    @Override
    public Subject save(String name){
        Subject subject = findByName(name);
        if (subject != null) {
            return subject;
        }
        subject = new Subject();
        subject.setName(name);
        return repository.save(subject);
    }

    @Cacheable
    @Override
    public Subject findByName(String name){
        return (Subject) repository.findOne(SubjectSpecification.getByName(name));
    }

    @Override
    public Long size() {
        return repository.count();
    }

    @Override
    public Collection<Subject> getAll(){
        return repository.findAll();
    }
}
