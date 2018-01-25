package com.theopus.repository.service.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.theopus.entity.schedule.*;
import com.theopus.entity.schedule.enums.LessonOrder;
import com.theopus.entity.schedule.enums.LessonType;
import com.theopus.repository.config.DataBaseServiceConfig;
import com.theopus.repository.jparepo.*;
import com.theopus.repository.service.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DataBaseServiceConfig.class})
public class LessonServiceImplTest {

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
    @Autowired
    private LessonService lessonService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private RoomService roomService;

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
    public void getByRoomAtDate() throws Exception {
        String test_subject_1 = "test_subject_1";
        LessonType lessonType = LessonType.CONSULTATION;
        LessonOrder lessonOrder1 = LessonOrder.FIFTH;
        LessonOrder lessonOrder1_5 = LessonOrder.SECOND;
        LessonOrder lessonOrder2 = LessonOrder.FORTH;
        String test_room_1 = "test_room_1";
        String test_room_2 = "test_room_2";
        String test_group_1 = "test_group_1";
        String test_group_2 = "test_group_2";

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

        Curriculum curriculum_1_5 = new Curriculum(saved_course_2, new Group(test_group_2), Sets.newHashSet(
                new Circumstance(lessonOrder1_5, new Room(test_room_1), Sets.newHashSet(
                        LocalDate.now(),
                        LocalDate.now().plusDays(1),
                        LocalDate.now().plusDays(2),
                        LocalDate.now().plusDays(3)
                ))
        ));
        Curriculum curriculum_2 = new Curriculum(saved_course_2, new Group(test_group_1), Sets.newHashSet(
                new Circumstance(lessonOrder2, new Room(test_room_2), Sets.newHashSet(
                        LocalDate.now().plusDays(1),
                        LocalDate.now().plusDays(2),
                        LocalDate.now().plusDays(3)
                ))
        ));
        curriculumService.save(curriculum_1);
        curriculumService.save(curriculum_2);
        curriculumService.save(curriculum_1_5);

        List<Lesson> expected = Lists.newArrayList(
                new Lesson(
                        lessonOrder1_5,
                        Sets.newHashSet(new Room(test_room_1)),
                        Sets.newHashSet(
                                new Group(test_group_2)
                        ),
                        LocalDate.now(),
                        course_2
                ),
                new Lesson(
                        lessonOrder1,
                        Sets.newHashSet(new Room(test_room_1)),
                        Sets.newHashSet(
                                new Group(test_group_1)
                        ),
                        LocalDate.now(),
                        course_1
                )
        );

        List<Lesson> actual = lessonService.getByRoom(LocalDate.now(), roomService.findByName(test_room_1));

        assertEquals(expected, actual);
    }

