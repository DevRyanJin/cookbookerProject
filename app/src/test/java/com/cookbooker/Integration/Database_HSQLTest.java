package com.cookbooker.Integration;

import com.cookbooker.data.Database_HSQL;
import com.cookbooker.objects.Ingredient;
import com.cookbooker.objects.Picture;
import com.cookbooker.objects.Recipe;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class Database_HSQLTest {
    private static Database_HSQL db;
    private static String path = System.getProperty("user.dir") + "/src/main/assets/";
    private static String dir = "database/";
    private static String temp = "temp/";
    private static String name = "demodb";

    //copy database before tests so tests do not affect database
    @BeforeClass
    public static void runOnceBeforeClass() {

        try {
            Files.copy(Paths.get(path + dir), Paths.get(path + temp));
            File f = new File(path + dir);
            File[] f_list = f.listFiles();

            for (File s : f_list) {
                Files.copy(Paths.get(path + dir + s.getName()), Paths.get(path + temp + s.getName()));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        db = new Database_HSQL(path + temp + name);
    }

    //delete copy of database.
    @AfterClass
    public static void runOnceAfterClass() {
        try {
            db.stop();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            File f = new File(path + temp);
            File[] f_list = f.listFiles();
            for (File s : f_list) {
                try {
                    Files.delete(Paths.get(path + temp + s.getName()));
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            Files.delete(Paths.get(path + temp));

        } catch (Exception e) {
            System.out.println(e);
        }
    }


    @Test
    public void getRecipeList_isCorrect() {
        assertNotNull(db.getRecipeList(true));
    }

    @Test
    public void getRecipeByID_isCorrect() {
        ArrayList<Recipe> col = new ArrayList<>();
        Recipe check;
        col.addAll(db.getRecipeList(false));
        if (col.size() > 0) {
            for (Recipe r : col) {
                long id = r.getRecipeId();
                check = db.getRecipeByID(id, false);
                assertEquals((long) check.getRecipeId(), id);
            }
        }
    }

    @Test
    public void getRecipeByName_isCorrect() {
        assertTrue(db.getRecipeByName("straw", true).size() > 0);
    }

    @Test
    public void getRecipeByIngredient_isCorrect() {
        assertTrue(db.getRecipeByIngredient("straw", true).size() > 0);
    }

    @Test
    public void getRecipeByPreptime_isCorrect() {
        assertTrue(db.getRecipeByPreptime(180, 0, true).size() > 0);
    }

    @Test
    public void getRecipeByServings_isCorrect() {
        assertTrue(db.getRecipeByServings(20, 0, true).size() > 0);
    }

    @Test
    public void getRecipeByNameOrIngredient_isCorrect() {
        Collection<Recipe> col = db.getRecipeByNameOrIngredient("lemon", true);
        assertTrue(!col.isEmpty());
        col = db.getRecipeByNameOrIngredient("lemon && pie", true);
        assertTrue(!col.isEmpty());

    }

    @Test
    public void storeRecipe_isCorrect() {
        assertTrue(db.storeRecipe(this.bad_recipe()) > 0);
    }

    @Test
    public void editRecipe_isCorrect() {
        Recipe r = db.getRecipeByID(db.storeRecipe(this.bad_recipe()), false);
        //edit existing recipe
        assertTrue(db.editRecipe(r));
        //edit non-existing recipe
        assertFalse(db.editRecipe(this.bad_recipe()));
    }

    @Test
    public void recipeCount_isCorrect() {
        db.storeRecipe(this.bad_recipe());
        db.storeRecipe(this.bad_recipe());
        db.storeRecipe(this.bad_recipe());
        Collection<Recipe> col = db.getRecipeByName("feces", false);
        assertEquals(col.size(), 3);
    }

    @Test
    public void removeRecipe_isCorrect() {
        db.storeRecipe(this.bad_recipe());
        Collection<Recipe> col = db.getRecipeByName("feces", false);
        for (Recipe in : col) {
            db.removeRecipe(in.getRecipeId());
        }
        col = db.getRecipeByName("feces", false);
        assertEquals(col.size(), 0);
    }

    @Test
    public void removePicture_isCorrect() {
        long r_id = db.storeRecipe(this.bad_recipe());
        try {
            Picture p = this.bad_picture();
            Recipe r = db.getRecipeByID(r_id, false);

            long p_id = db.addToGallery(p, r).getId();

            db.removePicture(p_id);
            assertEquals(db.getGallery(r_id, false).size(), 0);
        } catch (Exception e) {
            assert (false);
        }
    }

    @Test
    public void getPictureByID_is_correct() {
        long r_id = db.storeRecipe(this.bad_recipe());
        try {
            Picture p = this.bad_picture();
            Recipe r = db.getRecipeByID(r_id, false);
            long p_id = db.addToGallery(p, r).getId();

            assertEquals(db.getPictureByID(p_id).getId(), p_id);
        } catch (Exception e) {
            assert (false);
        }
    }

    @Test
    public void addToGallery_is_correct() {
        long r_id = db.storeRecipe(this.bad_recipe());
        try {
            Picture p = this.bad_picture();
            Recipe r = db.getRecipeByID(r_id, false);

            assertTrue((db.addToGallery(p, r).getId()) > 0);
        } catch (Exception e) {
            assert (false);
        }
    }

    @Test
    public void getGallery_is_correct() {
        long r_id = db.storeRecipe(this.bad_recipe());
        try {
            Picture p = this.bad_picture();
            Recipe r = db.getRecipeByID(r_id, false);

            db.addToGallery(p, r).getId();
            assertTrue((db.getGallery(r_id, false).size()) > 0);
            assertTrue((db.getGallery(r_id, false).size()) == 1);
        } catch (Exception e) {
            assert (false);
        }
    }

    @Test
    public void removePicture_is_correct() {
        long r_id = db.storeRecipe(this.bad_recipe());
        try {
            Picture p = this.bad_picture();
            Recipe r = db.getRecipeByID(r_id, false);

            long p_id = db.addToGallery(p, r).getId();
            db.removePicture(p_id);
            assertTrue((db.getGallery(r_id, false).size()) == 0);
        } catch (Exception e) {
            assert (false);
        }
    }

    @Test
    public void recipecount_is_correct() {
        long l = db.getRecipeCount();
        db.storeRecipe(this.bad_recipe());
        assertTrue(db.getRecipeCount() > l);
    }
    //generates recipe with known data that can be used for testing
    //Is a recipe that is valid but unlikely to contain information
    //matching legitimate database entries
    private Recipe bad_recipe() {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient(500, "pinch", "feces"));
        Recipe.RecipeBuilder rBuilder = new Recipe.RecipeBuilder();
        rBuilder.setRecipeId(-1L);
        rBuilder.setName("Feces");
        rBuilder.setIngredients(ingredients);
        List<String> l = new ArrayList<>();
        l.add("Wipe");
        rBuilder.setDirections(l);
        rBuilder.setServings(0);
        rBuilder.setPrepTime(0);
        return rBuilder.build();
    }
    //generates picture with known data that can be used for testing
    //Is a picture that is valid but unlikely to contain information
    //matching legitimate database picture.
    private Picture bad_picture() {
        File imgPath = new File(System.getProperty("user.dir") + "/src/main/assets/images/bad.jpg");
        Picture p = null;
        try {
            FileInputStream fin = new FileInputStream(imgPath);
            byte[] data = new byte[fin.available()];
            fin.read(data);
            p = new Picture(-1L, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }

    //return database to default state
    @After
    public void remove_from_db() {
        //remove bad recipes
        Collection<Recipe> col = db.getRecipeByName("feces", false);
        for (Recipe in : col) {
            db.removeRecipe(in.getRecipeId());
        }
    }
}
