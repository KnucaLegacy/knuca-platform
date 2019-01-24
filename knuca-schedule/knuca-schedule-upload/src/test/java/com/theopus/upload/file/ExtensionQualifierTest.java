package com.theopus.upload.file;

import com.theopus.upload.constants.FileExtension;
import com.theopus.upload.exception.IllegalFileException;
import org.junit.Test;

import java.io.File;

public class ExtensionQualifierTest {

    @Test
    public void validateFileLowcase() throws Exception {
        ExtensionQualifier validator = new ExtensionQualifier(
                FileExtension.PDF,
                FileExtension.DOC,
                FileExtension.DOCX);

        File file = new File("kek.pdf");
        validator.validateFile(file);
    }

    @Test
    public void validateFileUPCase() throws Exception {
        ExtensionQualifier validator = new ExtensionQualifier(
                FileExtension.PDF,
                FileExtension.DOC,
                FileExtension.DOCX);

        File file = new File("kek.PDF");
        validator.validateFile(file);
    }

    @Test(expected = IllegalFileException.class)
    public void validateNotDefinedFile() throws Exception {
        ExtensionQualifier validator = new ExtensionQualifier(
                FileExtension.DOC,
                FileExtension.DOCX);

        File file = new File("kek.PDF");
        validator.validateFile(file);
    }
}
