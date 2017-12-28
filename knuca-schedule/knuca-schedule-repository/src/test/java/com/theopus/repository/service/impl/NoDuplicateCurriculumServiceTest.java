package com.theopus.repository.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.theopus.entity.schedule.*;
import com.theopus.entity.schedule.enums.LessonOrder;
import com.theopus.entity.schedule.enums.LessonType;
import com.theopus.repository.conf.DataBaseServiceConfigTest;
import com.theopus.repository.jparepo.*;
import com.theopus.repository.service.CourseService;
import com.theopus.repository.service.CurriculumService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Oleksandr_Tkachov on 25.12.2017.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DataBaseServiceConfigTest.class})
public class NoDuplicateCurriculumServiceTest {

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
        groupRepository.deleteAll();
        courseRepository.deleteAll();
        subjectRepository.deleteAll();
        teacherRepository.deleteAll();
    }

    @Test
    public void sameSave() throws Exception {
        String groupName1 = "test_group_1";
        String subjectName1 = "test_subject_1";
        LessonType lessonType1 = LessonType.CONSULTATION;

        Course course_1 = new Course(new Subject(subjectName1), lessonType1, Collections.emptySet());
        Curriculum curriculum_1 = new Curriculum(course_1, new Group(groupName1), Collections.emptySet());

        Course course_2 = new Course(new Subject(subjectName1), lessonType1, Collections.emptySet());
        Curriculum curriculum_2 = new Curriculum(course_2, new Group(groupName1), Collections.emptySet());

        List<Curriculum> expected = Lists.newArrayList(curriculum_1);
        curriculumService.save(curriculum_1);
        curriculumService.save(curriculum_2);
        List<Curriculum> actual = curriculumRepository.findAll();

        assertEquals(expected, actual);
    }

    @Test
    public void differentGroupSave() throws Exception {
        String groupName1 = "test_group_1";
        String groupName2 = "test_group_2";
        String subjectName1 = "test_subject_1";
        LessonType lessonType1 = LessonType.CONSULTATION;

        Course course_1 = new Course(new Subject(subjectName1), lessonType1, Collections.emptySet());
        Curriculum curriculum_1 = new Curriculum(course_1, new Group(groupName1), Collections.emptySet());

        Course course_2 = new Course(new Subject(subjectName1), lessonType1, Collections.emptySet());
        Curriculum curriculum_2 = new Curriculum(course_2, new Group(groupName2), Collections.emptySet());

        List<Curriculum> expected = Lists.newArrayList(curriculum_1, curriculum_2);
        curriculumService.save(curriculum_1);
        curriculumService.save(curriculum_2);
        List<Curriculum> actual = curriculumRepository.findAll();

        assertEquals(expected, actual);
    }

    @Test
    public void diffetentLessonTypeSave() throws Exception {
        String groupName1 = "test_group_1";
        String subjectName1 = "test_subject_1";
        LessonType lessonType1 = LessonType.CONSULTATION;
        LessonType lessonType2 = LessonType.EXAM;

        Course course_1 = new Course(new Subject(subjectName1), lessonType1, Collections.emptySet());
        Curriculum curriculum_1 = new Curriculum(course_1, new Group(groupName1), Collections.emptySet());

        Course course_2 = new Course(new Subject(subjectName1), lessonType2, Collections.emptySet());
        Curriculum curriculum_2 = new Curriculum(course_2, new Group(groupName1), Collections.emptySet());

        List<Curriculum> expected = Lists.newArrayList(curriculum_1, curriculum_2);
        curriculumService.save(curriculum_1);
        curriculumService.save(curriculum_2);
        List<Curriculum> actual = curriculumRepository.findAll();

        assertEquals(expected, actual);
    }

    @Test
    public void differentSubjects() throws Exception {
        String groupName1 = "test_group_1";
        String subjectName1 = "test_subject_1";
        String subjectName2 = "test_subject_2";
        LessonType lessonType1 = LessonType.CONSULTATION;

        Course course_1 = new Course(new Subject(subjectName1), lessonType1, Collections.emptySet());
        Curriculum curriculum_1 = new Curriculum(course_1, new Group(groupName1), Collections.emptySet());

        Course course_2 = new Course(new Subject(subjectName2), lessonType1, Collections.emptySet());
        Curriculum curriculum_2 = new Curriculum(course_2, new Group(groupName1), Collections.emptySet());

        List<Curriculum> expected = Lists.newArrayList(curriculum_1, curriculum_2);
        curriculumService.save(curriculum_1);
        curriculumService.save(curriculum_2);
        List<Curriculum> actual = curriculumRepository.findAll();

        assertEquals(expected, actual);
    }

    @Test
    public void remove() throws Exception {
        String groupName1 = "test_group_1";
        String subjectName1 = "test_subject_1";
        LessonType lessonType1 = LessonType.CONSULTATION;

        Course course_1 = new Course(new Subject(subjectName1), lessonType1, Collections.emptySet());
        Circumstance circumstance = new Circumstance(LessonOrder.FIRST, new Room("1"), Sets.newHashSet());
        Curriculum curriculum_1 = new Curriculum(course_1, new Group(groupName1), Sets.newHashSet(
               circumstance
        ));
        circumstance.setCurriculum(curriculum_1);

        Course course_2 = new Course(new Subject(subjectName1), lessonType1, Collections.emptySet());
        Curriculum curriculum_2 = new Curriculum(course_2, new Group(groupName1), Sets.newHashSet(
                new Circumstance(LessonOrder.FIRST, new Room("1"), Sets.newHashSet())
        ));

        List<Curriculum> expected = Collections.emptyList();
        curriculumService.save(curriculum_1);
        curriculumService.save(curriculum_2);
        curriculumRepository.deleteAll();
        List<Curriculum> actual = curriculumRepository.findAll();

        assertEquals(expected, actual);
    }

    @Test
    public void saveWithDifferentCircumstances() throws Exception {
        String groupName1 = "test_group_1";
        String subjectName1 = "test_subject_1";
        LessonType lessonType1 = LessonType.CONSULTATION;

        Course course_1 = new Course(new Subject(subjectName1), lessonType1, Collections.emptySet());
        Curriculum curriculum_1 = new Curriculum(course_1, new Group(groupName1), Sets.newHashSet(
                new Circumstance(LessonOrder.FIRST, new Room("1"), Sets.newHashSet())
        ));

        Curriculum curriculum_2 = new Curriculum(course_1, new Group(groupName1), Sets.newHashSet(
                new Circumstance(LessonOrder.FIRST, new Room("2"), Sets.newHashSet())
        ));

        Curriculum curriculum_3 = new Curriculum(course_1, new Group(groupName1), Sets.newHashSet(
                new Circumstance(LessonOrder.FIRST, new Room("3"), Sets.newHashSet())
        ));

        List<Curriculum> expected = Lists.newArrayList(
                new Curriculum(course_1, new Group(groupName1), Sets.newHashSet(
                        new Circumstance(LessonOrder.FIRST, new Room("1"), Sets.newHashSet()),
                        new Circumstance(LessonOrder.FIRST, new Room("2"), Sets.newHashSet()),
                        new Circumstance(LessonOrder.FIRST, new Room("3"), Sets.newHashSet())
                        ))
        );
        expected.get(0).getCircumstances().forEach(circumstance -> circumstance.setCurriculum(curriculum_1));

        curriculumService.saveAll(Lists.newArrayList(
                curriculum_1,
                curriculum_2,
                curriculum_3
        ));
        List<Curriculum> actual = curriculumRepository.findAll();
        actual.forEach(curriculum -> curriculum.getCircumstances().forEach(circumstance -> System.out.println(circumstance.getCurriculum())));
        expected.forEach(curriculum -> curriculum.getCircumstances().forEach(circumstance -> System.out.println(circumstance.getCurriculum())));
        assertEquals(expected, actual);
        assertEquals(expected.get(0).getCircumstances(), actual.get(0).getCircumstances());
    }
}