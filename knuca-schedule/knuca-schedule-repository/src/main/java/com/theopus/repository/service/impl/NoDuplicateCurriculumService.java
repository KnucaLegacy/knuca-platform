package com.theopus.repository.service.impl;

import com.theopus.entity.schedule.*;
import com.theopus.entity.schedule.enums.LessonOrder;
import com.theopus.repository.jparepo.CurriculumRepository;
import com.theopus.repository.service.*;
import com.theopus.repository.specification.CurriculumSpecification;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class NoDuplicateCurriculumService implements CurriculumService {

    private final CourseService courseService;
    private final CircumstanceService circumstanceService;
    private final GroupService groupService;
    private final CurriculumRepository repository;
    private final CurriculumIsolatedCache curriculumIsolatedCache;

    public NoDuplicateCurriculumService(CourseService courseService, CircumstanceService circumstanceService,
                                        GroupService groupService, CurriculumRepository repository,
                                        CurriculumIsolatedCache curriculumIsolatedCache) {
        this.courseService = courseService;
        this.circumstanceService = circumstanceService;
        this.groupService = groupService;
        this.repository = repository;
        this.curriculumIsolatedCache = curriculumIsolatedCache;
    }

    @Override
    public Curriculum save(Curriculum curriculum) {
        Course course = courseService.save(curriculum.getCourse());
        Group group = groupService.save(curriculum.getGroup());
        curriculum.setCourse(course);
        curriculum.setGroup(group);


        Set<Circumstance> circumstances = curriculum.getCircumstances();
        curriculum.setCircumstances(new HashSet<>());
        Curriculum saved = curriculumIsolatedCache.findAndSave(curriculum);
        circumstances.forEach(circumstance -> circumstance.setCurriculum(saved));
        Set<Circumstance> circumstances1 = circumstanceService.saveAll(circumstances);
        saved.getCircumstances().addAll(circumstances1);
        Curriculum save = repository.save(saved);
        return save;
    }

    @Override
    @Transactional
    public List<Curriculum> saveAll(List<Curriculum> curriculumList) {
        return curriculumList.stream()
                .map(this::save)
                .collect(Collectors.toList());
    }

    @Override
    public void flush() {
        courseService.flush();
        circumstanceService.flush();
        groupService.flush();
        curriculumIsolatedCache.flush();
    }

    @Override
    public void delete(Curriculum curriculum) {
        repository.delete(curriculum);
    }

    @Override
    public List<Curriculum> getByGroup(LocalDate date, Group group) {
        return repository.findAll(CurriculumSpecification.withDateAndGroup(date, group));
    }

    @Override
    public List<Curriculum> getWithCourseAtDate(LocalDate date, Course course, LessonOrder order) {
        return repository.findAll(CurriculumSpecification.atDateWithCourse(date, course, order));
    }

    @Override
    public List<Curriculum> getByTeacher(LocalDate date, Teacher teacher) {
        return repository.findAll(CurriculumSpecification.withDateAndTeacher(date, teacher));
    }
}
