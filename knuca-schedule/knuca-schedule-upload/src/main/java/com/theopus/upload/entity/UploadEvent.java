package com.theopus.upload.entity;

import javax.persistence.Entity;
import java.io.File;

@Entity
public class UploadEvent {

    private Long id;
    private Status status;
    private File pdfFile;


    enum Status {
        IN_QUEUE,
        IN_PROCESS,
        SUCCEEDED,
        FAILED,
    }
}
