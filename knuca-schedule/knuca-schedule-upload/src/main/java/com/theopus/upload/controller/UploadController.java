package com.theopus.upload.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.theopus.upload.constants.Views;
import com.theopus.upload.entity.Report;
import com.theopus.upload.entity.UploadEvent;
import com.theopus.upload.entity.UploadFile;
import com.theopus.upload.file.EventCreator;
import com.theopus.upload.handling.EventLauncher;
import com.theopus.upload.handling.ReportService;
import com.theopus.upload.service.UploadEventService;
import com.theopus.upload.service.UploadFileService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/upload")
public class UploadController {

    private final UploadEventService eventService;
    private final UploadFileService fileService;
    private final EventCreator eventCreator;
    private final EventLauncher eventLauncher;
    private final ReportService reportService;

    public UploadController(UploadEventService eventService, UploadFileService fileService, EventCreator eventCreator,
                            EventLauncher eventLauncher, ReportService reportService) {
        this.eventService = eventService;
        this.fileService = fileService;
        this.eventCreator = eventCreator;
        this.eventLauncher = eventLauncher;
        this.reportService = reportService;
    }

    @PostMapping("/files")
    @JsonView(Views.Short.class)
    public ResponseEntity<UploadEvent> upload(@RequestParam("files") MultipartFile[] files, @RequestParam Integer year) throws IOException {
        UploadEvent save = eventService.save(eventCreator.create(Arrays.asList(files)));
        eventLauncher.submit(save);
        return ResponseEntity.ok().body(save);
    }

    @GetMapping("/events")
    @JsonView(Views.Short.class)
    public ResponseEntity<List<UploadEvent>> events() {
        return ResponseEntity.ok(eventService.getAll());
    }

    @GetMapping("/files")
    @JsonView(Views.Short.class)
    public ResponseEntity<List<UploadFile>> files() {
        return ResponseEntity.ok(fileService.getAll());
    }

    @GetMapping("/reports")
    @JsonView(Views.Short.class)
    public ResponseEntity<List<Report>> reports() {
        return ResponseEntity.ok(reportService.getAll());
    }


    @GetMapping("/events/view")
    public ModelAndView eventsView(){
        ModelAndView modelAndView = new ModelAndView("events");
        modelAndView.addObject("events", eventService.getAll());
        return modelAndView;
    }

    @GetMapping("/files/view")
    public ModelAndView filesView(){
        ModelAndView modelAndView = new ModelAndView("files");
        modelAndView.addObject("files", fileService.getAll());
        return modelAndView;
    }
    @GetMapping("/files/{fileId}/view")
    public ModelAndView oneFilsView(@PathVariable Long fileId){
        ModelAndView modelAndView = new ModelAndView("file");
        modelAndView.addObject("file", fileService.get(fileId));
        return modelAndView;
    }

    @GetMapping("/reports/view")
    public ModelAndView reportsView(){
        ModelAndView modelAndView = new ModelAndView("reports");
        modelAndView.addObject("reports", reportService.getAll());
        return modelAndView;
    }

    @GetMapping("/reports/{reportId}/view")
    public ModelAndView reportView(@PathVariable("reportId") Long reportId){
        ModelAndView modelAndView = new ModelAndView("report");
        modelAndView.addObject("report", reportService.get(reportId));
        return modelAndView;
    }
}
