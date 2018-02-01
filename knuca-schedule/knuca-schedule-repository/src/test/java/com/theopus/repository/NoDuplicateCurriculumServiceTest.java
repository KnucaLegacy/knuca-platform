package com.theopus.repository;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.theopus.entity.schedule.*;
import com.theopus.entity.schedule.enums.LessonOrder;
import com.theopus.entity.schedule.enums.LessonType;
import com.theopus.repository.config.DataBaseServiceConfig;
import com.theopus.repository.jparepo.*;
import com.theopus.repository.service.CourseService;
import com.theopus.repository.service.CurriculumService;
import com.theopus.repository.service.GroupService;
import com.theopus.repository.specification.RoomSpecification;
import com.theopus.repository.specification.TeacherSpecification;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Oleksandr_Tkachov on 25.12.2017.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DataBaseServiceConfig.class})
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
    @Autowired
    private GroupService groupService;

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
    public void sameSave_deleteGroup() throws Exception {
        String groupName1 = "test_group_1";
        String subjectName1 = "test_subject_1";
        LessonType lessonType1 = LessonType.CONSULTATION;

        Course course_1 = new Course(new Subject(subjectName1), lessonType1, Collections.emptySet());
        Curriculum curriculum_1 = new Curriculum(course_1, new Group(groupName1), Collections.emptySet());

        List<Curriculum> expected = Lists.newArrayList();
        List<Course> expected_course = Lists.newArrayList(course_1);
        curriculumService.save(curriculum_1);
        groupService.delete(groupService.findByName(groupName1));
        List<Curriculum> actual = curriculumRepository.findAll();
        List<Course> actual_course = courseRepository.findAll();

        assertEquals(expected, actual);
        assertEquals(expected_course, actual_course);
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

        List<Curriculum> expected = Lists.newArrayList(curriculum_2);
        curriculumService.save(curriculum_1);
        curriculumService.save(curriculum_2);
        courseService.delete(course_1);
        List<Curriculum> actual = curriculumRepository.findAll();

        assertEquals(expected, actual);
    }

    @Test
    public void diffetentLessonTypeSave_deleteOneCourse() throws Exception {
        String groupName1 = "test_group_1";
        String subjectName1 = "test_subject_1";
        LessonType lessonType1 = LessonType.CONSULTATION;
        LessonType lessonType2 = LessonType.EXAM;

        Course course_1 = new Course(new Subject(subjectName1), lessonType1, Collections.emptySet());
        Curriculum curriculum_1 = new Curriculum(course_1, new Group(groupName1), Collections.emptySet());

        Course course_2 = new Course(new Subject(subjectName1), lessonType2, Collections.emptySet());
        Curriculum curriculum_2 = new Curriculum(course_2, new Group(groupName1), Collections.emptySet());

        List<Curriculum> expected = Lists.newArrayList(curriculum_2);
        curriculumService.save(curriculum_1);
        curriculumService.save(curriculum_2);
        courseService.delete(course_1);
        List<Curriculum> actual = curriculumRepository.findAll();

        assertEquals(expected, actual);
    }

    @Test
    public void diffetentLessonTypeSave_deleteOneCurriculum() throws Exception {
        String groupName1 = "test_group_1";
        String subjectName1 = "test_subject_1";
        LessonType lessonType1 = LessonType.CONSULTATION;
        LessonType lessonType2 = LessonType.EXAM;

        Course course_1 = new Course(new Subject(subjectName1), lessonType1, Collections.emptySet());
        Curriculum curriculum_1 = new Curriculum(course_1, new Group(groupName1), Collections.emptySet());

        Course course_2 = new Course(new Subject(subjectName1), lessonType2, Collections.emptySet());
        Curriculum curriculum_2 = new Curriculum(course_2, new Group(groupName1), Collections.emptySet());

        List<Curriculum> expected = Lists.newArrayList(curriculum_2);
        Curriculum saved1 = curriculumService.save(curriculum_1);
        curriculumService.save(curriculum_2);
        List<Course> expected_courses = courseRepository.findAll();

        curriculumService.delete(saved1);

        List<Curriculum> actual = curriculumRepository.findAll();
        List<Course> actual_courses = courseRepository.findAll();

        assertEquals(expected, actual);
        assertEquals(expected_courses, actual_courses);
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

    @Test
    public void getAtDayWithTeacher() throws Exception {
        String test_subject_1 = "test_subject_1";
        LessonType lessonType = LessonType.CONSULTATION;
        LessonOrder lessonOrder1 = LessonOrder.FIFTH;
        LessonOrder lessonOrder2 = LessonOrder.FOURTH;
        String test_room_1 = "test_room_1";
        String test_group_1 = "test_group_1";

        String test_teacher_1 = "test_teacher_1";
        String test_teacher_2 = "test_teacher_2";

        Course course_1 = new Course(new Subject(test_subject_1), lessonType, Sets.newHashSet(
                new Teacher(test_teacher_1)
        ));
        Course saved_course = courseService.save(course_1);

        Course course_2 = new Course(new Subject(test_subject_1), lessonType, Sets.newHashSet(
                new Teacher(test_teacher_2)
        ));
        Course saved_course_2 = courseService.save(course_2);

        Curriculum curriculum_1 = new Curriculum(saved_course, new Group(test_group_1), Sets.newHashSet(
                new Circumstance(lessonOrder1, new Room(test_room_1), Sets.newHashSet(
                        LocalDate.now(),
                        LocalDate.now().plusDays(1),
                        LocalDate.now().plusDays(2),
                        LocalDate.now().plusDays(3)
                ))
        ));
        Curriculum curriculum_2 = new Curriculum(saved_course_2, new Group(test_group_1), Sets.newHashSet(
                new Circumstance(lessonOrder2, new Room(test_room_1), Sets.newHashSet(
                        LocalDate.now(),
                        LocalDate.now().plusDays(1),
                        LocalDate.now().plusDays(2),
                        LocalDate.now().plusDays(3)
                ))
        ));
        curriculumService.save(curriculum_1);
        curriculumService.save(curriculum_2);

        List<Curriculum> expected = Lists.newArrayList(curriculum_1);

        List<Curriculum> actual = curriculumService.getAtDayByTeacher(
                LocalDate.now(),
                (Teacher) teacherRepository.findOne(TeacherSpecification.getByName(test_teacher_1))
        );

        assertEquals(expected, actual);
    }

    @Test
    public void getAtDayWithGroup() throws Exception {
        String test_subject_1 = "test_subject_1";
        LessonType lessonType = LessonType.CONSULTATION;
        LessonOrder lessonOrder1 = LessonOrder.FIFTH;
        LessonOrder lessonOrder2 = LessonOrder.FOURTH;
        String test_room_1 = "test_room_1";
        String test_group_1 = "test_group_1";
        String test_group_2 = "test_group_2";

        String test_teacher_1 = "test_teacher_1";

        Course course_1 = new Course(new Subject(test_subject_1), lessonType, Sets.newHashSet(
                new Teacher(test_teacher_1)
        ));
        Course saved_course = courseService.save(course_1);


        Curriculum curriculum_1 = new Curriculum(saved_course, new Group(test_group_1), Sets.newHashSet(
                new Circumstance(lessonOrder1, new Room(test_room_1), Sets.newHashSet(
                        LocalDate.now(),
                        LocalDate.now().plusDays(1),
                        LocalDate.now().plusDays(2),
                        LocalDate.now().plusDays(3)
                ))
        ));
        Curriculum curriculum_2 = new Curriculum(saved_course, new Group(test_group_2), Sets.newHashSet(
                new Circumstance(lessonOrder2, new Room(test_room_1), Sets.newHashSet(
                        LocalDate.now(),
                        LocalDate.now().plusDays(1),
                        LocalDate.now().plusDays(2),
                        LocalDate.now().plusDays(3)
                ))
        ));
        curriculumService.save(curriculum_1);
        curriculumService.save(curriculum_2);

        List<Curriculum> expected = Lists.newArrayList(curriculum_1);

        List<Curriculum> actual = curriculumService.getAtDayByGroup(
                LocalDate.now(),
                groupService.findByName(test_group_1)
        );

        assertEquals(expected, actual);
    }

    @Test
    public void getAtDayWithRoom() throws Exception {
        String test_subject_1 = "test_subject_1";
        LessonType lessonType = LessonType.CONSULTATION;
        LessonOrder lessonOrder1 = LessonOrder.FIFTH;
        LessonOrder lessonOrder2 = LessonOrder.FOURTH;
        String test_room_1 = "test_room_1";
        String test_room_2 = "test_room_2";
        String test_group_1 = "test_group_1";
        String test_teacher_1 = "test_teacher_1";

        Course course_1 = new Course(new Subject(test_subject_1), lessonType, Sets.newHashSet(
                new Teacher(test_teacher_1)
        ));
        Course saved_course = courseService.save(course_1);


        Curriculum curriculum_1 = new Curriculum(saved_course, new Group(test_group_1), Sets.newHashSet(
                new Circumstance(lessonOrder1, new Room(test_room_1), Sets.newHashSet(
                        LocalDate.now(),
                        LocalDate.now().plusDays(1),
                        LocalDate.now().plusDays(2),
                        LocalDate.now().plusDays(3)
                ))
        ));
        Curriculum curriculum_2 = new Curriculum(saved_course, new Group(test_group_1), Sets.newHashSet(
                new Circumstance(lessonOrder2, new Room(test_room_2), Sets.newHashSet(
                        LocalDate.now(),
                        LocalDate.now().plusDays(1),
                        LocalDate.now().plusDays(2),
                        LocalDate.now().plusDays(3)
                ))
        ));
        curriculumService.save(curriculum_1);
        curriculumService.save(curriculum_2);

        List<Curriculum> expected = Lists.newArrayList(curriculum_1);

        List<Curriculum> actual = curriculumService.getAtDayByRoom(
                LocalDate.now(),
                (Room) roomRepository.findOne(RoomSpecification.getByName(test_room_1))
        );

        assertEquals(expected, actual);
    }



    @Test
    public void getAtDateWithCourseAndOrder() throws Exception {

    }
}