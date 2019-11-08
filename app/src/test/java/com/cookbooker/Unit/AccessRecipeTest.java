package com.cookbooker.Unit;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.assertEquals;

import com.cookbooker.data.Database;
import com.cookbooker.data.Database_HSQL;
import com.cookbooker.logic.AccessRecipe;
import com.cookbooker.objects.Ingredient;
import com.cookbooker.objects.Picture;
import com.cookbooker.objects.Recipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;


public class AccessRecipeTest {

    private Database tempDB;
    private AccessRecipe accessRecipe;
    private Recipe testRecipe1;
    private Recipe testRecipe2;
    private Recipe testRecipe3;
    private Recipe testRecipe4;

    @Before
    public void setUp() {
        System.out.println("Starting test for AccessRecipe");
        tempDB = mock(Database_HSQL.class);
        this.accessRecipe = new AccessRecipe(tempDB);

        Recipe.RecipeBuilder tempRecipe1 = new Recipe.RecipeBuilder();
        Recipe.RecipeBuilder tempRecipe2 = new Recipe.RecipeBuilder();
        Recipe.RecipeBuilder tempRecipe3 = new Recipe.RecipeBuilder();
        Recipe.RecipeBuilder tempRecipe4 = new Recipe.RecipeBuilder();

        List<String> direction1 = new ArrayList<>();
        List<String> direction2 = new ArrayList<>();
        List<String> direction3 = new ArrayList<>();
        List<String> direction4 = new ArrayList<>();

        List<Ingredient> ingredient1 = new ArrayList<>();
        List<Ingredient> ingredient2 = new ArrayList<>();
        List<Ingredient> ingredient3 = new ArrayList<>();
        List<Ingredient> ingredient4 = new ArrayList<>();

        List<Picture> pictures1 = new ArrayList<>();
        List<Picture> pictures2 = new ArrayList<>();
        List<Picture> pictures3 = new ArrayList<>();
        List<Picture> pictures4 = new ArrayList<>();


        direction1.add("Make a pie"); //Lemon meringue pie
        direction2.add("Bake Cookies");//Brown Butter and Toffee Chocolate Chip Cookies
        direction3.add("whip is good"); //Strawberry Pavlova
        direction4.add("Make a tomato spaghetti");

        ingredient1.add(new Ingredient(225, "g", "plain flour"));
        ingredient1.add(new Ingredient(175, "g", "butter"));
        ingredient1.add(new Ingredient(45, "g", "icing sugar"));
        ingredient1.add(new Ingredient(1, "", "large egg"));
        ingredient1.add(new Ingredient(6, "", "lemon's zest"));
        ingredient1.add(new Ingredient(6, "", "lemon's juice"));

        ingredient2.add(new Ingredient(1, "cup", "unsalted butter"));
        ingredient2.add(new Ingredient(2, "cup", "all-purpose flour"));
        ingredient2.add(new Ingredient(1, "tsp", "baking soda"));
        ingredient2.add(new Ingredient((float) 0.75, "tsp", "kosher salt"));
        ingredient2.add(new Ingredient(1, "cup", "dark brown sugar"));
        ingredient2.add(new Ingredient((float) 0.33, "cup", "granulated sugar"));

        ingredient3.add(new Ingredient(500, "g", "strawberries"));
        ingredient3.add(new Ingredient(200, "g", "redcurrants"));
        ingredient3.add(new Ingredient(3, "tbsp", "icing sugar"));
        ingredient3.add(new Ingredient(350, "g", "double cream"));

        ingredient4.add(new Ingredient(100, "g", "onions"));
        ingredient4.add(new Ingredient(2, "cup", "tomato sauce"));
        ingredient4.add(new Ingredient(1, "", "bell pepper"));
        ingredient4.add(new Ingredient(1, "tsp", "sugar"));
        ingredient4.add(new Ingredient(200, "g", "uncooked spaghetti"));

        pictures1.add(new Picture(1, new byte[16]));
        pictures1.add(new Picture(2, new byte[16]));
        pictures1.add(new Picture(3, new byte[16]));
        pictures1.add(new Picture(4, new byte[16]));

        pictures2.add(new Picture(5, new byte[16]));
        pictures2.add(new Picture(6, new byte[16]));
        pictures2.add(new Picture(7, new byte[16]));
        pictures2.add(new Picture(8, new byte[16]));

        pictures3.add(new Picture(9, new byte[16]));
        pictures3.add(new Picture(10, new byte[16]));
        pictures3.add(new Picture(11, new byte[16]));
        pictures3.add(new Picture(12, new byte[16]));

        pictures4.add(new Picture(13, new byte[16]));
        pictures4.add(new Picture(14, new byte[16]));
        pictures4.add(new Picture(15, new byte[16]));
        pictures4.add(new Picture(16, new byte[16]));

        tempRecipe1.setRecipeId(0L);
        tempRecipe1.setName("Lemon meringue pie");
        tempRecipe1.setDirections(direction1);
        tempRecipe1.setIngredients(ingredient1);
        tempRecipe1.addPicture(new Picture(1, new byte[1]));
        tempRecipe1.setServings(16);
        tempRecipe1.setPrepTime(60);
        tempRecipe1.setPictures(pictures1);

        tempRecipe2.setRecipeId(2L);
        tempRecipe2.setName("Brown Butter and Toffee Chocolate Chip Cookies");
        tempRecipe2.setDirections(direction2);
        tempRecipe2.setIngredients(ingredient2);
        tempRecipe2.setServings(15);
        tempRecipe2.setPrepTime(40);
        tempRecipe2.setPictures(pictures2);

        tempRecipe3.setRecipeId(3L);
        tempRecipe3.setName("Strawberry Pavlova");
        tempRecipe3.setDirections(direction3);
        tempRecipe3.setIngredients(ingredient3);
        tempRecipe3.setServings(20);
        tempRecipe3.setPrepTime(30);
        tempRecipe3.setPictures(pictures3);

        tempRecipe4.setRecipeId(4L);
        tempRecipe4.setName("Tomato Spaghetti");
        tempRecipe4.setDirections(direction4);
        tempRecipe4.setIngredients(ingredient4);
        tempRecipe4.setServings(4);
        tempRecipe4.setPrepTime(25);
        tempRecipe4.setPictures(pictures3);

        testRecipe1 = tempRecipe1.build();
        testRecipe2 = tempRecipe2.build();
        testRecipe3 = tempRecipe3.build();
        testRecipe4 = tempRecipe4.build();
    }

