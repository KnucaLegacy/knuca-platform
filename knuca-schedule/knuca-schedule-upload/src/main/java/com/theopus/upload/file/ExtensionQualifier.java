package com.theopus.upload.file;

import com.theopus.upload.constants.FileExtension;
import com.theopus.upload.exception.IllegalFileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ExtensionQualifier {
    private static final Logger LOG = LoggerFactory.getLogger(ExtensionQualifier.class);
    private final Map<Pattern, FileExtension> extensionsPatterns;

    public ExtensionQualifier(FileExtension... extensions) {
        this.extensionsPatterns = new HashMap<>();
        for (FileExtension fe : extensions) {
            extensionsPatterns.put(
                    Pattern.compile(".*\\." + fe.value() + "$", Pattern.CASE_INSENSITIVE),
                    fe
            );
        }
    }

    public FileExtension validateFile(File file) {
        String name = file.getName();
        for (Pattern pattern : extensionsPatterns.keySet()) {
            if (pattern.matcher(name).matches()) {
                FileExtension ext = extensionsPatterns.get(pattern);
                LOG.info("File {} matches to {}", file, ext);
                return ext;
            }
        }
        throw new IllegalFileException(name + "is not appropriate file extension");
    }
}
