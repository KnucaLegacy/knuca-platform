package com.theopus.repository;


import com.theopus.entity.schedule.*;
import com.theopus.entity.schedule.enums.LessonOrder;
import com.theopus.entity.schedule.enums.LessonType;
import com.theopus.repository.jparepo.CourseRepository;
import com.theopus.repository.jparepo.SubjectRepository;
import com.theopus.repository.jparepo.TeacherRepository;
import com.theopus.repository.specification.CourseSpecification;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//TODO: to remove
@SpringBootApplication
@EntityScan("com.theopus.entity.schedule")
public class JpaConfig {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(JpaConfig.class);
        CourseRepository courseRepository = run.getBean(CourseRepository.class);
        TeacherRepository teacherRepository = run.getBean(TeacherRepository.class);
        SubjectRepository subjectRepository = run.getBean(SubjectRepository.class);
    }

}
