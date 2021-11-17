package com.codercampus.api.error;

import java.util.List;

public class ResourceError {
    private List<String> errors;

    public ResourceError(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
