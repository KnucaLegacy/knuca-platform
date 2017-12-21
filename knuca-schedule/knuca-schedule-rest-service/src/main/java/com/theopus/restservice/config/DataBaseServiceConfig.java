package com.theopus.restservice.config;

import com.theopus.entity.schedule.Group;
import com.theopus.entity.schedule.Room;
import com.theopus.entity.schedule.Subject;
import com.theopus.entity.schedule.Teacher;
import com.theopus.repository.jparepo.GroupRepository;
import com.theopus.repository.jparepo.RoomRepository;
import com.theopus.repository.jparepo.SubjectRepository;
import com.theopus.repository.jparepo.TeacherRepository;
import com.theopus.repository.service.*;
import com.theopus.repository.service.impl.CacheableGroupService;
import com.theopus.repository.service.impl.CacheableRoomService;
import com.theopus.repository.service.impl.CacheableSubjectService;
import com.theopus.repository.service.impl.CacheableTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.theopus.repository.jparepo")
@EntityScan("com.theopus.entity.schedule")
public class DataBaseServiceConfig {

    @Bean("roomService")
    public RoomService roomService(RoomRepository roomRepository){
        return new CacheableRoomService(roomRepository);
    }

    @Bean("groupService")
    public GroupService groupService(GroupRepository groupRepository){
        return new CacheableGroupService(groupRepository);
    }

    @Bean("teacherService")
    public TeacherService teacherService(TeacherRepository teacherRepository){
        return new CacheableTeacherService(teacherRepository);
    }

    @Bean("subjectService")
    public SubjectService subjectService(SubjectRepository subjectRepository){
        return new CacheableSubjectService(subjectRepository);
    }



}
