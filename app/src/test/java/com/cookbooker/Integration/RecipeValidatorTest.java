package com.cookbooker.Integration;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.cookbooker.logic.exceptions.InvalidIngredientException;
import com.cookbooker.logic.exceptions.InvalidPictureGalleryException;
import com.cookbooker.logic.exceptions.InvalidRecipeException;
import com.cookbooker.objects.Ingredient;
import com.cookbooker.objects.Picture;
import com.cookbooker.objects.Recipe;
import com.cookbooker.logic.*;

public class RecipeValidatorTest {

    @Before
    public void testRecipeValidator() {
        System.out.println("Starting test for RecipeValidator");
    }

    @Test
    public void testRecipeValidateName_validName() {
        System.out.println("Testing validateName() with valid name");

        Recipe.RecipeBuilder rb = new Recipe.RecipeBuilder();
        rb.setName("Strawberry Pavlova");
        Recipe okRecipe = rb.build();


        boolean success = true;

        try {
            RecipeValidator.validateName(okRecipe);
        } catch (InvalidRecipeException e) {
            success = false;
        }
        assertTrue("Form was incorrectly validated", success);

    }


    @Test
    public void testRecipeValidateName_InvalidName() {
        System.out.println("Testing validateName() with invalid name");

        Recipe.RecipeBuilder rb = new Recipe.RecipeBuilder();
        rb.setName("");
        Recipe badRecipe = rb.build();

        boolean success = true;

        try {
            RecipeValidator.validateName(badRecipe);
        } catch (InvalidRecipeException e) {
            success = false;
        }
        assertFalse("Expected invalid one was valid", success);

    }

    @Test
    public void testRecipeValidatePictures_ValidPicture() {
        System.out.println("Testing validatePictures() with valid Collection<Picture>");
        Recipe.RecipeBuilder rb = new Recipe.RecipeBuilder();
        ArrayList<Picture> pics = new ArrayList();
        pics.add(new Picture(1,new byte[16]));
        pics.add(new Picture(2,new byte[16]));
        Recipe goodRecipe = rb.setRecipeId(1L).setName("GoodRecipe").setPictures(pics).build();
        try {
            RecipeValidator.validatePictures(goodRecipe);
        } catch (InvalidPictureGalleryException e) {
            assertTrue("Picture gallery was incorrectly validated", false);
        }
    }

    @Test
    public void testRecipeValidatePictures_InvalidPicture() {
        System.out.println("Testing validatePictures() with invalid Collection<Picture>");
        Recipe.RecipeBuilder rb = new Recipe.RecipeBuilder();
        ArrayList<Picture> pics = new ArrayList();
        Recipe badRecipe = rb.setRecipeId(1L).setName("BadRecipe").setPictures(pics).build();
        boolean success = true;
        try {
            RecipeValidator.validatePictures(badRecipe);
            success = true;
        } catch (InvalidPictureGalleryException e) {
            success = false;
        }
        assertFalse("Picture gallery was Correctly validated", success);
    }

    @Test
    public void testRecipeValidateDirection_validDirection() {
        System.out.println("Testing validateDirection() with valid direction");

        Recipe.RecipeBuilder rb = new Recipe.RecipeBuilder();
        rb.setName("Strawberry Pavlova");
        List<String> l = new ArrayList();
        l.add("whip it good");
        rb.setDirections(l);
        Recipe okRecipe = rb.build();

        boolean success = true;

        try {
            RecipeValidator.validateDirection(okRecipe);
        } catch (InvalidRecipeException e) {
            success = false;
        }
        assertTrue("Form was incorrectly validated", success);
    }


    @Test
    public void testRecipeValidateDirection_InvalidDirection() {
        System.out.println("Testing validaDirection() with invalid direction");

        Recipe.RecipeBuilder rb = new Recipe.RecipeBuilder();
        rb.setName("Strawberry Pavlova");
        List<String> l = new ArrayList();
        rb.setDirections(l);
        Recipe badRecipe = rb.build();

        boolean success = true;

        try {
            RecipeValidator.validateDirection(badRecipe);
        } catch (InvalidRecipeException e) {
            success = false;
        }
        assertFalse("Expected invalid one was valid", success);
    }


    @Test
    public void testRecipeValidateServings_validServings() {
        System.out.println("Testing validaDirection() with valid servings");


        Recipe.RecipeBuilder rb = new Recipe.RecipeBuilder();
        rb.setName("Strawberry Pavlova");
        List<String> l = new ArrayList();
        l.add("whip it good");
        rb.setDirections(l);
        rb.setServings(1);
        Recipe okRecipe = rb.build();


        boolean success = true;

        try {
            RecipeValidator.validateServings(okRecipe);
        } catch (InvalidRecipeException e) {
            success = false;
        }
        assertTrue("Form was incorrectly validated", success);
    }


    @Test
    public void testRecipeValidateServings_InvalidServings() {
        System.out.println("Testing validaDirection() with invalid servings");

        Recipe.RecipeBuilder rb = new Recipe.RecipeBuilder();
        rb.setName("Strawberry Pavlova");
        List<String> l = new ArrayList();
        l.add("whip it good");
        rb.setDirections(l);
        rb.setServings(-1);
        Recipe badRecipe = rb.build();

        boolean success = true;

        try {
            RecipeValidator.validateServings(badRecipe);
        } catch (InvalidRecipeException e) {
            success = false;
        }

        assertFalse("Expected invalid one was valid", success);
    }


