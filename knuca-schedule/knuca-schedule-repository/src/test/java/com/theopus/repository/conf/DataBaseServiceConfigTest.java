package com.theopus.repository.conf;

import com.theopus.repository.jparepo.*;
import com.theopus.repository.service.*;
import com.theopus.repository.service.impl.*;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableAutoConfiguration
@EnableCaching
@Configuration
@ComponentScan
@EnableJpaRepositories("com.theopus.repository.jparepo")
@EntityScan("com.theopus.entity.schedule")
public class DataBaseServiceConfigTest {

    @Bean("roomService")
    public RoomService roomService(RoomRepository roomRepository) {
        return new CacheableRoomService(roomRepository);
    }

    @Bean("groupService")
    public GroupService groupService(GroupRepository groupRepository) {
        return new CacheableGroupService(groupRepository);
    }

    @Bean("teacherService")
    public TeacherService teacherService(TeacherRepository teacherRepository) {
        return new CacheableTeacherService(teacherRepository);
    }

    @Bean("subjectService")
    public SubjectService subjectService(SubjectRepository subjectRepository) {
        return new CacheableSubjectService(subjectRepository);
    }

    @Bean("circumstanceService")
    public CircumstanceService circumstanceService(CircumstanceRepository circumstanceRepository, RoomService roomService,
                                                   CircumstanceIsolatedCache circumstanceIsolatedCache) {
        return new AppendableCircumstanceService(circumstanceRepository, roomService, circumstanceIsolatedCache);
    }

    @Bean("courseService")
    public CourseService courseService(CourseRepository courseRepository, TeacherService teacherService, SubjectService subjectService) {
        return new CacheableCourseService(courseRepository, teacherService, subjectService);
    }

    @Bean("curriculumService")
    public CurriculumService curriculumService(CourseService courseService, CircumstanceService circumstanceService,
                                               GroupService groupService, CurriculumRepository curriculumRepository,
                                               CurriculumIsolatedCache curriculumIsolatedCache) {
        return new NoDuplicateCurriculumService(courseService, circumstanceService, groupService, curriculumRepository,
                curriculumIsolatedCache);
    }

    @Bean("curriculumIsolatedCache")
    public CurriculumIsolatedCache curriculumIsolatedCache(CurriculumRepository curriculumRepository) {
        return new CurriculumIsolatedCacheImpl(curriculumRepository);
    }

    @Bean("circumstanceIsolatedCache")
    public CircumstanceIsolatedCache circumstanceIsolatedCache(CircumstanceRepository circumstanceRepository,
                                                               CurriculumIsolatedCache curriculumIsolatedCache) {
        return new CircumstanceIsolatedCacheImpl(circumstanceRepository, curriculumIsolatedCache);
    }
}
