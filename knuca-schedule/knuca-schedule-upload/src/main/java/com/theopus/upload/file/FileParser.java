package com.theopus.upload.file;

import com.theopus.parser.ParserUtils;
import com.theopus.upload.constants.FileExtension;
import com.theopus.upload.exception.IllegalFileException;
import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class FileParser {
    private static final Logger LOG = LoggerFactory.getLogger(FileParser.class);

    private final ExtensionQualifier extensionQualifier;

    public FileParser(ExtensionQualifier extensionQualifier) {
        this.extensionQualifier = extensionQualifier;
    }

    public String parseToString(File file) throws IllegalFileException {
        FileExtension fileExtension = extensionQualifier.validateFile(file);
        return parseToString(file, fileExtension);
    }

    public String parseToString(File file, FileExtension fileExtension) {
        try {
            switch (fileExtension) {
                case PDF:
                    return ParserUtils.pdfFileToString(file);
                case DOCX:
                    return ParserUtils.docxFileToString(file);
                case TXT:
                    return ParserUtils.fileToString(file);
                case DOC:
                    return parseDoc(file);
                default: {
                    LOG.info("Not suitable action for type {}, parsing as txt file", fileExtension);
                    return ParserUtils.fileToString(file);
                }
            }
        } catch (OLE2NotOfficeXmlFileException | IOException e) {
            throw new IllegalFileException("File " + file.getName() + " qualified as " + fileExtension + " file, but can not be read.", e);
        }
    }

    private String parseDoc(File file) throws IOException {
        try {
            return ParserUtils.docFileToString(file);
        } catch (IllegalArgumentException e) {
            LOG.info("File `" + file.getName() + "` qualified as DOC file, but can not be read.", e);
        }
        return ParserUtils.msdosFileToString(file);
    }
}
