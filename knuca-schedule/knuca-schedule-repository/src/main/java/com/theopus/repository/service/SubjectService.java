package com.theopus.repository.service;

import com.theopus.entity.schedule.Group;
import com.theopus.entity.schedule.Subject;
import com.theopus.repository.jparepo.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectService {


    @Autowired
    private SubjectRepository subjectRepository;

    public void save(Subject subject){
    }
}
