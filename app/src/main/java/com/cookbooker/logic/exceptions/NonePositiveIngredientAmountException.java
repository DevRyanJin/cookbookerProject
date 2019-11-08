package com.cookbooker.logic.exceptions;

public class NonePositiveIngredientAmountException extends InvalidIngredientException {
    public NonePositiveIngredientAmountException(String error) {
        super(error);
    }
}
