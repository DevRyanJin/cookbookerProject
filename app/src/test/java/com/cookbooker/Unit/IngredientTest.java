package com.cookbooker.Unit;


import org.junit.Test;

import static org.junit.Assert.*;

import com.cookbooker.data.*;
import com.cookbooker.objects.*;

public class IngredientTest {

    @Test
    public void testIngredient1() {

        Ingredient ingredient;

        System.out.println("\nStarting testIngredient");

        ingredient = new Ingredient(125, "g", "onion");
        assertNotNull(ingredient);
        assertEquals(125, ingredient.amount(), .001);
        assertEquals("g", ingredient.unit());
        assertEquals("onion", ingredient.substance());

        System.out.println("Finished testIngredient\n");

    }
}