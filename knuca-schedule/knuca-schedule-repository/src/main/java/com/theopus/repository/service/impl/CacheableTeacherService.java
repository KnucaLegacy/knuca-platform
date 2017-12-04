package com.theopus.repository.service.impl;

import com.theopus.entity.schedule.Teacher;
import com.theopus.repository.jparepo.TeacherRepository;
import com.theopus.repository.service.SimpleService;
import com.theopus.repository.specification.TeacherSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import java.util.Collection;

public class CacheableTeacherService implements SimpleService<Teacher> {

    private TeacherRepository repository;

    @Autowired
    public CacheableTeacherService(TeacherRepository repository) {
        this.repository = repository;
    }

    @Cacheable
    @Override
    public Teacher save(Teacher teacher){
        Teacher saved = findByName(teacher.getName());
        if (saved != null){
            return saved;
        }
        return repository.save(teacher);
    }

    @Cacheable
    @Override
    public Teacher save(String name){
        Teacher subject = findByName(name);
        if (subject != null) {
            return subject;
        }
        subject = new Teacher();
        subject.setName(name);
        return repository.save(subject);
    }

    @Cacheable
    @Override
    public Teacher findByName(String name){
        return (Teacher) repository.findOne(TeacherSpecification.getByName(name));
    }

    @Override
    public Long size() {
        return repository.count();
    }

    @Override
    public Collection<Teacher> getAll(){
        return repository.findAll();
    }
}
