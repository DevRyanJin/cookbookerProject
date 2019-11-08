package com.cookbooker.logic.exceptions;

public class EmptyRecipeNameException extends InvalidRecipeException {
    public EmptyRecipeNameException(String error) {
        super(error);
    }
}
