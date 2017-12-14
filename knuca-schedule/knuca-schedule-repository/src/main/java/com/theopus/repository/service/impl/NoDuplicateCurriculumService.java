package com.theopus.repository.service.impl;

import com.theopus.entity.schedule.Course;
import com.theopus.entity.schedule.Curriculum;
import com.theopus.entity.schedule.Group;
import com.theopus.repository.jparepo.CurriculumRepository;
import com.theopus.repository.service.CircumstanceService;
import com.theopus.repository.service.CourseService;
import com.theopus.repository.service.CurriculumService;
import com.theopus.repository.service.SimpleService;
import com.theopus.repository.specification.CurriculumSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class NoDuplicateCurriculumService implements CurriculumService {

    private CourseService courseService;
    private CircumstanceService circumstanceService;
    private SimpleService<Group> groupService;
    private CurriculumRepository repository;

    @Autowired
    public NoDuplicateCurriculumService(CourseService courseService, CircumstanceService circumstanceService,
                                        SimpleService<Group> groupService, CurriculumRepository repository) {
        this.courseService = courseService;
        this.circumstanceService = circumstanceService;
        this.groupService = groupService;
        this.repository = repository;
    }

    Curriculum save(Curriculum curriculum) {
        Curriculum saved = this.safeSave(curriculum);
        saved.getCircumstances().forEach(circumstance -> circumstance.setCurriculum(saved));
        return repository.save(saved);
    }

    @Cacheable
    public Curriculum safeSave(Curriculum curriculum) {
        Course course = courseService.save(curriculum.getCourse());
        Group group = groupService.save(curriculum.getGroup());
        curriculum.setCourse(course);
        curriculum.setGroup(group);

        Curriculum dbCuriculum = (Curriculum) repository.findAll(CurriculumSpecification.sameCurriculum(curriculum)).get(0);
        if (dbCuriculum != null) {
            return dbCuriculum;
        } else {
            return repository.save(curriculum);
        }
    }
}
