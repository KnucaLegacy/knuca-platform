package com.theopus.upload.config;

import com.google.common.collect.ImmutableMap;
import com.theopus.entity.schedule.Group;
import com.theopus.parser.facade.Parser;
import com.theopus.parser.obj.AnchorType;
import com.theopus.parser.obj.FileSheet;
import com.theopus.parser.obj.ident.AnchorIdentifier;
import com.theopus.repository.service.CircumstanceService;
import com.theopus.repository.service.CurriculumService;
import com.theopus.repository.service.GroupService;
import com.theopus.upload.constants.FileExtension;
import com.theopus.upload.controller.UploadController;
import com.theopus.upload.file.EventCreator;
import com.theopus.upload.file.ExtensionQualifier;
import com.theopus.upload.file.FileConverter;
import com.theopus.upload.file.FileParser;
import com.theopus.upload.handling.*;
import com.theopus.upload.service.UploadEventService;
import com.theopus.upload.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class UpdaterConfig {

    @Bean
    @Autowired
    public UploadController uploadController(EventLauncher eventLauncher, EventCreator eventCreator, UploadFileService fileService,
                                             UploadEventService eventService, ReportService reportService) {
        return new UploadController(eventService, fileService, eventCreator, eventLauncher, reportService);
    }

    @Bean
    @Autowired
    public EventLauncher eventLauncher(EventHandler handler, UploadEventService eventService) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        return new EventLauncher(executorService, handler, eventService);
    }

    @Bean
    @Autowired
    public EventHandler eventHandler(CurriculumService curriculumService, ParserDistributor distributor, ReportService reportService) {
        return new EventHandler(curriculumService, distributor, reportService);
    }

    @Bean
    @Autowired
    public ParserDistributor parserDistributor(GroupService groupService, CircumstanceService service) {
        AnchorIdentifier anchorIdentifier = Parser.defaultAnchorIdentifier();
        FileSheet<Group> groupFileSheet = Parser.groupDefaultPatternsParser();
        FindAndRemoveGroupStrategy groupStrategy = new FindAndRemoveGroupStrategy(groupService, service, groupFileSheet);

        return new ParserDistributor(
                ImmutableMap.of(AnchorType.GROUP, groupFileSheet),
                ImmutableMap.of(AnchorType.GROUP, groupStrategy),
                anchorIdentifier
        );
    }

    @Bean
    public EventCreator eventCreator(FileConverter fileConverter) {
        return new EventCreator(fileConverter, true);
    }

    @Bean
    public FileConverter converter() {
        ExtensionQualifier extensionQualifier = new ExtensionQualifier(
                FileExtension.PDF,
                FileExtension.DOC,
                FileExtension.DOCX
        );
        FileParser fileParser = new FileParser(extensionQualifier);

        FileSheet<?> parser = Parser.onlyFileSheetParser();
        return new FileConverter(fileParser, extensionQualifier, parser);
    }
}
