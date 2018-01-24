package com.theopus.repository.service.impl;

import com.theopus.entity.schedule.Group;
import com.theopus.entity.schedule.Room;
import com.theopus.entity.schedule.Subject;
import com.theopus.entity.schedule.Teacher;
import com.theopus.repository.config.DataBaseServiceConfig;
import com.theopus.repository.jparepo.GroupRepository;
import com.theopus.repository.jparepo.RoomRepository;
import com.theopus.repository.jparepo.SubjectRepository;
import com.theopus.repository.jparepo.TeacherRepository;
import com.theopus.repository.service.GroupService;
import com.theopus.repository.service.RoomService;
import com.theopus.repository.service.SubjectService;
import com.theopus.repository.service.TeacherService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Oleksandr_Tkachov on 23.12.2017.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DataBaseServiceConfig.class})
@Transactional
public class CacheableSimpleServiceTest {

    @Autowired
    private GroupService groupService;
    @Autowired
    private TeacherService teacherService;

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private RoomService roomService;
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private SubjectService subjectService;
    @Autowired
    private SubjectRepository subjectRepository;

    @Before
    public void setUp() throws Exception {
        teacherRepository.deleteAll();
        teacherService.flush();
        groupRepository.deleteAll();
        groupService.flush();
        roomRepository.deleteAll();
        roomService.flush();
        subjectRepository.deleteAll();
        subjectService.flush();
    }

    @After
    public void tearDown() throws Exception {
        teacherRepository.deleteAll();
        teacherService.flush();
        groupRepository.deleteAll();
        groupService.flush();
        roomRepository.deleteAll();
        roomService.flush();
        subjectRepository.deleteAll();
        subjectService.flush();
    }


    @Test
    public void saveTeacher_Count() throws Exception {
        String name = "testname";
        String name2 = "testname2";
        teacherService.save(new Teacher(name));
        teacherService.save(new Teacher(name));
        teacherService.save(new Teacher(name));
        teacherService.save(new Teacher(name2));

        long expected = 2L;
        long actual = teacherService.count();

        Assert.assertEquals(expected,actual);
    }

    @Test
    public void saveRoom_Count() throws Exception {
        String name = "testname";
        String name2 = "testname2";
        roomService.save(new Room(name));
        roomService.save(new Room(name2));

        long expected = 2L;
        long actual = roomService.count();

        Assert.assertEquals(expected,actual);
    }

    @Test
    public void saveSubject_Count() throws Exception {
        String name = "testname";
        String name2 = "testname2";
        subjectService.save(new Subject(name));
        subjectService.save(new Subject(name2));

        long expected = 2L;
        long actual = subjectService.count();

        Assert.assertEquals(expected,actual);
    }

    @Test
    public void saveGroup_Count() throws Exception {
        String name = "testname";
        String name2 = "testname2";
        groupService.save(new Group(name));
        groupService.save(new Group(name2));

        long expected = 2L;
        long actual = groupService.count();

        Assert.assertEquals(expected,actual);
    }

    @Test
    public void saveSameRoom_getById() throws Exception {
        String name = "testname";
        Room expected = new Room(name);
        Room save = roomService.save(new Room(name));
        Room actual = roomService.get(save.getId());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void saveSameSubject_getById() throws Exception {
        String name = "testname";
        Subject expected = new Subject(name);
        Subject save = subjectService.save(new Subject(name));
        Subject actual = subjectService.get(save.getId());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void saveSameGroup_getById() throws Exception {
        String name = "testname";
        Group expected = new Group(name);
        Group save = groupService.save(new Group(name));
        Group actual = groupService.get(save.getId());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void saveSameTeacher_getById() throws Exception {
        String name = "testname";
        Teacher expected = teacherService.save(new Teacher(name));
        Teacher actual = teacherService.get(expected.getId());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void saveSameSubject() throws Exception {
        String name = "testname";
        List<Subject> expected = Collections.singletonList(new Subject(name));
        subjectService.save(new Subject(name));
        subjectService.save(new Subject(name));
        subjectService.save(new Subject(name));

        List<Subject> actual = (List<Subject>) subjectService.getAll();

        assertTrue(expected.equals(actual));
    }

    @Test
    public void saveSameRoom() throws Exception {
        String name = "testname";
        List<Room> expected = Collections.singletonList(new Room(name));
        roomService.save(new Room(name));
        roomService.save(new Room(name));
        roomService.save(new Room(name));

        List<Room> actual = (List<Room>) roomService.getAll();

        assertTrue(expected.equals(actual));
    }

    @Test
    public void saveSameGroup() throws Exception {
        String name = "testname";
        List<Group> expected = Collections.singletonList(new Group(name));
        groupService.save(new Group(name));
        groupService.save(new Group(name));
        groupService.save(new Group(name));

        List<Group> actual = (List<Group>) groupService.getAll();

        assertTrue(expected.equals(actual));
    }

    @Test
    public void saveSameTeacher() throws Exception {
        String name = "testname";
        List<Teacher> expected = Collections.singletonList(new Teacher(name));
        teacherService.save(new Teacher(name));
        teacherService.save(new Teacher(name));
        teacherService.save(new Teacher(name));

        List<Teacher> actual = (List<Teacher>) teacherService.getAll();

        Assert.assertEquals(expected,actual);
    }


    @Test
    public void saveSameTeacherAfterFlush() throws Exception {
        String name = "testname";
        List<Teacher> expected = Collections.singletonList(new Teacher(name));
        teacherService.save(new Teacher(name));
        teacherService.flush();
        teacherService.save(new Teacher(name));
        teacherService.flush();
        teacherService.save(new Teacher(name));

        List<Teacher> actual = (List<Teacher>) teacherService.getAll();

        Assert.assertEquals(expected,actual);
    }

    @Test
    public void saveSameGroupAfterFlush() throws Exception {
        String name = "testname";
        List<Group> expected = Collections.singletonList(new Group(name));
        groupService.save(new Group(name));
        groupService.flush();
        groupService.save(new Group(name));
        groupService.flush();
        groupService.save(new Group(name));

        List<Group> actual = (List<Group>) groupService.getAll();

        assertEquals(expected,actual);
    }
}