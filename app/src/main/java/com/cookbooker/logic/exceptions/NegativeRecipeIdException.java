package com.cookbooker.logic.exceptions;

public class NegativeRecipeIdException extends RecipeHasInvalidIdException {
    public NegativeRecipeIdException(String error) {
        super(error);
    }
}
