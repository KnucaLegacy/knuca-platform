package com.theopus.upload.file;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import com.theopus.parser.ParserUtils;
import com.theopus.upload.entity.UploadEvent;
import com.theopus.upload.entity.UploadFile;
import com.theopus.upload.exception.IllegalFileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class EventCreator {
    private static final Logger LOG = LoggerFactory.getLogger(EventCreator.class);
    private final FileConverter converter;
    private final boolean rejectOnCorruptedFile;

    public EventCreator(FileConverter converter, boolean rejectOnCorruptedFile) {
        this.converter = converter;
        this.rejectOnCorruptedFile = rejectOnCorruptedFile;
    }

    public EventCreator(FileConverter converter) {
        this.converter = converter;
        this.rejectOnCorruptedFile = true;
    }


    public UploadEvent create(MultipartFile multipartFile) throws IOException {
        return create(multipartFile, 0);
    }

    public UploadEvent create(Collection<MultipartFile> fileCollection) {
        return create(fileCollection, 0);
    }

    public UploadEvent create(MultipartFile multipartFile, Integer year) throws IOException {
        UploadEvent result = new UploadEvent();

        File file = toFile(multipartFile);
        UploadFile uploadFile = converter.convertFile(file);
        file.delete();
        uploadFile.setAssignedYear(year);

        result.setStatus(UploadEvent.Status.CREATED);
        result.setFiles(Arrays.asList(uploadFile));

        return result;
    }

    public UploadEvent create(Collection<MultipartFile> fileCollection, Integer year) {
        UploadEvent event = new UploadEvent();
        List<UploadFile> fileList = new ArrayList<>();

        for (MultipartFile multipartFile : fileCollection) {
            try {
                File file = toFile(multipartFile);
                UploadFile uploadFile = converter.convertFile(file);
                file.delete();
                uploadFile.setAssignedYear(year);
                fileList.add(uploadFile);
            } catch (IOException | IllegalFileException e) {
                String msg = "Not suitable file `" + multipartFile.getName() + "` for parsing.";
                if (rejectOnCorruptedFile) {
                    throw new IllegalFileException(msg + " Interrupting.", e);
                } else {
                    LOG.error(msg + " Skipping.", e);
                }
            }
        }
        LOG.info("Converted {} files of {}.", fileList.size(), fileCollection.size());
        event.setFiles(fileList);
        event.setStatus(UploadEvent.Status.CREATED);
        return event;
    }


    private File toFile(MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        InputStream inputStream = multipartFile.getInputStream();
        Files.write(ByteStreams.toByteArray(inputStream), file);
        inputStream.close();
        return file;
    }
}
