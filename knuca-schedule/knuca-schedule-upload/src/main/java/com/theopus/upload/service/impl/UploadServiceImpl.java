package com.theopus.upload.service.impl;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import com.theopus.entity.schedule.Group;
import com.theopus.parser.ParserUtils;
import com.theopus.parser.facade.Parser;
import com.theopus.parser.obj.FileSheet;
import com.theopus.repository.service.CircumstanceService;
import com.theopus.repository.service.CurriculumService;
import com.theopus.repository.service.GroupService;
import com.theopus.upload.service.UploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class UploadServiceImpl implements UploadService {

    private static final Logger LOG = LoggerFactory.getLogger(UploadServiceImpl.class);


    private CurriculumService service;
    private CircumstanceService circumstanceService;
    private GroupService groupService;
    private ExecutorService ex;

    @Autowired
    public UploadServiceImpl(CurriculumService service, CircumstanceService circumstanceService, GroupService groupService) {
        this.service = service;
        this.circumstanceService = circumstanceService;
        this.groupService = groupService;
        this.ex = Executors.newSingleThreadExecutor();
    }

    @Override
    public Integer upload(MultipartFile multipartFile) throws IOException {
        FileSheet<Group> parser = Parser.groupDefaultPatternsParser();
        parser.prepare(fileToString(multipartFile));
        startProcessing(parser);
        return parser.getTotal();
    }

    @Override
    public Integer upload(MultipartFile multipartFile, Integer year) throws IOException {
        FileSheet<Group> parser = Parser.groupDefaultPatternsParser();
        parser.prepare(fileToString(multipartFile), year);
        startProcessing(parser);
        return parser.getTotal();
    }

    @PreDestroy
    public void destroy() {
        ex.shutdownNow();
    }

    private String fileToString(MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        Files.write(ByteStreams.toByteArray(multipartFile.getInputStream()), file);
        String text = ParserUtils.pdfFileToString(file);
        file.delete();
        return text;
    }

    private void startProcessing(FileSheet<Group> parser) {
        LocalDate nowMock = LocalDate.now();
        ex.submit(() -> {
            try {
                int i = 1;
                LOG.info("{} sheets to be parsed+saved.", parser.getTotal());
                while (parser.next()) {
                    Group anchor = groupService.findByName(parser.getCurrent().getAnchor().getName());
                    if (!Objects.isNull(anchor)) {
                        LOG.info("Deleting from {} with anchor {}", nowMock, anchor);
                        circumstanceService.deleteWithGroupAfter(anchor, nowMock);
                    } else {
                        LOG.info("Not Found with anchor {}", parser.getCurrent().getAnchor().getName());
                    }
                    service.saveAll(parser.parse());
                    service.flush();
                    LOG.info("{}/{} proceeded", i++, parser.getTotal());
                }
            } catch (Exception e) {
                LOG.error("Exception in upload executor service", e);
            }
        });
    }
}
