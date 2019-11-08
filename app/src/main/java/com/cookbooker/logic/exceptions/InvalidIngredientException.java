package com.cookbooker.logic.exceptions;

public class InvalidIngredientException extends InvalidRecipeException {
    public InvalidIngredientException(String error) {
        super(error);
    }
}
