package com.theopus.repository.service.impl;

import com.theopus.entity.schedule.Course;
import com.theopus.entity.schedule.Curriculum;
import com.theopus.entity.schedule.Group;
import com.theopus.repository.jparepo.CurriculumRepository;
import com.theopus.repository.service.*;
import com.theopus.repository.specification.CurriculumSpecification;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
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


        Curriculum saved = curriculumIsolatedCache.findAndSave(curriculum);
        saved.getCircumstances().forEach(circumstance -> circumstance.setCurriculum(saved));
        saved.setCircumstances(circumstanceService.saveAll(saved.getCircumstances()));
        return repository.save(saved);
    }

    @Override
    public List<Curriculum> saveAll(List<Curriculum> curriculumList){
        return curriculumList.stream()
                .map(this::save)
                .collect(Collectors.toList());
    }

}
