package com.theopus.upload.service;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import com.theopus.entity.schedule.Group;
import com.theopus.parser.ParserUtils;
import com.theopus.parser.facade.Parser;
import com.theopus.parser.obj.FileSheet;
import com.theopus.repository.service.CurriculumService;
import javassist.bytecode.analysis.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class UploadServiceImpl implements UploadService {

    private static final Logger LOG = LoggerFactory.getLogger(UploadServiceImpl.class);


    private CurriculumService service;

    @Autowired
    public UploadServiceImpl(CurriculumService service) {
        this.service = service;
    }

    @Override
    public Integer upload(String string) {
        String text = string;
        FileSheet<Group> parser = Parser.groupDefaultPatternsParser();
        parser.prepare(text);
        ExecutorService ex = Executors.newSingleThreadExecutor();
        ex.submit(() -> {
            int i = 1;
            LOG.info("{} sheets to be parsed+saved.", parser.getTotal());
            while (parser.next()) {
                service.saveAll(parser.parse());
                service.flush();
                LOG.info("{}/{} proceeded", i++, parser.getTotal());
            }
        });
        return parser.getTotal();

    }

    @Override
    public Integer upload(MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());

        Files.write(ByteStreams.toByteArray(multipartFile.getInputStream()), file);
        String text = ParserUtils.pdfFileToString(file);
        Integer upload = upload(text);
        file.delete();
        return upload;
    }
}
