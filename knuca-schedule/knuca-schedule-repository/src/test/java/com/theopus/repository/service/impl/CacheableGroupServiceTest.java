package com.theopus.repository.service.impl;

import com.theopus.entity.schedule.Group;
import com.theopus.entity.schedule.Teacher;
import com.theopus.repository.jparepo.GroupRepository;
import com.theopus.repository.service.GroupService;
import conf.DataBaseServiceConfigTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by Oleksandr_Tkachov on 23.12.2017.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DataBaseServiceConfigTest.class})
public class CacheableGroupServiceTest {

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupRepository groupRepository;

    @Before
    public void setUp() throws Exception {
        groupRepository.deleteAll();
        groupService.flush();
    }

    @After
    public void tearDown() throws Exception {
        groupRepository.deleteAll();
        groupService.flush();
    }

    @Test
    public void saveSameGroup() throws Exception {
        String name = "testname";
        Long expected = 1L;
        groupService.save(new Group(name));
        groupService.save(new Group(name));
        groupService.save(new Group(name));

        Long actual = groupService.size();

        assertEquals(expected,actual);

    }
}