package com.theopus.upload.handling;

import com.theopus.entity.schedule.Circumstance;
import com.theopus.entity.schedule.Curriculum;
import com.theopus.parser.exceptions.IllegalParserFileException;
import com.theopus.parser.obj.FileSheet;
import com.theopus.parser.obj.ValidatorReport;
import com.theopus.repository.service.CurriculumService;
import com.theopus.upload.entity.UploadEvent;
import com.theopus.upload.entity.UploadFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class EventHandler {
    private static final Logger LOG = LoggerFactory.getLogger(EventHandler.class);

    private final CurriculumService curriculumService;
    private final ParserDistributor distributor;
    private final ReportService reportService;

    public EventHandler(CurriculumService curriculumService,
                        ParserDistributor distributor, ReportService reportService) {
        this.curriculumService = curriculumService;
        this.distributor = distributor;
        this.reportService = reportService;
    }

    //TODO make more understandable/usable return type for method
    //TODO refactor to smaller methods.
    public long process(UploadEvent event) {
        long result = 0;
        for (UploadFile file : event.getFiles()) {
            Integer totalSheets = file.getTotalSheets();
            LOG.info("File {} with {} sheets parsing has started...", file.getName(), totalSheets);
            FindAndRemoveStrategy<?> strategy = distributor.removeStrategy(file.getContent());
            FileSheet<?> parser = distributor.parser(file.getContent());

            if (file.getAssignedYear() != 0) {
                parser.prepare(file.getContent(), file.getAssignedYear());
            } else {
                parser.prepare(file.getContent());
            }

            int currentSheet = 1;
            while (parser.next()) {
                //TODO handle deletions
                try {
                    List<Circumstance> andRemove = strategy.findAndRemove(LocalDate.now());
                    List<Curriculum> parse = parser.parse();
                    ValidatorReport reportOfCurrentSheet = parser.getReportOfCurrentSheet();
                    if (reportOfCurrentSheet.getViolationsCount() > 0) {
                        reportService.reportSheet(reportOfCurrentSheet, file, currentSheet, strategy.getAnchorName());
                    }
                    result += curriculumService.saveAll(parse).size();
                    curriculumService.flush();
                    LOG.info("{} / {} parsed", currentSheet++, totalSheets);
                } catch (IllegalParserFileException e) {
                    LOG.error("File " + file.getName() + " error skipping.", e);
                }
            }
        }
        LOG.info("Finished processing {} event. Saved {} curriculums.", event.getId(), result);
        return result;
    }
}
