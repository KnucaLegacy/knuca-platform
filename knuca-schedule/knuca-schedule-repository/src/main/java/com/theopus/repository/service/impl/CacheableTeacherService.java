package com.theopus.repository.service.impl;

import com.theopus.entity.schedule.Teacher;
import com.theopus.repository.jparepo.CourseRepository;
import com.theopus.repository.jparepo.TeacherRepository;
import com.theopus.repository.service.CourseService;
import com.theopus.repository.service.TeacherService;
import com.theopus.repository.specification.TeacherSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.Collection;

public class CacheableTeacherService implements TeacherService {

    private static final Logger LOG = LoggerFactory.getLogger(CacheableTeacherService.class);

    private TeacherRepository repository;

    public CacheableTeacherService(TeacherRepository repository) {
        this.repository = repository;
    }

    @Cacheable("teachers")
    @Override
    public Teacher save(Teacher teacher) {
        Teacher saved = findByName(teacher.getName());
        if (saved != null) {
            return saved;
        }
        return repository.save(teacher);
    }

    @Cacheable("teachers")
    @Override
    public Teacher findByName(String name) {
        return (Teacher) repository.findOne(TeacherSpecification.getByName(name));
    }

    @Override
    public Long count() {
        return repository.count();
    }

    @Override
    public Collection<Teacher> getAll() {
        return repository.findAll();
    }

    @Override
    public void delete(Teacher object) {
        repository.delete(object);
    }

    @CacheEvict(value = "teachers", allEntries = true)
    @Override
    public void flush() {
        LOG.info("Cleared 'teachers' cache.");
    }
}
