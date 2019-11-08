package com.cookbooker.logic.exceptions;

public class IngredientNotFoundException extends RuntimeException {
    public IngredientNotFoundException(String error) {
        super(error);
    }
}
