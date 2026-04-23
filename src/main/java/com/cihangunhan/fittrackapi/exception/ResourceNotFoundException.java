package com.cihangunhan.fittrackapi.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " bulunamadı: " + id);
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}