    @Test
    public void testGetAllRecipes() {
        System.out.println("Testing getAllRecipe method\n");

        Collection<Recipe> testRecipeList = new ArrayList<>();

        testRecipeList.add(testRecipe1);
        testRecipeList.add(testRecipe2);
        testRecipeList.add(testRecipe3);
        testRecipeList.add(testRecipe4);

        when(tempDB.getRecipeList(true)).thenReturn(testRecipeList);
        Collection<Recipe> actual = accessRecipe.getAllRecipes();

        assertNotNull(actual);
        assertEquals(actual, testRecipeList);
    }


    @Test
    public void testAddRecipe() {
        System.out.println("Testing addRecipe method\n");

        accessRecipe.addRecipe(testRecipe1);
        verify(tempDB).storeRecipe(testRecipe1);
    }

    @Test
    public void testSearchForRecipes_byRecipe() {
        System.out.println("Testing searchForRecipe method (by Recipe)\n");

        Collection<Recipe> recipeList = new ArrayList<>();
        recipeList.add(testRecipe4);


        ArrayList<String> queries = new ArrayList<>();
        queries.add(testRecipe4.getName());

        when(tempDB.storeRecipe(testRecipe4)).thenReturn(testRecipe4.getRecipeId());
        when(tempDB.getRecipeByName(testRecipe4.getName(), true)).thenReturn(recipeList);

        accessRecipe.addRecipe(testRecipe4);
        Collection<Recipe> actual = accessRecipe.searchForRecipes(queries);


        assertTrue(actual.contains(testRecipe4));

    }
    @Test
    public void testSearchForRecipes_byIngredient() {
        System.out.println("Testing searchForRecipe method (by Ingredient)\n");

        Collection<Recipe> recipeList = new ArrayList<>();
        recipeList.add(testRecipe1);

        Ingredient testIngredient =testRecipe1.getIngredients().iterator().next(); //plain flour

        ArrayList<String> queries = new ArrayList<>();
        queries.add(testIngredient.substance());

        when(tempDB.storeRecipe(testRecipe1)).thenReturn(testRecipe1.getRecipeId());
        when(tempDB.getRecipeByIngredient(testIngredient.substance(), true)).thenReturn(recipeList);
        when(tempDB.getRecipeByName(testIngredient.substance(), true)).thenReturn(recipeList);

        accessRecipe.addRecipe(testRecipe1);
        Collection<Recipe> actual = accessRecipe.searchForRecipes(queries);

        assertTrue(actual.contains(testRecipe1));

    }

    @Test
    public void testGetRecipe() {
        System.out.println("Testing getRecipe method\n");

        when(tempDB.getRecipeByID(testRecipe2.getRecipeId(), false)).thenReturn(testRecipe2);
        when(tempDB.storeRecipe(testRecipe2)).thenReturn(testRecipe2.getRecipeId());

        accessRecipe.addRecipe(testRecipe2);
        Recipe actual = accessRecipe.getRecipe(testRecipe2.getRecipeId());

        assertEquals(actual, testRecipe2);
    }

    @Test
    public void testAddPictureToRecipe() {
        System.out.println("Testing addPictureToRecipe method\n");

        Picture p = new Picture(1);
        when(tempDB.addToGallery(p, testRecipe3)).thenReturn(p);

        assertEquals(accessRecipe.addPictureToRecipe(p, testRecipe3), p);
    }

    @Test
    public void testGenerateDailyRecipe() {
        System.out.println("Testing generateDailyRecipe method\n");

        List<Recipe> testRecipes = new ArrayList<>();
        testRecipes.add(testRecipe1);
        testRecipes.add(testRecipe2);
        testRecipes.add(testRecipe3);
        testRecipes.add(testRecipe4);
        Random randomo = mock(Random.class);

        when(randomo.nextInt(anyInt())).thenReturn(0);
        assertEquals(accessRecipe.generateDailyRecipe(randomo, testRecipes), testRecipe1);

        when(randomo.nextInt(anyInt())).thenReturn(1);
        assertEquals(accessRecipe.generateDailyRecipe(randomo, testRecipes), testRecipe2);

        when(randomo.nextInt(anyInt())).thenReturn(2);
        assertEquals(accessRecipe.generateDailyRecipe(randomo, testRecipes), testRecipe3);

        when(randomo.nextInt(anyInt())).thenReturn(3);
        assertEquals(accessRecipe.generateDailyRecipe(randomo, testRecipes), testRecipe4);

    }
}