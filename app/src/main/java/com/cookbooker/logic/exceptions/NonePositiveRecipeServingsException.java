package com.cookbooker.logic.exceptions;

public class NonePositiveRecipeServingsException extends InvalidRecipeException {
    public NonePositiveRecipeServingsException(String error) {
        super(error);
    }
}
