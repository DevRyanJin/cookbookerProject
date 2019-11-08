package com.cookbooker.logic.exceptions;

public class EmptyIngredientNameException extends InvalidIngredientException {
    public EmptyIngredientNameException(String error) {
        super(error);
    }
}
