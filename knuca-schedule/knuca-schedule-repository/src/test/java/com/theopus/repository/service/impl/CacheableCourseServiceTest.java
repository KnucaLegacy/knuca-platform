package com.theopus.repository.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.theopus.entity.schedule.Course;
import com.theopus.entity.schedule.Subject;
import com.theopus.entity.schedule.Teacher;
import com.theopus.entity.schedule.enums.LessonType;
import com.theopus.repository.config.DataBaseServiceConfig;
import com.theopus.repository.jparepo.CourseRepository;
import com.theopus.repository.jparepo.SubjectRepository;
import com.theopus.repository.jparepo.TeacherRepository;
import com.theopus.repository.service.CourseService;
import com.theopus.repository.service.TeacherService;
import com.theopus.repository.specification.SubjectSpecification;
import com.theopus.repository.specification.TeacherSpecification;
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
import static org.junit.Assert.fail;

/**
 * Created by Oleksandr_Tkachov on 23.12.2017.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DataBaseServiceConfig.class})
public class CacheableCourseServiceTest {

    @Autowired
    private CourseService courseService;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Before
    public void setUp() throws Exception {
        courseRepository.deleteAll();
        subjectRepository.deleteAll();
        teacherRepository.deleteAll();
        courseService.flush();
    }

    @After
    public void tearDown() throws Exception {
        courseRepository.deleteAll();
        subjectRepository.deleteAll();
        teacherRepository.deleteAll();
        courseService.flush();
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
        courseService.delete(course_1);
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
        teacherService.delete(teacherService.findByName(teacherName_2));
        List<Course> actual = (List<Course>) courseService.getAll();

        assertEquals(expected, actual);
    }

    @Test(expected = org.springframework.dao.DataIntegrityViolationException.class)
    public void deleteTeacherFail() throws Exception {
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
        teacherService.delete(teacherService.findByName(teacherName_2));
        fail();
    }

    @Test
    public void deleteSubject() throws Exception {
        String subjectName = "test_subject_1";
        String teacherName_1 = "test_teacher_1";
        String teacherName_2 = "test_teacher_2";
        LessonType lessonType = LessonType.LAB;
        Course course_1 = new Course(new Subject(subjectName), lessonType, Sets.newHashSet(
                new Teacher(teacherName_1),
                new Teacher(teacherName_2)
        ));

        List<Course> expected = Collections.emptyList();
        courseService.save(course_1);
        courseService.deleteWithSubject((Subject) subjectRepository.findOne(SubjectSpecification.getByName(subjectName)));
        List<Course> actual = (List<Course>) courseService.getAll();
        assertEquals(expected, actual);
    }

    @Test
    public void delete() throws Exception {
        String subjectName = "test_subject_1";
        String teacherName_1 = "test_teacher_1";
        String teacherName_2 = "test_teacher_2";
        LessonType lessonType = LessonType.LAB;
        Course course_1 = new Course(new Subject(subjectName), lessonType, Sets.newHashSet(
                new Teacher(teacherName_1),
                new Teacher(teacherName_2)
        ));
        long expected = 0L;
        courseService.save(course_1);
        courseService.delete(course_1);
        long actual = courseService.count();
        assertEquals(expected, actual);
    }

    @Test
    public void doubleSaveAfterFlush() throws Exception {
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
        courseService.flush();
        courseService.save(course_2);
        List<Course> actual = (List<Course>) courseService.getAll();
        assertEquals(expected, actual);
    }
}