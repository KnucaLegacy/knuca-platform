package conf;

import com.theopus.repository.jparepo.GroupRepository;
import com.theopus.repository.jparepo.RoomRepository;
import com.theopus.repository.jparepo.SubjectRepository;
import com.theopus.repository.jparepo.TeacherRepository;
import com.theopus.repository.service.GroupService;
import com.theopus.repository.service.RoomService;
import com.theopus.repository.service.SubjectService;
import com.theopus.repository.service.TeacherService;
import com.theopus.repository.service.impl.CacheableGroupService;
import com.theopus.repository.service.impl.CacheableRoomService;
import com.theopus.repository.service.impl.CacheableSubjectService;
import com.theopus.repository.service.impl.CacheableTeacherService;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableAutoConfiguration
@EnableJpaRepositories("com.theopus.repository.jparepo")
@EntityScan("com.theopus.entity.schedule")
public class DataBaseServiceConfigTest {

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
