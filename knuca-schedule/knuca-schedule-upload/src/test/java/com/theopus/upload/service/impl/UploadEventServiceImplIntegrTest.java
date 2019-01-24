package com.theopus.upload.service.impl;

import com.theopus.upload.config.PersistenceConfig;
import com.theopus.upload.entity.UploadEvent;
import com.theopus.upload.entity.UploadFile;
import com.theopus.upload.repository.UploadEventRepo;
import com.theopus.upload.repository.UploadFileRepo;
import com.theopus.upload.service.UploadEventService;
import org.assertj.core.util.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = PersistenceConfig.class)
public class UploadEventServiceImplIntegrTest {

    @Autowired
    private UploadEventService ues;
    @Autowired
    private UploadEventRepo repo;
    @Autowired
    private UploadFileRepo uploadFileRepo;

    @Before
    public void setUp() throws Exception {
        repo.deleteAll();
        uploadFileRepo.deleteAll();
    }

    @After
    public void tearDown() throws Exception {
        repo.deleteAll();
        uploadFileRepo.deleteAll();
    }

    @Test
    public void count() throws Exception {
        UploadEvent expected1 = new UploadEvent();
        expected1.setStatus(UploadEvent.Status.CREATED);
        expected1.setLastProcessing(LocalDateTime.now());

        UploadEvent expected2 = new UploadEvent();
        expected2.setStatus(UploadEvent.Status.FAILED);
        expected2.setLastProcessing(LocalDateTime.now());

        long expected = 2L;
        ues.save(expected1);
        ues.save(expected2);
        long actual = ues.count();

        assertEquals(expected, actual);
    }

    @Test
    public void save() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        UploadEvent expected = new UploadEvent();
        expected.setStatus(UploadEvent.Status.CREATED);
        expected.setLastProcessing(now);

        ues.save(expected);
        UploadEvent actual = ues.getAll().get(0);

        assertEquals(expected, actual);
    }

    @Test
    public void saveWithFiles() throws Exception {
        UploadFile expected = new UploadFile();
        LocalDateTime now = LocalDateTime.now();
        UploadEvent toSave = new UploadEvent();
        toSave.setStatus(UploadEvent.Status.CREATED);
        toSave.setLastProcessing(now);
        toSave.setFiles(Lists.newArrayList(expected));


        ues.save(toSave);
        UploadFile actual = uploadFileRepo.findAll().get(0);

        assertEquals(expected, actual);
    }

    @Test
    public void get() throws Exception {
        UploadEvent expected = new UploadEvent();
        expected.setStatus(UploadEvent.Status.CREATED);
        expected.setLastProcessing(LocalDateTime.now());

        UploadEvent save = ues.save(expected);
        UploadEvent actual = ues.get(save.getId());

        assertEquals(expected, actual);
    }

    @Test(expected = EntityNotFoundException.class)
    public void getError() throws Exception {
        ues.get(227L);
    }

    @Test
    public void getAll() throws Exception {
        UploadEvent expected1 = new UploadEvent();
        expected1.setStatus(UploadEvent.Status.CREATED);
        expected1.setLastProcessing(LocalDateTime.now());

        UploadEvent expected2 = new UploadEvent();
        expected2.setStatus(UploadEvent.Status.FAILED);
        expected2.setLastProcessing(LocalDateTime.now());

        List<UploadEvent> expected = Lists.newArrayList(expected1, expected2);
        ues.save(expected1);
        ues.save(expected2);
        List<UploadEvent> actual = ues.getAll();

        assertEquals(expected, actual);
    }

    @Test
    public void delete() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        UploadEvent entity = new UploadEvent();
        entity.setStatus(UploadEvent.Status.CREATED);
        entity.setLastProcessing(now);

        UploadEvent save = ues.save(entity);
        ues.delete(save);

        List<Object> expected = Collections.emptyList();
        List<UploadEvent> actual = ues.getAll();

        assertEquals(expected, actual);
    }
    @Test
    public void deleteWithFileStay() throws Exception {
        UploadFile expected = new UploadFile();
        LocalDateTime now = LocalDateTime.now();
        UploadEvent toSave = new UploadEvent();
        toSave.setStatus(UploadEvent.Status.CREATED);
        toSave.setLastProcessing(now);
        toSave.setFiles(Lists.newArrayList(expected));


        ues.save(toSave);
        ues.delete(toSave);
        UploadFile actual = uploadFileRepo.findAll().get(0);

        assertEquals(expected, actual);
        assertEquals(0L, repo.count());
    }




}