    @Test
    public void testRecipeValidatePrepTime_validTime() {
        System.out.println("Testing validaDirection() with valid time");

        Recipe.RecipeBuilder rb = new Recipe.RecipeBuilder();
        rb.setName("Strawberry Pavlova");
        List<String> l = new ArrayList();
        l.add("whip it good");
        rb.setDirections(l);
        rb.setServings(1);
        rb.setPrepTime(10);
        Recipe okRecipe = rb.build();

        boolean success = true;

        try {
            RecipeValidator.validatePreptime(okRecipe);
        } catch (InvalidRecipeException e) {
            success = false;
        }
        assertTrue("Form was incorrectly validated", success);
    }


    @Test
    public void testRecipeValidatePrepTime_InvalidTime() {
        System.out.println("Testing validPrepTime() with invalid time");

        Recipe.RecipeBuilder rb = new Recipe.RecipeBuilder();
        rb.setName("Brown Butter and Toffee Chocolate Chip Cookies");
        List<String> l = new ArrayList();
        l.add("whip it good");
        rb.setDirections(l);
        rb.setServings(1);
        rb.setPrepTime(0);
        Recipe badRecipe = rb.build();

        boolean success = true;

        try {
            RecipeValidator.validatePreptime(badRecipe);
        } catch (InvalidRecipeException e) {
            success = false;
        }
        assertFalse("Expected invalid one was valid", success);
    }


    @Test
    public void testRecipeValidateIngredients_validIngredient() {
        System.out.println("Testing validateIngredients() with valid ingredients");

        Recipe.RecipeBuilder rb = new Recipe.RecipeBuilder();
        rb.setName("Strawberry Pavlova");
        List<String> l = new ArrayList();
        l.add("whip it good");
        rb.setDirections(l);
        rb.setServings(1);
        rb.setPrepTime(10);
        rb.setRecipeId(0L);

        ArrayList<Ingredient> okIngredients = new ArrayList<>();
        okIngredients.add(new Ingredient(4, "", "egg white"));
        okIngredients.add(new Ingredient(4, "g", "baking soda"));
        okIngredients.add(new Ingredient(5, "oz", "dark brown sugar"));
        rb.setIngredients(okIngredients);
        Recipe okRecipe = rb.build();
        boolean success = true;

        try {
            RecipeValidator.validateIngredients(okRecipe);
        } catch (InvalidIngredientException e) {
            success = false;
        }
        assertTrue("Form was incorrectly validated", success);
    }


    @Test
    public void testRecipeValidateIngredients_InvalidIngredientsAmount() {
        System.out.println("Testing validateIngredients() with invalid ingredient amount");

        Recipe.RecipeBuilder rb = new Recipe.RecipeBuilder();
        rb.setName("Strawberry Pavlova");
        List<String> l = new ArrayList();
        l.add("whip it good");
        rb.setDirections(l);
        rb.setServings(1);
        rb.setPrepTime(10);
        rb.setRecipeId(0L);

        ArrayList<Ingredient> badIngredients = new ArrayList<>();
        badIngredients.add(new Ingredient(-10, "", "egg whiteasdfaf"));
        badIngredients.add(new Ingredient(-1, "g", "butter"));
        Recipe badRecipe = rb.build();
        boolean success = true;

        try {
            RecipeValidator.validateIngredients(badRecipe);
        } catch (InvalidIngredientException e) {
            success = false;
        }
        assertFalse("Expected invalid one was valid", success);
    }

    @Test
    public void testRecipeValidateIngredients_InvalidIngredientsSubstance() {
        System.out.println("Testing validateIngredients() with invalid ingredient substance");

        Recipe.RecipeBuilder rb = new Recipe.RecipeBuilder();
        rb.setName("Strawberry Pavlova");
        List<String> l = new ArrayList();
        l.add("whip it good");
        rb.setDirections(l);
        rb.setServings(1);
        rb.setPrepTime(10);
        rb.setRecipeId(0L);

        ArrayList<Ingredient> badIngredients = new ArrayList<>();
        badIngredients.add(new Ingredient(10, "", "egg whiteasdfaf"));
        badIngredients.add(new Ingredient(15, "g", ""));
        Recipe badRecipe = rb.build();
        boolean success = true;

        try {
            RecipeValidator.validateIngredients(badRecipe);
        } catch (InvalidIngredientException e) {
            success = false;
        }
        assertFalse("Expected invalid one was valid", success);
    }


    @Test
    public void testRecipeValidateIngredients_validAndInvalidIngredients() {
        System.out.println("Testing validateIngredients() with valid ingredient and invalid ingredient");

        Recipe.RecipeBuilder rb = new Recipe.RecipeBuilder();
        rb.setName("Strawberry Pavlova");
        List<String> l = new ArrayList();
        l.add("whip it good");
        rb.setDirections(l);
        rb.setServings(1);
        rb.setPrepTime(10);
        rb.setRecipeId(0L);

        ArrayList<Ingredient> badIngredients = new ArrayList<>();
        badIngredients.add(new Ingredient(5, "oz", "dark brown sugar"));
        badIngredients.add(new Ingredient(-1, "", ""));

        Recipe badRecipe = rb.build();

        boolean success = true;

        try {
            RecipeValidator.validateIngredients(badRecipe);
        } catch (InvalidIngredientException e) {
            success = false;
        }
        assertFalse("Expected invalid one was valid", success);
    }
}
