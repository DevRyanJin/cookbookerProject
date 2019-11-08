package com.cookbooker.logic.exceptions;

public class EmptyRecipeDirectionException extends InvalidRecipeException {
    public EmptyRecipeDirectionException(String error) {
        super(error);
    }
}
