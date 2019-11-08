package com.cookbooker.logic.exceptions;

public class RecipeHasDuplicateIdException extends RecipeHasInvalidIdException {
    public RecipeHasDuplicateIdException(String error) {
        super(error);
    }
}
