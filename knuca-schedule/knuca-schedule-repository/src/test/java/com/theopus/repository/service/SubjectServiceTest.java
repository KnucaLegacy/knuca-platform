package com.theopus.repository.service;

import com.theopus.repository.JpaConfig;
import com.theopus.repository.jparepo.SubjectRepository;
import com.theopus.repository.specification.SubjectSpecification;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = JpaConfig.class)
public class SubjectServiceTest {


    @Autowired
    private SubjectRepository subjectRepository;

    @Test
    public void name() throws Exception {
        subjectRepository.findAll(SubjectSpecification.customerHasBirthday());
    }
}