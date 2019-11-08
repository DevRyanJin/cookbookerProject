package com.cookbooker.logic.exceptions;

public class NonePositiveRecipePrepTimeException extends InvalidRecipeException {
    public NonePositiveRecipePrepTimeException(String error) {
        super(error);
    }
}
