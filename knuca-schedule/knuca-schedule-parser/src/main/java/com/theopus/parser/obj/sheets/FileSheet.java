package com.theopus.parser.obj.sheets;

public abstract class FileSheet<T> {

    public FileSheet<T> child(Sheet<T> sheet) {
        return this;
    }
}
