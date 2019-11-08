package com.cookbooker.logic.exceptions;

public class RecipeHasNoIngredientException extends InvalidIngredientException {
    public RecipeHasNoIngredientException(String error) {
        super(error);
    }
}
