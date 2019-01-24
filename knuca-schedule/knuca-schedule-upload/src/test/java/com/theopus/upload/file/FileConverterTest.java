package com.theopus.upload.file;

import com.theopus.parser.exceptions.IllegalParserFileException;
import com.theopus.parser.obj.FileSheet;
import com.theopus.upload.constants.FileExtension;
import com.theopus.upload.entity.UploadFile;
import com.theopus.upload.exception.IllegalFileException;
import com.theopus.upload.file.ExtensionQualifier;
import com.theopus.upload.file.FileConverter;
import com.theopus.upload.file.FileParser;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class FileConverterTest {

    private static final String sampleText = "text";

    private ExtensionQualifier eqMock = mock(ExtensionQualifier.class);
    private FileParser fpMock = mock(FileParser.class);
    private FileSheet fsMock = mock(FileSheet.class);

    @Before
    public void setUp() throws Exception {
        when(fpMock.parseToString(null)).thenReturn("text");
    }

    @Test
    public void uploadEventExtParsing() {
        UploadFile expected = new UploadFile();
        expected.setExtension(FileExtension.PDF);
        expected.setContent(sampleText);
        expected.setTotalSheets(1);
        File pdfFile = new File("file.pdf");
        expected.setName("file.pdf");

        when(eqMock.validateFile(pdfFile)).thenReturn(FileExtension.PDF);
        when(fpMock.parseToString(pdfFile, FileExtension.PDF)).thenReturn(sampleText);
        when(fsMock.prepare(sampleText)).thenReturn(fsMock);
        when(fsMock.getTotal()).thenReturn(1);

        FileConverter fileConverter = new FileConverter(fpMock, eqMock, fsMock);
        UploadFile actual = fileConverter.convertFile(pdfFile);

        assertEquals(expected, actual);
    }

    @Test(expected = IllegalFileException.class)
    public void uploadEventExtParsingPrepareThrewException() {
        UploadFile expected = new UploadFile();
        expected.setExtension(FileExtension.PDF);
        expected.setContent(sampleText);
        expected.setTotalSheets(1);
        File pdfFile = new File("file.pdf");
        expected.setName("file.pdf");

        when(eqMock.validateFile(pdfFile)).thenReturn(FileExtension.PDF);
        when(fpMock.parseToString(pdfFile, FileExtension.PDF)).thenReturn(sampleText);
        when(fsMock.prepare(sampleText)).thenThrow(new IllegalParserFileException("test message"));
        when(fsMock.getTotal()).thenReturn(1);

        FileConverter fileConverter = new FileConverter(fpMock, eqMock, fsMock);
        UploadFile actual = fileConverter.convertFile(pdfFile);
    }


    @Test(expected = IllegalFileException.class)
    public void uploadEventExtParsingConvertorThrewException() {
        UploadFile expected = new UploadFile();
        expected.setExtension(FileExtension.PDF);
        expected.setContent(sampleText);
        expected.setTotalSheets(1);
        File pdfFile = new File("file.pdf");

        when(eqMock.validateFile(pdfFile)).thenReturn(FileExtension.PDF);
        when(fpMock.parseToString(pdfFile, FileExtension.PDF)).thenThrow(new IllegalFileException("bad file"));
        when(fsMock.prepare(sampleText)).thenReturn(fsMock);
        when(fsMock.getTotal()).thenReturn(1);

        FileConverter fileConverter = new FileConverter(fpMock, eqMock, fsMock);
        UploadFile actual = fileConverter.convertFile(pdfFile);

        assertEquals(expected, actual);
    }

    @Test
    public void uploadEventExtParsingWithDate() {
        UploadFile expected = new UploadFile();
        expected.setUploadTime(LocalDateTime.now());
        File pdfFile = new File("file.pdf");

        when(eqMock.validateFile(pdfFile)).thenReturn(FileExtension.PDF);
        when(fpMock.parseToString(pdfFile, FileExtension.PDF)).thenReturn(sampleText);
        when(fsMock.prepare(sampleText)).thenReturn(fsMock);
        when(fsMock.getTotal()).thenReturn(1);

        FileConverter fileConverter = new FileConverter(fpMock, eqMock, fsMock);
        UploadFile actual = fileConverter.convertFile(pdfFile);


        boolean before = expected.getUploadTime().isBefore(actual.getUploadTime());
        boolean equal = expected.getUploadTime().isEqual(actual.getUploadTime());
        assertTrue(
                "Date before.",
                before || equal
        );
    }

    @Test
    public void uploadEventExtParsingWithName() {
        UploadFile expected = new UploadFile();
        File pdfFile = new File("file.pdf");
        expected.setName("file.pdf");

        when(eqMock.validateFile(pdfFile)).thenReturn(FileExtension.PDF);
        when(fpMock.parseToString(pdfFile, FileExtension.PDF)).thenReturn(sampleText);
        when(fsMock.prepare(sampleText)).thenReturn(fsMock);
        when(fsMock.getTotal()).thenReturn(1);

        FileConverter fileConverter = new FileConverter(fpMock, eqMock, fsMock);
        UploadFile actual = fileConverter.convertFile(pdfFile);

        assertEquals(expected.getName(), actual.getName());
    }


}
