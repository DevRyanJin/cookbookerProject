package com.cookbooker.logic;

import com.cookbooker.logic.exceptions.EmptyIngredientNameException;
import com.cookbooker.logic.exceptions.EmptyRecipeDirectionException;
import com.cookbooker.logic.exceptions.EmptyRecipeNameException;
import com.cookbooker.logic.exceptions.InvalidIngredientException;
import com.cookbooker.logic.exceptions.InvalidPictureGalleryException;
import com.cookbooker.logic.exceptions.InvalidRecipeException;
import com.cookbooker.logic.exceptions.NonePositiveIngredientAmountException;
import com.cookbooker.logic.exceptions.NonePositiveRecipePrepTimeException;
import com.cookbooker.logic.exceptions.NonePositiveRecipeServingsException;
import com.cookbooker.logic.exceptions.RecipeHasNoIngredientException;
import com.cookbooker.objects.Ingredient;
import com.cookbooker.objects.Recipe;

import java.util.ArrayList;

public class RecipeValidator {

    public static void validateAll(Recipe recipe) throws InvalidRecipeException {
        validateName(recipe);
        validateDirection(recipe);
        validateIngredients(recipe);
    }

    public static void validateName(Recipe recipe) throws InvalidRecipeException {
        if (recipe == null) {
            throw new InvalidRecipeException("Recipe object was null");
        } else if (recipe.getName() == null) {
            throw new InvalidRecipeException("Recipe name was null");
        } else if (recipe.getName().isEmpty()) {
            throw new EmptyRecipeNameException("Recipe name was empty");
        }
    }

    public static void validateDirection(Recipe recipe) throws InvalidRecipeException {
        if (recipe == null) {
            throw new InvalidRecipeException("Recipe was null");
        } else if (recipe.getDirections() == null) {
            throw new InvalidRecipeException("Recipe directions were null");
        } else if (recipe.getDirections().isEmpty()) {
            throw new EmptyRecipeDirectionException("Recipe Directions are empty");

        }
    }

    public static void validatePreptime(Recipe recipe) throws InvalidRecipeException {
        if (recipe == null) {
            throw new InvalidRecipeException("Recipe was null");
        } else if (recipe.getPrepTime() <= 0) {
            throw new NonePositiveRecipePrepTimeException("Recipe prep time was negative");
        }

    }

    public static void validateServings(Recipe recipe) throws InvalidRecipeException {
        if (recipe == null) {
            throw new InvalidRecipeException("Recipe was null");
        } else if (recipe.getServings() <= 0) {
            throw new NonePositiveRecipeServingsException("Recipe Servings was negative");
        }

    }

    public static void validatePictures(Recipe recipe) throws InvalidRecipeException {
        if (recipe == null) {
            throw new InvalidRecipeException("Recipe was null");
        } else if (recipe.getPictures() == null) {
            throw new InvalidPictureGalleryException("Picture collection was empty");
        } else if (recipe.getPictures().size() < 1) {
            throw new InvalidPictureGalleryException("Picture Collection was empty");
        }
    }

    public static void validateIngredients(Recipe recipe) throws InvalidIngredientException {
        if (recipe == null) {
            throw new InvalidRecipeException("Recipe was null");
        } else if (recipe.getIngredients() == null) {
            throw new RecipeHasNoIngredientException("Recipe ingredients collection was null");
        } else {
            containsValidIngredients(new ArrayList<>(recipe.getIngredients()));
        }
    }

    private static void containsValidIngredients(ArrayList<Ingredient> ingredients) throws InvalidIngredientException {
        if (ingredients == null || ingredients.size() < 1) {
            throw new RecipeHasNoIngredientException("Recipe was null");
        }

        for (int i = 0; i < ingredients.size(); i++) {

            if (ingredients.get(i) == null) {
                throw new InvalidIngredientException("Ingredient is null");

            } else if (ingredients.get(i).amount() <= 0) {
                throw new NonePositiveIngredientAmountException("Ingredient amount is zero");

            } else if (ingredients.get(i).substance().isEmpty()) {
                throw new EmptyIngredientNameException("Ingredient name is empty");
            }
        }
    }
}