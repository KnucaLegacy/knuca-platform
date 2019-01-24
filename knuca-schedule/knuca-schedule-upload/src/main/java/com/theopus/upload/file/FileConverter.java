package com.theopus.upload.file;

import com.theopus.parser.exceptions.IllegalParserFileException;
import com.theopus.parser.obj.FileSheet;
import com.theopus.upload.constants.FileExtension;
import com.theopus.upload.entity.UploadFile;
import com.theopus.upload.exception.IllegalFileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDateTime;

public class FileConverter {

    private static final Logger LOG = LoggerFactory.getLogger(FileConverter.class);

    private final ExtensionQualifier qualifier;
    private final FileParser fileParser;
    private final FileSheet<?> parser;

    public FileConverter(FileParser fileParser, ExtensionQualifier extensionQualifier, FileSheet<?> parser) {
        this.qualifier = extensionQualifier;
        this.fileParser = fileParser;
        this.parser = parser;
    }

    public UploadFile convertFile(File file) {
        UploadFile result = new UploadFile();
        FileExtension fileExtension = qualifier.validateFile(file);
        String fileContent = fileParser.parseToString(file, fileExtension);
        Integer integer = totalSheets(fileContent);

        result.setExtension(fileExtension);
        result.setContent(fileContent);
        result.setTotalSheets(integer);
        result.setUploadTime(LocalDateTime.now());
        result.setName(file.getName());
        LOG.info("Successfully Converted file {}, to file event", file.getName());
        return result;
    }

    private Integer totalSheets(String content) {
        try {
            return parser.prepare(content).getTotal();
        } catch (IllegalParserFileException e) {
            throw new IllegalFileException("Cannot parse file sheet", e);
        }
    }
}
