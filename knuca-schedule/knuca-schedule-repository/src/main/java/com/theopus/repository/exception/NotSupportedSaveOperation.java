package com.theopus.repository.exception;

/**
 * Created by Oleksandr_Tkachov on 24.12.2017.
 */
public class NotSupportedSaveOperation extends RuntimeException {
    public NotSupportedSaveOperation(String s) {
        super(s);
    }
}
