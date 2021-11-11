package com.codercampus.api.exception;

public class CategoryNotFoundException extends Exception {

    private final String categoryName;

    private CategoryNotFoundException(String categoryName) {
        this.categoryName = categoryName;
    }

    public static CategoryNotFoundException createWith(String categoryName) {
            return new CategoryNotFoundException(categoryName);
    }
    
    @Override
    public String getMessage() {
        return "Category '" + categoryName + "' not found";
    }

}
