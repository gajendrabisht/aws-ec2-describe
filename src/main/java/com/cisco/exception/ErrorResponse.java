package com.cisco.exception;

import java.util.List;

public class ErrorResponse<T> {

    private List<T> errors;

    public ErrorResponse(List<T> errors) {
        this.errors = errors;
    }

    public List<T> getErrors() {
        return errors;
    }

}
