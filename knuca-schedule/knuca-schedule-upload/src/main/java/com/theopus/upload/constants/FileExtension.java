package com.theopus.upload.constants;

public enum FileExtension {
    DOCX("docx"),
    DOC("doc"),
    PDF("pdf"),
    TXT("txt"),
    NONE("none");

    private final String value;

    FileExtension(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
