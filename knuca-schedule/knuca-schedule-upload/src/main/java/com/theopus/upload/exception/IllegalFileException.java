package com.theopus.upload.exception;

public class IllegalFileException extends RuntimeException {

    public IllegalFileException(String message) {
        super(message);
    }

    public IllegalFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
