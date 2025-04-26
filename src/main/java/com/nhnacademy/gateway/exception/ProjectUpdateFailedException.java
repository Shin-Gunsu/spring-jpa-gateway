package com.nhnacademy.gateway.exception;

public class ProjectUpdateFailedException extends RuntimeException {
    public ProjectUpdateFailedException(String message) {
        super(message);
    }
}
