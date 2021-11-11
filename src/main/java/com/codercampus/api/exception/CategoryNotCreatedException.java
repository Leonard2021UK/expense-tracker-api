package com.codercampus.api.exception;

public class CategoryNotCreatedException extends Exception {

    private final String categoryName;

    private CategoryNotCreatedException(String categoryName) {
        this.categoryName = categoryName;
    }

    public static CategoryNotCreatedException createWith(String categoryName) {
            return new CategoryNotCreatedException(categoryName);
    }
    
    @Override
    public String getMessage() {
        return "Category '" + categoryName + "' was not created";
    }

}
