package com.theopus.upload.file;

import com.theopus.upload.constants.FileExtension;
import com.theopus.upload.exception.IllegalFileException;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FileParserTest {

    private ExtensionQualifier extensionQualifier = mock(ExtensionQualifier.class);

    @Test
    public void parsePdfFile() throws Exception {
        File pdfFile = new File("src/test/resources/files/pdf-sample.pdf");
        when(extensionQualifier.validateFile(pdfFile)).thenReturn(FileExtension.PDF);

        FileParser fileParser = new FileParser(extensionQualifier);
        String actual = fileParser.parseToString(pdfFile);

        assertNotNull(actual);
    }

    @Test(expected = IllegalFileException.class)
    public void parseFakePdfFile() throws Exception {
        File fakePdfFile = new File("src/test/resources/files/fake-pdf-sample.pdf");
        when(extensionQualifier.validateFile(fakePdfFile)).thenReturn(FileExtension.PDF);

        FileParser fileParser = new FileParser(extensionQualifier);
        String actual = fileParser.parseToString(fakePdfFile);

        assertNotNull(actual);
    }

    @Test
    public void parseDocFile() throws Exception {
        File doc = new File("src/test/resources/files/doc-sample.doc");
        when(extensionQualifier.validateFile(doc)).thenReturn(FileExtension.DOC);

        FileParser fileParser = new FileParser(extensionQualifier);
        String actual = fileParser.parseToString(doc);

        assertNotNull(actual);
    }

    @Test
    public void parseDocxFile() throws Exception {
        File docx = new File("src/test/resources/files/docx-sample.docx");
        when(extensionQualifier.validateFile(docx)).thenReturn(FileExtension.DOCX);

        FileParser fileParser = new FileParser(extensionQualifier);
        String actual = fileParser.parseToString(docx);

        assertNotNull(actual);
    }

    @Test(expected = IllegalFileException.class)
    public void parseFakeDocxFile() throws Exception {
        File fakeDocx = new File("src/test/resources/files/fake-docx-sample.docx");
        when(extensionQualifier.validateFile(fakeDocx)).thenReturn(FileExtension.DOCX);

        FileParser fileParser = new FileParser(extensionQualifier);
        String actual = fileParser.parseToString(fakeDocx);
        System.out.println(actual);
        assertNotNull(actual);
    }

    @Test
    public void parseFakeMsdosDocFile() throws Exception {
        File fakeDocx = new File("src/test/resources/files/msdos-sample.doc");
        when(extensionQualifier.validateFile(fakeDocx)).thenReturn(FileExtension.DOC);

        FileParser fileParser = new FileParser(extensionQualifier);
        String actual = fileParser.parseToString(fakeDocx);

        assertNotNull(actual);
    }

    @Test
    public void parseTxtFile() throws Exception {
        File doc = new File("src/test/resources/files/txt-sample.txt");
        when(extensionQualifier.validateFile(doc)).thenReturn(FileExtension.TXT);

        FileParser fileParser = new FileParser(extensionQualifier);
        String actual = fileParser.parseToString(doc);

        assertNotNull(actual);
    }

    @Test
    public void parseNoneFile() throws Exception {
        File doc = new File("src/test/resources/files/txt-sample.txt");
        when(extensionQualifier.validateFile(doc)).thenReturn(FileExtension.NONE);

        FileParser fileParser = new FileParser(extensionQualifier);
        String actual = fileParser.parseToString(doc);

        assertNotNull(actual);
    }
}