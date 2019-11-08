package com.cookbooker.objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Recipe {
    //Mandatory Properties
    private final Long recipeId;  //Unique ID assigned by the database
    private final String name;    //UserSide name of the recipe

    //Optional Properties
    private List<String> directions = new ArrayList<>();   //all the directionss
    private Collection<Ingredient> ingredients = new ArrayList<>();  //all the ingredients.
    private Collection<Picture> pictures = new ArrayList<>();
    private int prepTime = -1;   //Time required to prepare recipe.
    private int servings = -1;   //the number of servings the recipe makes

    public Long getRecipeId() {
        return recipeId;
    }

    public String getName() {
        return name;
    }

    public List<String> getDirections() {
        return directions;
    }

    public Collection<Ingredient> getIngredients() {
        return ingredients;
    }

    public Collection<Picture> getPictures() {
        return pictures;
    }

    public int getPrepTime() {
        return prepTime;
    }

    public int getServings() {
        return servings;
    }

    //Builder must be used to construct a full instance of this class
    private Recipe(final Long recipeId, final String name,
                   List<String> directions, Collection<Ingredient> ingredients,
                   Collection<Picture> pictures, int prepTime, int servings) {
        this.recipeId = recipeId;
        this.name = name;
        this.pictures = pictures;
        this.directions = directions;
        this.ingredients = ingredients;
        this.prepTime = prepTime;
        this.servings = servings;
    }

    public static class RecipeBuilder {
        private Long recipeId;  //Unique ID assigned by the database
        private String name;    //UserSide name of the recipe
        private List<String> directions;   //String of all the directionss
        private Collection<Ingredient> ingredients;  //not sure if we need to make separate ingredients class.
        private Collection<Picture> pictures;
        private long mainPictureId; //Picture to be displayed in the MainActivity, Currently references drawable
        private int prepTime;   //Time required to prepare recipe.
        private int servings;   //the nuto use mber of servings the recipe makes

        public RecipeBuilder setRecipeId(Long recipeId) {
            this.recipeId = recipeId;
            return this;
        }

        public RecipeBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public RecipeBuilder setDirections(List<String> directions) {
            this.directions = directions;
            return this;
        }

        public RecipeBuilder setIngredients(Collection<Ingredient> ingredients) {
            this.ingredients = ingredients;
            return this;
        }

        public RecipeBuilder addPicture(Picture picture) {
            if (this.pictures == null)
                this.pictures = new ArrayList<>();
            this.pictures.add(picture);
            return this;
        }

        public RecipeBuilder setPictures(Collection<Picture> pictures) {
            this.pictures = pictures;
            return this;
        }

        public RecipeBuilder setPrepTime(int prepTime) {
            this.prepTime = prepTime;
            return this;
        }

        public RecipeBuilder setServings(int servings) {
            this.servings = servings;
            return this;
        }

        public Recipe build() {
            return new Recipe(recipeId, name, directions, ingredients, pictures, prepTime, servings);
        }
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Recipe || object instanceof Recipe.RecipeBuilder)
            return this.recipeId.equals(((Recipe) object).recipeId);
        return false;
    }

    @Override
    public int hashCode() {
        return Integer.parseInt(this.getRecipeId() + "");
    }

    @Override
    public String toString() {
        String ret = "Recipe:";
        ret += "\n\tID:\t" + recipeId;
        ret += "\n\tName:\t" + name;
        ret += "\n\tPicNil:\t" + (pictures == null);
        ret += "\n\tDirectNil:\t" + (directions == null);
        ret += "\n\tIngredNil:\t" + (ingredients == null);
        ret += "\n\tPicsNil:\t" + (pictures == null);
        ret += "\n\tPrep:\t" + prepTime;
        ret += "\n\tServ:\t" + servings;
        return ret;
    }
}