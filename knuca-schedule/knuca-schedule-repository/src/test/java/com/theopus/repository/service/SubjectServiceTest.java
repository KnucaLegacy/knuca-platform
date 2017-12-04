package com.theopus.repository.service;

import com.theopus.entity.schedule.Subject;
import com.theopus.repository.JpaConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = JpaConfig.class)
public class SubjectServiceTest {


    @Autowired
    private SimpleService<Subject> subjectService;

    @Test
    public void lol() throws Exception {
        Subject subject = new Subject();
        subject.setName("lol");
        subjectService.save(subject);
        Subject subject1 = new Subject();
        subject1.setName("lol");
        subjectService.save(subject1);
        Subject subject2 = new Subject();
        subject2.setName("lol");
        subjectService.save(subject2);

        System.out.println(subjectService.getAll());

    }
}