package com.theopus.upload.controller;

import com.theopus.upload.service.UploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/upload")
public class UploadController {


    private UploadService service;

    public UploadController(UploadService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity uploadPdf(@RequestParam("files") MultipartFile[] files, @RequestParam Integer year) throws IOException {

        System.out.println(Arrays.toString(files));
        int totalsheets = 0;
        for (MultipartFile file : files) {
            if (!file.getOriginalFilename().matches(".*\\.(pdf)|(PDF)$")) {
                throw new RuntimeException("File is not pdf format.");
            }

            if (year != null) {
                totalsheets += service.upload(file, year);
            } else {
                totalsheets += service.upload(file);
            }
        }



        return ResponseEntity.ok().body(totalsheets + " sheets from " + files.length + "files accepted on proceeding");
    }
}
