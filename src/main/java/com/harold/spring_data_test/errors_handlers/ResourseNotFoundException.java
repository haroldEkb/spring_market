package com.harold.spring_data_test.errors_handlers;


public class ResourseNotFoundException extends RuntimeException {
    public ResourseNotFoundException(String message) {
        super(message);
    }
}
