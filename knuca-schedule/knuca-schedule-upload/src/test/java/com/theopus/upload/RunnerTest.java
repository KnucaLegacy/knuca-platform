package com.theopus.upload;

import com.theopus.entity.schedule.Curriculum;
import com.theopus.entity.schedule.Group;
import com.theopus.parser.ParserUtils;
import com.theopus.parser.facade.Parser;
import com.theopus.parser.obj.FileSheet;
import com.theopus.repository.config.DataBaseServiceConfig;
import com.theopus.repository.service.CurriculumService;
import com.theopus.upload.config.PersistenceConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = PersistenceConfig.class)
public class RunnerTest {


}
