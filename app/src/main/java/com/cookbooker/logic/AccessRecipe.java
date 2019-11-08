package com.cookbooker.logic;

import com.cookbooker.data.Database;
import com.cookbooker.logic.exceptions.AccessRecipeException;
import com.cookbooker.objects.Picture;
import com.cookbooker.objects.Recipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class AccessRecipe {
    private Database dataBase;

    public AccessRecipe(Database dataBase) {
        this.dataBase = dataBase;
    }

    /**
     * @return Collection of ALL Recipes from the database
     */
    public Collection<Recipe> getAllRecipes() {
        return this.dataBase.getRecipeList(true);
    }


    /**
     * Searches for recipes by (RECIPE %NAME% || RECIPE INCLUDING INGREDIENT %NAME%)
     * If query is empty, method returns entire collection of recipes.
     *
     * @param queries string to search for
     * @return Collection of 0+ recipes matching query
     */
    public Collection<Recipe> searchForRecipes(Collection<String> queries) {
        //Default to all recipes for a search of nothing
        if (queries.size() == 0)
            return getAllRecipes();

        ArrayList<Recipe> foundRecipes = new ArrayList<>();
        for (String query : queries) {
            query = query.trim();
            for (Recipe rep : this.dataBase.getRecipeByIngredient(query, true))
                if (!foundRecipes.contains(rep))
                    foundRecipes.add(rep);
            for (Recipe rep : this.dataBase.getRecipeByName(query, true))
                if (!foundRecipes.contains(rep))
                    foundRecipes.add(rep);
        }

        return foundRecipes;
    }

    /**
     * Returns a single FULL Recipe given an ID, throws AccessRecipeException if recipe doesn't exist
     *
     * @param rid id of the recipe to fetch
     * @return recipe from the database
     * @throws AccessRecipeException when recipe is invalid
     */
    public Recipe getRecipe(long rid) throws AccessRecipeException {
        Recipe found = this.dataBase.getRecipeByID(rid, false);
        RecipeValidator.validateAll(found);
        return found;
    }

    /**
     * Adds a recipe to the database
     *
     * @param newRecipe New recipe to be added into the database
     * @throws AccessRecipeException thrown when an invalid recipe is attempted to be stored
     */
    public void addRecipe(Recipe newRecipe) throws AccessRecipeException {
        RecipeValidator.validateAll(newRecipe);
        try {
            this.dataBase.storeRecipe(newRecipe);
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            throw new AccessRecipeException("Invalid recipe\n");
        }
    }

    /**
     * Adds the specific picture to the gallery with an association to the recipe
     *
     * @param picture Picture to store in the gallery
     * @param recipe  Recipe to have the picture associated with
     * @return The complete picture object (Picture.id may be different from when it was passed in)
     */
    public Picture addPictureToRecipe(Picture picture, Recipe recipe) {
        return this.dataBase.addToGallery(picture, recipe);
    }

    public void editRecipe(Recipe newRecipe) throws AccessRecipeException {
        RecipeValidator.validateAll(newRecipe);
        try {
            this.dataBase.editRecipe(newRecipe);
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            throw new AccessRecipeException("Unable to edit recipe\n");
        }
        return;
    }

    //Passing an entire object reference is unnecessary when deleting a recipe.  Change parameter to ID instead of a recipe object.
    public void deleteRecipe(Recipe newRecipe) throws AccessRecipeException {
        RecipeValidator.validateAll(newRecipe);
        try {
            this.dataBase.removeRecipe(newRecipe.getRecipeId());
        } catch (RuntimeException e) {
            throw new AccessRecipeException("Unable to delete recipe\n" + e.getMessage());
        }
    }

    /**
     * Given a list of recipes and a Random generator, pick a recipe from the list.
     *
     * @param rand Random Generator (Default is java.util.Random)
     * @param list A List of recipes to select from
     * @return A single recipe from list
     */
    public Recipe generateDailyRecipe(Random rand, List<Recipe> list) {
        return list.get(rand.nextInt(list.size()));
    }

}