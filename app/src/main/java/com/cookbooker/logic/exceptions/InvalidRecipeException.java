package com.cookbooker.logic.exceptions;

public class InvalidRecipeException extends RuntimeException {
    public InvalidRecipeException(String error) {
        super("Unable to access data:\n" + error);
    }
}
