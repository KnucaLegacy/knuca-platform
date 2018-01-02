package com.theopus.repository.service.impl;

import com.theopus.entity.schedule.Circumstance;
import com.theopus.entity.schedule.Course;
import com.theopus.entity.schedule.Curriculum;
import com.theopus.entity.schedule.Group;
import com.theopus.repository.jparepo.CurriculumRepository;
import com.theopus.repository.service.*;
import org.springframework.transaction.annotation.Transactional;

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
}
