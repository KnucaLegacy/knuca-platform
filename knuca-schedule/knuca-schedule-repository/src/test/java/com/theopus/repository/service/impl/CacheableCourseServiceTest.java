package com.theopus.repository.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.theopus.entity.schedule.Course;
import com.theopus.entity.schedule.Subject;
import com.theopus.entity.schedule.Teacher;
import com.theopus.entity.schedule.enums.LessonType;
import com.theopus.repository.jparepo.CourseRepository;
import com.theopus.repository.jparepo.SubjectRepository;
import com.theopus.repository.jparepo.TeacherRepository;
import com.theopus.repository.service.CourseService;
import com.theopus.repository.specification.TeacherSpecification;
import conf.DataBaseServiceConfigTest;
import org.checkerframework.checker.units.qual.A;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Oleksandr_Tkachov on 23.12.2017.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DataBaseServiceConfigTest.class)
public class CacheableCourseServiceTest {

    @Autowired
    private CourseService courseService;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Before
    public void setUp() throws Exception {
        courseService.flush();
        courseRepository.deleteAll();
        teacherRepository.deleteAll();
        subjectRepository.deleteAll();
    }

    @After
    public void tearDown() throws Exception {
        courseService.flush();
        courseRepository.deleteAll();
        teacherRepository.deleteAll();
        subjectRepository.deleteAll();
    }

    @Test
    public void sameCourseSave() throws Exception {
        String subjectName = "test_subject_1";
        String teacherName_1 = "test_teacher_1";
        String teacherName_2 = "test_teacher_2";
        LessonType lessonType = LessonType.LAB;
        Course course_1 = new Course(new Subject(subjectName), lessonType, Sets.newHashSet(
                new Teacher(teacherName_1),
                new Teacher(teacherName_2)
        ));

        Course course_2 = new Course(new Subject(subjectName), lessonType, Sets.newHashSet(
                new Teacher(teacherName_1),
                new Teacher(teacherName_2)
        ));
        List<Course> expected = Collections.singletonList(course_1);
        courseService.save(course_1);
        courseService.save(course_2);
        List<Course> actual = (List<Course>) courseService.getAll();

        assertEquals(expected, actual);
    }

    @Test
    public void sameCourseSave_different_teachers() throws Exception {
        String subjectName = "test_subject_1";
        String teacherName_1 = "test_teacher_1";
        String teacherName_2 = "test_teacher_2";
        LessonType lessonType = LessonType.LAB;
        Course course_1 = new Course(new Subject(subjectName), lessonType, Sets.newHashSet(
                new Teacher(teacherName_1),
                new Teacher(teacherName_2)
        ));

        Course course_2 = new Course(new Subject(subjectName), lessonType, Sets.newHashSet(
                new Teacher(teacherName_1)
        ));
        List<Course> expected = Lists.newArrayList(course_1, course_2);
        courseService.save(course_1);
        courseService.save(course_2);
        List<Course> actual = (List<Course>) courseService.getAll();

        assertEquals(expected, actual);
    }

    @Test
    public void deleteCourseNotAffect() throws Exception {
        String subjectName = "test_subject_1";
        String teacherName_1 = "test_teacher_1";
        String teacherName_2 = "test_teacher_2";
        LessonType lessonType = LessonType.LAB;
        Course course_1 = new Course(new Subject(subjectName), lessonType, Sets.newHashSet(
                new Teacher(teacherName_1),
                new Teacher(teacherName_2)
        ));
        List<Teacher> expected_teachers = Lists.newArrayList(new Teacher(teacherName_1), new Teacher(teacherName_2));
        List<Subject> expected_subject = Lists.newArrayList(new Subject(subjectName));
        courseService.save(course_1);
        courseRepository.delete(course_1);
        List<Teacher> actual_teachers = teacherRepository.findAll();
        List<Subject> actual_subject = subjectRepository.findAll();

        assertEquals(expected_teachers, actual_teachers);
        assertEquals(expected_subject, actual_subject);
        assertEquals(courseRepository.count(), 0);
    }

    @Test
    public void unenrollTeacherNotAffect() throws Exception {
        String subjectName_1 = "test_subject_1";
        String subjectName_2 = "test_subject_1";
        String teacherName_1 = "test_teacher_1";
        String teacherName_2 = "test_teacher_2";
        String teacherName_3 = "test_teacher_3";
        LessonType lessonType = LessonType.LAB;
        Course course_1 = new Course(new Subject(subjectName_1), lessonType, Sets.newHashSet(
                new Teacher(teacherName_1),
                new Teacher(teacherName_2)
        ));

        Course course_2 = new Course(new Subject(subjectName_2), lessonType, Sets.newHashSet(
                new Teacher(teacherName_1),
                new Teacher(teacherName_2),
                new Teacher(teacherName_3)
        ));

        List<Course> expected = Lists.newArrayList(
                new Course(
                        new Subject(subjectName_1), lessonType, Sets.newHashSet(
                        new Teacher(teacherName_1))
                ),
                new Course(new Subject(subjectName_2), lessonType, Sets.newHashSet(
                        new Teacher(teacherName_1),
                        new Teacher(teacherName_3)
                )));
        courseService.save(course_1);
        courseService.save(course_2);
        courseService.unenrollTeacher((Teacher) teacherRepository.findOne(TeacherSpecification.getByName(teacherName_2)));
        List<Course> actual = (List<Course>) courseService.getAll();

        assertEquals(expected, actual);
    }
}