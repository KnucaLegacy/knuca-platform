package com.theopus.repository.service.impl;

import com.theopus.entity.schedule.Teacher;
import com.theopus.repository.jparepo.TeacherRepository;
import com.theopus.repository.service.TeacherService;
import conf.DataBaseServiceConfigTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DataBaseServiceConfigTest.class)
public class CacheableTeacherServiceTest {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TeacherRepository teacherRepository;

    @After
    public void tearDown() throws Exception {
        teacherRepository.deleteAll();
        teacherService.flush();
    }

    @Before
    public void name() throws Exception {
        teacherRepository.deleteAll();
        teacherService.flush();
    }

    @Test
    public void saveSameTeacher() throws Exception {
        String name = "testname";
        Long expected = 1L;
        teacherService.save(new Teacher(name));
        teacherService.save(new Teacher(name));
        teacherService.save(new Teacher(name));

        Long actual = teacherService.size();

        Assert.assertEquals(expected,actual);
    }
}