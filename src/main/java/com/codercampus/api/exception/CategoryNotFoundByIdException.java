package com.codercampus.api.exception;

public class CategoryNotFoundByIdException extends Exception {

    private final Long categoryId;

    private CategoryNotFoundByIdException(Long categoryId) {
        this.categoryId = categoryId;
    }

    public static CategoryNotFoundByIdException createWith(Long categoryId) {
            return new CategoryNotFoundByIdException(categoryId);
    }
    
    @Override
    public String getMessage() {
        return "Category with '" + categoryId + "' not found";
    }

}
