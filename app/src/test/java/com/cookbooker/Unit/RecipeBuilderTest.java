package com.cookbooker.Unit;

import org.junit.Test;

import static org.junit.Assert.*;

import com.cookbooker.objects.*;

import java.util.ArrayList;
import java.util.List;

public class RecipeBuilderTest {

    @Test
    public void testRecipe1() {

        System.out.println("\nStarting testRecipe");

        Recipe.RecipeBuilder rb = new Recipe.RecipeBuilder();
        rb.setName("pie");
        List<String> l = new ArrayList();
        l.add("make a pie");
        rb.setDirections(l);
        rb.setPrepTime(30);
        rb.setServings(5);
        rb.setRecipeId(1L);

        ArrayList<Ingredient> ingredients = new ArrayList<>();

        ingredients.add(new Ingredient(12, "g", "flour"));
        ingredients.add(new Ingredient(455, "g", "black sugar"));
        ingredients.add(new Ingredient(20, "g", "salt"));
        ingredients.add(new Ingredient(10, "g", "egg white"));

        rb.setIngredients(ingredients);

        Recipe recipe = rb.build();


        assertNotNull(recipe);
        assertNotNull(ingredients);
        assertEquals("pie", recipe.getName());
        assertEquals("make a pie", recipe.getDirections().get(0));
        assertEquals(5, recipe.getServings());
        assertEquals(30, recipe.getPrepTime());
        assertEquals(4, ingredients.size());

        System.out.println("Finished testRecipe");
    }
}