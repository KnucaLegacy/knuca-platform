package com.theopus.repository.service.impl;

import com.theopus.entity.schedule.Teacher;
import com.theopus.repository.jparepo.TeacherRepository;
import com.theopus.repository.service.TeacherService;
import conf.DataBaseServiceConfigTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DataBaseServiceConfigTest.class)
public class CacheableTeacherServiceTest {


    @Autowired
    private TeacherRepository repository;


    @Test
    public void saveSameTeacher() throws Exception {
        TeacherService teacherService = new CacheableTeacherService(repository);
        teacherService.save(new Teacher("228"));
    }
}