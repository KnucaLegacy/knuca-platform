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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = PersistenceConfig.class)
public class RunnerTest {

    @Autowired
    private CurriculumService service;

    @Test
    public void name() throws Exception {
        String text = ParserUtils.readPdfsFromFolder("src/test/resources/pdfs/full");
        FileSheet<Group> parser = Parser.groupDefaultPatternsParser();
        parser.prepare(text.toString());

        int i = 1;
        while (parser.next()){
            service.saveAll(parser.parse());
            System.out.println(" " + i++ + "of " + parser.getTotal() + " sheet parsed.");
        }
    }
}
