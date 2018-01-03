package com.theopus.repository.service.impl;

import com.theopus.entity.schedule.Group;
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
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Oleksandr_Tkachov on 23.12.2017.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DataBaseServiceConfig.class})
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
    }

    @After
    public void tearDown() throws Exception {
        teacherRepository.deleteAll();
        teacherService.flush();
        groupRepository.deleteAll();
        groupService.flush();
    }

    @Test
    public void saveSameGroup() throws Exception {
        String name = "testname";
        List<Group> expected = Collections.singletonList(new Group(name));
        groupService.save(new Group(name));
        groupService.save(new Group(name));
        groupService.save(new Group(name));

        List<Group> actual = (List<Group>) groupService.getAll();

        assertEquals(expected,actual);
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
}