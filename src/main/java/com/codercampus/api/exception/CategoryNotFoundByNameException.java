package com.codercampus.api.exception;

public class CategoryNotFoundByNameException extends Exception {

    private final String categoryName;

    private CategoryNotFoundByNameException(String categoryName) {
        this.categoryName = categoryName;
    }

    public static CategoryNotFoundByNameException createWith(String categoryName) {
            return new CategoryNotFoundByNameException(categoryName);
    }
    
    @Override
    public String getMessage() {
        return "Category '" + categoryName + "' not found";
    }

}
