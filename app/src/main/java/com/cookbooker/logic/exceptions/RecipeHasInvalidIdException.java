package com.cookbooker.logic.exceptions;

public class RecipeHasInvalidIdException extends InvalidRecipeException {
    public RecipeHasInvalidIdException(String error) {
        super(error);
    }
}
