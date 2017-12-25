package com.theopus.repository.service.impl;

import com.theopus.entity.schedule.Course;
import com.theopus.entity.schedule.Subject;
import com.theopus.entity.schedule.Teacher;
import com.theopus.repository.jparepo.CourseRepository;
import com.theopus.repository.service.CourseService;
import com.theopus.repository.service.SubjectService;
import com.theopus.repository.service.TeacherService;
import com.theopus.repository.specification.CourseSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CacheableCourseService implements CourseService {

    private static final Logger LOG = LoggerFactory.getLogger(CacheableCourseService.class);

    private CourseRepository repository;
    private TeacherService teacherService;
    private SubjectService subjectService;

    public CacheableCourseService(CourseRepository repository, TeacherService teacherService, SubjectService subjectService) {
        this.repository = repository;
        this.teacherService = teacherService;
        this.subjectService = subjectService;
    }

    @Cacheable("courses")
    @Override
    public Course save(Course course) {
        course.setTeachers(course.getTeachers().stream()
                .map(t -> teacherService.save(t))
                .collect(Collectors.toSet()));
        course.setSubject(subjectService.save(course.getSubject()));
        Course one = (Course) repository.findOne(CourseSpecification.sameCourse(course));
        if (!Objects.isNull(one)) {
            return one;
        }

        return repository.save(course);
    }

    @Override
    public Long count() {
        return repository.count();
    }

    @Override
    public Collection<Course> getAll() {
        return repository.findAll();
    }

    @Override
    public Course unenrollTeacher(Course course, Teacher teacher) {
        course.getTeachers().remove(teacher);
        return repository.save(course);
    }

    @Override
    public List<Course> unenrollTeacher(Teacher teacher) {
        return ((List<Course>) repository.findAll(CourseSpecification.withTeacher(teacher)))
                .stream().map(o -> unenrollTeacher(o, teacher))
                .collect(Collectors.toList());
    }

    @Override
    public List<Course> withSubject(Subject subject) {
        return repository.findAll(CourseSpecification.withSubject(subject));
    }

    @Override
    public void deleteWithSubject(Subject subject) {
        withSubject(subject).forEach(this::delete);
    }

    @Override
    public void delete(Course course) {
        repository.delete(course);
    }

    @CacheEvict(value = "courses", allEntries = true)
    @Override
    public void flush() {
        subjectService.flush();
        teacherService.flush();
        LOG.info("Cleared 'courses' cache.");
    }
}
