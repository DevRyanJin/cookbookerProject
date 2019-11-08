package com.cookbooker.data;

import com.cookbooker.objects.Picture;
import com.cookbooker.objects.Recipe;

import java.util.Collection;

public interface Database {
    Collection<Recipe> getRecipeList(final boolean isPartial);

    Recipe getRecipeByID(final long id, final boolean isPartial);

    Collection<Recipe> getRecipeByName(final String query, final boolean isPartial);

    Collection<Recipe> getRecipeByIngredient(final String query, final boolean isPartial);

    Collection<Recipe> getRecipeByPreptime(final int target, final int range, final boolean isPartial);

    Collection<Recipe> getRecipeByServings(final int target, final int range, final boolean isPartial);

    Collection<Recipe> getRecipeByNameOrIngredient(final String query, final boolean isPartial);

    long storeRecipe(final Recipe recipe);

    boolean editRecipe(Recipe recipe);

    void removeRecipe(final long id);

    Picture getPictureByID(final long id);

    Picture addToGallery(final Picture picture, final Recipe recipe);

    Collection<Picture> getGallery(final long r_id, boolean isPartial);

    void removePicture(long id);
}
