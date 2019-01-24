package com.theopus.upload.file;

import com.google.common.io.ByteStreams;
import com.theopus.upload.constants.FileExtension;
import com.theopus.upload.entity.UploadEvent;
import com.theopus.upload.entity.UploadFile;
import com.theopus.upload.exception.IllegalFileException;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EventCreatorTest {


    private FileConverter converter = mock(FileConverter.class);

    @Test
    public void createEvent() throws Exception {
        UploadFile file = new UploadFile();
        UploadEvent expected = new UploadEvent();
        expected.setFiles(Arrays.asList(file));
        expected.setStatus(UploadEvent.Status.CREATED);
        File pdf = new File("src/test/resources/files/pdf-sample.pdf");

        MultipartFile multipartFile = new MockMultipartFile(pdf.getName(), pdf.getName(), "etc", ByteStreams.toByteArray(new FileInputStream(pdf)));
        when(converter.convertFile(any(File.class))).thenReturn(file);

        EventCreator eventCreator = new EventCreator(converter);

        UploadEvent actual = eventCreator.create(multipartFile);

        assertEquals(expected, actual);
    }

    @Test
    public void createEventMultiFilesAllPass() throws Exception {
        UploadFile filePdf = new UploadFile();
        filePdf.setExtension(FileExtension.PDF);
        UploadFile fileDoc = new UploadFile();
        fileDoc.setExtension(FileExtension.DOC);
        UploadEvent expected = new UploadEvent();
        expected.setFiles(Arrays.asList(filePdf,fileDoc));
        expected.setStatus(UploadEvent.Status.CREATED);
        File pdf = new File("src/test/resources/files/pdf-sample.pdf");
        File doc = new File("src/test/resources/files/doc-sample.doc");

        MultipartFile multipartPDF = new MockMultipartFile(pdf.getName(), pdf.getName(), "etc", ByteStreams.toByteArray(new FileInputStream(pdf)));
        MultipartFile multipartDOC = new MockMultipartFile(pdf.getName(), pdf.getName(), "etc", ByteStreams.toByteArray(new FileInputStream(doc)));
        when(converter.convertFile(any(File.class))).thenReturn(filePdf).thenReturn(fileDoc);

        EventCreator eventCreator = new EventCreator(converter, true);

        UploadEvent actual = eventCreator.create(Arrays.asList(multipartDOC, multipartPDF));

        assertEquals(expected, actual);
    }

    @Test(expected = IllegalFileException.class)
    public void createEventMultiFiles2outOf3Pass() throws Exception {
        UploadFile filePdf = new UploadFile();
        filePdf.setExtension(FileExtension.PDF);
        UploadFile fileDoc = new UploadFile();
        fileDoc.setExtension(FileExtension.DOC);

        File pdf = new File("src/test/resources/files/pdf-sample.pdf");
        File doc = new File("src/test/resources/files/doc-sample.doc");
        File docx = new File("src/test/resources/files/docx-sample.docx");

        MultipartFile multipartPDF = new MockMultipartFile(pdf.getName(), pdf.getName(), "etc", ByteStreams.toByteArray(new FileInputStream(pdf)));
        MultipartFile multipartDOC = new MockMultipartFile(pdf.getName(), pdf.getName(), "etc", ByteStreams.toByteArray(new FileInputStream(doc)));
        MultipartFile multipartDOCX = new MockMultipartFile(pdf.getName(), pdf.getName(), "etc", ByteStreams.toByteArray(new FileInputStream(docx)));
        when(converter.convertFile(any(File.class))).thenReturn(filePdf).thenThrow(new IllegalFileException("test")).thenReturn(fileDoc);

        EventCreator eventCreator = new EventCreator(converter, true);

        eventCreator.create(Arrays.asList(multipartDOC, multipartPDF, multipartDOCX));
    }

    @Test
    public void createEventMultiFiles2outOf3PassFailDisabled() throws Exception {
        UploadFile filePdf = new UploadFile();
        filePdf.setExtension(FileExtension.PDF);
        UploadFile fileDoc = new UploadFile();
        fileDoc.setExtension(FileExtension.DOC);

        UploadEvent expected = new UploadEvent();
        expected.setFiles(Arrays.asList(filePdf,fileDoc));
        expected.setStatus(UploadEvent.Status.CREATED);


        File pdf = new File("src/test/resources/files/pdf-sample.pdf");
        File doc = new File("src/test/resources/files/doc-sample.doc");
        File docx = new File("src/test/resources/files/docx-sample.docx");

        MultipartFile multipartPDF = new MockMultipartFile(pdf.getName(), pdf.getName(), "etc", ByteStreams.toByteArray(new FileInputStream(pdf)));
        MultipartFile multipartDOC = new MockMultipartFile(pdf.getName(), pdf.getName(), "etc", ByteStreams.toByteArray(new FileInputStream(doc)));
        MultipartFile multipartDOCX = new MockMultipartFile(pdf.getName(), pdf.getName(), "etc", ByteStreams.toByteArray(new FileInputStream(docx)));
        when(converter.convertFile(any(File.class))).thenReturn(filePdf).thenThrow(new IllegalFileException("test")).thenReturn(fileDoc);

        EventCreator eventCreator = new EventCreator(converter, false);

        UploadEvent actual = eventCreator.create(Arrays.asList(multipartDOC, multipartDOCX, multipartPDF));

        assertEquals(expected, actual);
    }
}