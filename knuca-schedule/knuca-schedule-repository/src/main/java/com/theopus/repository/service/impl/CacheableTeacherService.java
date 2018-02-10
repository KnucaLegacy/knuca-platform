package com.theopus.repository.service.impl;

import com.theopus.entity.schedule.Course;
import com.theopus.entity.schedule.Teacher;
import com.theopus.repository.jparepo.CourseRepository;
import com.theopus.repository.jparepo.TeacherRepository;
import com.theopus.repository.service.TeacherService;
import com.theopus.repository.specification.CourseSpecification;
import com.theopus.repository.specification.TeacherSpecification;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class CacheableTeacherService implements TeacherService {

    private static final Logger LOG = LoggerFactory.getLogger(CacheableTeacherService.class);

    private TeacherRepository repository;
    private CourseRepository courseRepository;

    public CacheableTeacherService(TeacherRepository repository, CourseRepository courseRepository) {
        this.repository = repository;
        this.courseRepository = courseRepository;
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
        List<Course> all = courseRepository.findAll(CourseSpecification.withTeacher(object));
        all.stream()
                .peek(course -> course.getTeachers().remove(object))
                .forEach(course -> courseRepository.save(course));
        repository.save(object);
        repository.delete(object);
    }

    @Override
    public Teacher get(Long id) {
        return repository.findOne(id);
    }

    @CacheEvict(value = "teachers", allEntries = true)
    @Override
    public void flush() {
        LOG.debug("Cleared 'teachers' cache.");
    }
}
