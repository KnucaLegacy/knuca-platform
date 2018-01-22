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

@Controller
@RequestMapping("/upload")
public class UploadController {


    private UploadService service;

    public UploadController(UploadService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity uploadPdf(@RequestParam("file") MultipartFile file) throws IOException {
        if (!file.getOriginalFilename().matches(".*\\.(pdf)|(PDF)$")) {
            throw new RuntimeException("File is not pdf format.");
        }
        return ResponseEntity.ok().body(service.upload(file) + " sheets from " + file.getOriginalFilename() + " accepted on proceeding");
    }
}
