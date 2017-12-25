package com.theopus.repository.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.theopus.entity.schedule.*;
import com.theopus.entity.schedule.enums.LessonOrder;
import com.theopus.entity.schedule.enums.LessonType;
import com.theopus.repository.conf.DataBaseServiceConfigTest;
import com.theopus.repository.jparepo.*;
import com.theopus.repository.service.CircumstanceService;
import com.theopus.repository.service.CourseService;
import com.theopus.repository.service.CurriculumService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by Oleksandr_Tkachov on 25.12.2017.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DataBaseServiceConfigTest.class})
public class AppendableCircumstanceServiceTest {

    @Autowired
    private CircumstanceService circumstanceService;
    @Autowired
    private CurriculumService curriculumService;
    @Autowired
    private CurriculumRepository curriculumRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseService courseService;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private CircumstanceRepository circumstanceRepository;
    @Autowired
    private GroupRepository groupRepository;

    @Before
    public void setUp() throws Exception {
        curriculumService.flush();
        curriculumRepository.deleteAll();
        circumstanceRepository.deleteAll();
        circumstanceService.flush();
        roomRepository.deleteAll();
        courseRepository.deleteAll();
        subjectRepository.deleteAll();
        teacherRepository.deleteAll();
        groupRepository.deleteAll();
    }

    @After
    public void tearDown() throws Exception {
        curriculumService.flush();
        curriculumRepository.deleteAll();
        circumstanceRepository.deleteAll();
        roomRepository.deleteAll();
        courseRepository.deleteAll();
        subjectRepository.deleteAll();
        teacherRepository.deleteAll();
        groupRepository.deleteAll();
    }

    @Test
    public void saveSameCircumstance() throws Exception {
        String test_subject_1 = "test_subject_1";
        LessonType lessonType = LessonType.CONSULTATION;
        LessonOrder lessonOrder1 = LessonOrder.FIFTH;
        String test_room_1 = "test_room_1";
        String test_group_1 = "test_group_1";

        Course course_1 = new Course(new Subject(test_subject_1), lessonType, new HashSet<>());
        Course saved_course = courseService.save(course_1);

        Curriculum curriculum_1 = new Curriculum(saved_course, new Group(test_group_1), new HashSet<>());
        Curriculum saved_curriculum = curriculumService.save(curriculum_1);

        Circumstance circumstance_1 = new Circumstance(lessonOrder1, new Room(test_room_1), new HashSet<>());
       
        Circumstance circumstance_2 = new Circumstance(lessonOrder1, new Room(test_room_1), new HashSet<>());
       

        List<Circumstance> expected = Lists.newArrayList(circumstance_1);
        
        saved_curriculum.setCircumstances(Sets.newHashSet(circumstance_1));
        curriculumService.save(saved_curriculum);
        saved_curriculum.setCircumstances(Sets.newHashSet(circumstance_2));
        curriculumService.save(saved_curriculum);

        List<Circumstance> actual = circumstanceRepository.findAll();

        assertEquals(expected, actual);
    }

    @Test
    public void saveSameCircumstanceFormAppendable() throws Exception {
        String test_subject_1 = "test_subject_1";
        LessonType lessonType = LessonType.CONSULTATION;
        LessonOrder lessonOrder1 = LessonOrder.FIFTH;
        String test_room_1 = "test_room_1";
        String test_group_1 = "test_group_1";

        Course course_1 = new Course(new Subject(test_subject_1), lessonType, new HashSet<>());
        Course saved_course = courseService.save(course_1);

        Curriculum curriculum_1 = new Curriculum(saved_course, new Group(test_group_1), new HashSet<>());
        Curriculum saved_curriculum = curriculumService.save(curriculum_1);

        Circumstance circumstance_1 = new Circumstance(lessonOrder1, new Room(test_room_1), new HashSet<>());
        Circumstance circumstance_2 = new Circumstance(lessonOrder1, new Room(test_room_1), new HashSet<>());


        circumstance_1.setCurriculum(saved_curriculum);
        circumstance_2.setCurriculum(saved_curriculum);
        circumstanceService.save(circumstance_1);
        circumstanceService.save(circumstance_2);

        List<Circumstance> actual = circumstanceRepository.findAll();
        circumstance_1.setCurriculum(saved_curriculum);
        saved_curriculum.getCircumstances().add(circumstance_1);
        List<Circumstance> expected = Lists.newArrayList(circumstance_1);

        assertEquals(expected, actual);
    }

    @Test
    public void appendLocalDates() throws Exception {
        String test_subject_1 = "test_subject_1";
        LessonType lessonType = LessonType.CONSULTATION;
        LessonOrder lessonOrder1 = LessonOrder.FIFTH;
        String test_room_1 = "test_room_1";
        String test_group_1 = "test_group_1";

        Course course_1 = new Course(new Subject(test_subject_1), lessonType, new HashSet<>());
        Course saved_course = courseService.save(course_1);

        Curriculum curriculum_1 = new Curriculum(saved_course, new Group(test_group_1), new HashSet<>());
        Curriculum saved_curriculum = curriculumService.save(curriculum_1);

        Circumstance circumstance_1 = new Circumstance(lessonOrder1, new Room(test_room_1), Sets.newHashSet(
                LocalDate.now()
        ));
        Circumstance circumstance_2 = new Circumstance(lessonOrder1, new Room(test_room_1), Sets.newHashSet(
                LocalDate.now().plusDays(1)
        ));

        List<Circumstance> expected = Lists.newArrayList(
                new Circumstance(
                        lessonOrder1,
                        new Room(test_room_1),
                        Sets.newHashSet(
                                LocalDate.now(),
                                LocalDate.now().plusDays(1)
                        )
                )
        );
        expected.forEach(circumstance -> circumstance.setCurriculum(saved_curriculum));

        circumstance_1.setCurriculum(saved_curriculum);
        circumstance_2.setCurriculum(saved_curriculum);
        circumstanceService.save(circumstance_1);
        circumstanceService.save(circumstance_2);

        List<Circumstance> actual = circumstanceRepository.findAll();
        assertEquals(expected, actual);
    }
}