    @Test
    public void getByTeacherAtDate() throws Exception {
        String test_subject_1 = "test_subject_1";
        LessonType lessonType = LessonType.CONSULTATION;
        LessonOrder lessonOrder1 = LessonOrder.FIFTH;
        LessonOrder lessonOrder2 = LessonOrder.FORTH;
        String test_room_1 = "test_room_1";
        String test_group_1 = "test_group_1";
        String test_group_2 = "test_group_2";

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

        Curriculum curriculum_1_5 = new Curriculum(saved_course, new Group(test_group_2), Sets.newHashSet(
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
        curriculumService.save(curriculum_1_5);

        List<Lesson> expected = Lists.newArrayList(
                new Lesson(
                        lessonOrder1,
                        Sets.newHashSet(new Room(test_room_1)),
                        Sets.newHashSet(
                                new Group(test_group_1),
                                new Group(test_group_2)
                        ),
                        LocalDate.now(),
                        course_1
                )
        );

        List<Lesson> actual = lessonService.getByTeacher(LocalDate.now(), teacherService.findByName(test_teacher_1));

        assertEquals(expected, actual);
    }

    @Test
    public void getByGroupAtDate() throws Exception {
        String test_subject_1 = "test_subject_1";
        LessonType lessonType = LessonType.CONSULTATION;
        LessonOrder lessonOrder1 = LessonOrder.FIFTH;
        LessonOrder lessonOrder2 = LessonOrder.FORTH;
        LessonOrder lessonOrder1_5 = LessonOrder.FIRST;
        String test_room_1 = "test_room_1";
        String test_group_1 = "test_group_1";
        String test_group_2 = "test_group_2";

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

        Curriculum curriculum_1_5 = new Curriculum(saved_course, new Group(test_group_1), Sets.newHashSet(
                new Circumstance(lessonOrder1_5, new Room(test_room_1), Sets.newHashSet(
                        LocalDate.now(),
                        LocalDate.now().plusDays(1),
                        LocalDate.now().plusDays(2),
                        LocalDate.now().plusDays(3)
                ))
        ));
        Curriculum curriculum_2 = new Curriculum(saved_course_2, new Group(test_group_2), Sets.newHashSet(
                new Circumstance(lessonOrder2, new Room(test_room_1), Sets.newHashSet(
                        LocalDate.now(),
                        LocalDate.now().plusDays(1),
                        LocalDate.now().plusDays(2),
                        LocalDate.now().plusDays(3)
                ))
        ));
        curriculumService.save(curriculum_1);
        curriculumService.save(curriculum_2);
        curriculumService.save(curriculum_1_5);

        List<Lesson> expected = Lists.newArrayList(
                new Lesson(
                        lessonOrder1_5,
                        Sets.newHashSet(new Room(test_room_1)),
                        Sets.newHashSet(new Group(test_group_1)),
                        LocalDate.now(),
                        course_1
                ),
                new Lesson(
                        lessonOrder1,
                        Sets.newHashSet(new Room(test_room_1)),
                        Sets.newHashSet(new Group(test_group_1)),
                        LocalDate.now(),
                        course_1
                )
        );

        List<Lesson> actual = lessonService.getByGroup(LocalDate.now(), groupService.findByName(test_group_1));

        assertEquals(expected, actual);
    }

    @Test
    public void getByRoomAtDates() throws Exception {
        String test_subject_1 = "test_subject_1";
        LessonType lessonType = LessonType.CONSULTATION;
        LessonOrder lessonOrder1 = LessonOrder.FIFTH;
        LessonOrder lessonOrder1_5 = LessonOrder.SECOND;
        LessonOrder lessonOrder2 = LessonOrder.FORTH;
        String test_room_1 = "test_room_1";
        String test_room_2 = "test_room_2";
        String test_group_1 = "test_group_1";
        String test_group_2 = "test_group_2";

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
                        LocalDate.now().plusDays(2),
                        LocalDate.now().plusDays(3)
                ))
        ));

        Curriculum curriculum_1_5 = new Curriculum(saved_course_2, new Group(test_group_2), Sets.newHashSet(
                new Circumstance(lessonOrder1_5, new Room(test_room_1), Sets.newHashSet(
                        LocalDate.now().plusDays(1),
                        LocalDate.now().plusDays(2),
                        LocalDate.now().plusDays(3)
                ))
        ));

        Curriculum curriculum_2 = new Curriculum(saved_course_2, new Group(test_group_1), Sets.newHashSet(
                new Circumstance(lessonOrder2, new Room(test_room_2), Sets.newHashSet(
                        LocalDate.now(),
                        LocalDate.now().plusDays(1),
                        LocalDate.now().plusDays(2),
                        LocalDate.now().plusDays(3)
                ))
        ));

        curriculumService.save(curriculum_1);
        curriculumService.save(curriculum_2);
        curriculumService.save(curriculum_1_5);

        Map<LocalDate, List<Lesson>> expected = new ImmutableMap.Builder<LocalDate, List<Lesson>>()
                .put(
                        LocalDate.now(),
                        Lists.newArrayList(
                                new Lesson(
                                        lessonOrder1,
                                        Sets.newHashSet(new Room(test_room_1)),
                                        Sets.newHashSet(new Group(test_group_1)),
                                        LocalDate.now(),
                                        course_1
                                )
                        )
                )
                .put(
                        LocalDate.now().plusDays(1),
                        Lists.newArrayList(
                                new Lesson(
                                        lessonOrder1_5,
                                        Sets.newHashSet(new Room(test_room_1)),
                                        Sets.newHashSet(new Group(test_group_2)),
                                        LocalDate.now().plusDays(1),
                                        course_2
                                )
                        )
                )
                .build();

        Map<LocalDate, List<Lesson>> actual = lessonService.getByRoom(
                Sets.newHashSet(LocalDate.now(), LocalDate.now().plusDays(1)),
                roomService.findByName(test_room_1)
        );

        assertEquals(expected, actual);
    }

    @Test
    public void getByTeacherAtDates() throws Exception {
        String test_subject_1 = "test_subject_1";
        LessonType lessonType = LessonType.CONSULTATION;
        LessonOrder lessonOrder1 = LessonOrder.FIFTH;
        LessonOrder lessonOrder1_5 = LessonOrder.SECOND;
        LessonOrder lessonOrder2 = LessonOrder.FORTH;
        String test_room_1 = "test_room_1";
        String test_group_1 = "test_group_1";
        String test_group_2 = "test_group_2";

        String test_teacher_1 = "test_teacher_1";
        String test_teacher_2 = "test_teacher_2";

        Course course_1 = new Course(new Subject(test_subject_1), lessonType, Sets.newHashSet(
                new Teacher(test_teacher_1)
        ));
        Course saved_course = courseService.save(course_1);

        Course course_2 = new Course(new Subject(test_subject_1), lessonType, Sets.newHashSet(
                new Teacher(test_teacher_1),
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

        Curriculum curriculum_1_5 = new Curriculum(saved_course_2, new Group(test_group_1), Sets.newHashSet(
                new Circumstance(lessonOrder1, new Room(test_room_1), Sets.newHashSet(
                        LocalDate.now().plusDays(1),
                        LocalDate.now().plusDays(2),
                        LocalDate.now().plusDays(3)
                ))
        ));

        Curriculum curriculum_2 = new Curriculum(saved_course_2, new Group(test_group_2), Sets.newHashSet(
                new Circumstance(lessonOrder2, new Room(test_room_1), Sets.newHashSet(
                        LocalDate.now(),
                        LocalDate.now().plusDays(1),
                        LocalDate.now().plusDays(2),
                        LocalDate.now().plusDays(3)
                ))
        ));
        curriculumService.save(curriculum_1);
        curriculumService.save(curriculum_2);
        curriculumService.save(curriculum_1_5);

        Map<LocalDate, List<Lesson>> expected = new ImmutableMap.Builder<LocalDate, List<Lesson>>()
                .put(
                        LocalDate.now(),
                        Lists.newArrayList(
                                new Lesson(
                                        lessonOrder2,
                                        Sets.newHashSet(new Room(test_room_1)),
                                        Sets.newHashSet(new Group(test_group_2)),
                                        LocalDate.now(),
                                        course_2
                                ),
                                new Lesson(
                                        lessonOrder1,
                                        Sets.newHashSet(new Room(test_room_1)),
                                        Sets.newHashSet(new Group(test_group_1)),
                                        LocalDate.now(),
                                        course_1
                                )
                        )
                )
                .put(
                        LocalDate.now().plusDays(1),
                        Lists.newArrayList(
                                new Lesson(
                                        lessonOrder2,
                                        Sets.newHashSet(new Room(test_room_1)),
                                        Sets.newHashSet(
                                                new Group(test_group_2)
                                        ),
                                        LocalDate.now().plusDays(1),
                                        course_1
                                ),
                                new Lesson(
                                        lessonOrder1,
                                        Sets.newHashSet(new Room(test_room_1)),
                                        Sets.newHashSet(new Group(test_group_1)),
                                        LocalDate.now().plusDays(1),
                                        course_1
                                )
                        )
                )
                .build();

        Map<LocalDate, List<Lesson>> actual = lessonService.getByTeacher(
                Sets.newHashSet(LocalDate.now(), LocalDate.now().plusDays(1)),
                teacherService.findByName(test_teacher_1)
        );

        assertEquals(expected, actual);
    }

    @Test
    public void getByGroupAtDates() throws Exception {
        String test_subject_1 = "test_subject_1";
        LessonType lessonType = LessonType.CONSULTATION;
        LessonOrder lessonOrder1 = LessonOrder.FIFTH;
        LessonOrder lessonOrder2 = LessonOrder.FORTH;
        LessonOrder lessonOrder1_5 = LessonOrder.FIRST;
        String test_room_1 = "test_room_1";
        String test_group_1 = "test_group_1";
        String test_group_2 = "test_group_2";

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

        Curriculum curriculum_1_5 = new Curriculum(saved_course, new Group(test_group_1), Sets.newHashSet(
                new Circumstance(lessonOrder1_5, new Room(test_room_1), Sets.newHashSet(
                        LocalDate.now(),
                        LocalDate.now().plusDays(2),
                        LocalDate.now().plusDays(3)
                ))
        ));
        Curriculum curriculum_2 = new Curriculum(saved_course_2, new Group(test_group_2), Sets.newHashSet(
                new Circumstance(lessonOrder2, new Room(test_room_1), Sets.newHashSet(
                        LocalDate.now(),
                        LocalDate.now().plusDays(1),
                        LocalDate.now().plusDays(2),
                        LocalDate.now().plusDays(3)
                ))
        ));
        curriculumService.save(curriculum_1);
        curriculumService.save(curriculum_2);
        curriculumService.save(curriculum_1_5);

        Map<LocalDate, List<Lesson>> expected = new ImmutableMap.Builder<LocalDate, List<Lesson>>()
                .put(
                        LocalDate.now(),
                        Lists.newArrayList(
                                new Lesson(
                                        lessonOrder1_5,
                                        Sets.newHashSet(new Room(test_room_1)),
                                        Sets.newHashSet(new Group(test_group_1)),
                                        LocalDate.now(),
                                        course_1
                                ),
                                new Lesson(
                                        lessonOrder1,
                                        Sets.newHashSet(new Room(test_room_1)),
                                        Sets.newHashSet(new Group(test_group_1)),
                                        LocalDate.now(),
                                        course_1
                                )
                        )
                )
                .put(
                        LocalDate.now().plusDays(1),
                        Lists.newArrayList(
                                new Lesson(
                                        lessonOrder1,
                                        Sets.newHashSet(new Room(test_room_1)),
                                        Sets.newHashSet(new Group(test_group_1)),
                                        LocalDate.now().plusDays(1),
                                        course_1
                                )
                        )
                )
                .build();

        Map<LocalDate, List<Lesson>> actual = lessonService.getByGroup(
                Sets.newHashSet(LocalDate.now(), LocalDate.now().plusDays(1)),
                groupService.findByName(test_group_1)
        );

        assertEquals(expected, actual);
    }
}