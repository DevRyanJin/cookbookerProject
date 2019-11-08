package com.cookbooker.data;

import com.cookbooker.objects.Ingredient;
import com.cookbooker.objects.Picture;
import com.cookbooker.objects.Recipe;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class Database_HSQL implements Database {

    private final String dbPath;

    public Database_HSQL(final String dbPath) {

        this.dbPath = dbPath;
    }

    public void stop() throws SQLException {
        final Connection c = connection();
        final PreparedStatement st = c.prepareStatement("SHUTDOWN");
        st.execute();
        st.close();

    }

    private Connection connection() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:file:" + dbPath + ";ifexists=true;sql.ignore_case=true", "SA", "");
    }

    private String parseDirections(List<String> dir) {
        String concate_instructions = "";
        for (String each : dir) {
            concate_instructions += each + "LINE SEPERATOR 420 .MP3";
        }
        //remove tailing "LINE SEPERATOR 420 .MP3"
        concate_instructions = concate_instructions.substring(0, concate_instructions.length() - "LINE SEPERATOR 420 .MP3".length());
        return concate_instructions;
    }

    private String parseIngredients(Collection<Ingredient> ing) {
        String ingredients = "";
        for (Ingredient each : ing) {
            ingredients += Float.toString(each.amount()) + "ENTIRE TEXT OF MOBYDICK in french" + each.unit() + "ENTIRE TEXT OF MOBYDICK in french" + each.substance() + "LINE SEPERATOR 420 .MP3";
        }
        //remove tailing "LINE SEPERATOR 420 .MP3"
        ingredients = ingredients.substring(0, ingredients.length() - "LINE SEPERATOR 420 .MP3".length());
        return ingredients;
    }

    private Picture pictureFromResultSet(final ResultSet rs) throws SQLException {
        long id = rs.getLong("picture_id");
        byte[] data = Base64.getDecoder().decode(rs.getString("picture_data"));
        return new Picture(id, data);
    }

    //build recipe object
    private Recipe fromResultSet(final ResultSet rs, boolean isPartial) throws SQLException {
        Recipe.RecipeBuilder resultRecipe = new Recipe.RecipeBuilder();
        resultRecipe.setRecipeId(rs.getLong("recipeID"));
        resultRecipe.setName(rs.getString("name"));
        resultRecipe.setPictures(getGallery(rs.getLong("recipeID"), isPartial));

        if (!isPartial) {
            //"LINE SEPERATOR 420 .MP3" is line separator as reserved char char(30) was buggering up the database. So an arbitrary phrase is used.
            final String[] instructionsText = rs.getString("instructions").split("LINE SEPERATOR 420 .MP3");
            List<String> instructions = new ArrayList<>(Arrays.asList(instructionsText));

            resultRecipe.setDirections(instructions);
            resultRecipe.setPrepTime(rs.getInt("preptime"));
            resultRecipe.setServings(rs.getInt("servings"));

            final String[] ingredientText = rs.getString("ingredients").split("LINE SEPERATOR 420 .MP3");
            List<Ingredient> ingredients = new ArrayList<>();
            for (String each : ingredientText) {
                //"ENTIRE TEXT OF MOBYDICK in french" is TAB separator as reserved char char(29) was buggering up the database. So an arbitrary phrase is used.
                String[] ingredientIndividual = each.split("ENTIRE TEXT OF MOBYDICK in french");
                ingredients.add(new Ingredient(Float.parseFloat(ingredientIndividual[0]), ingredientIndividual[1], ingredientIndividual[2]));
            }
            resultRecipe.setIngredients(ingredients);
        }
        return resultRecipe.build();
    }

    public Collection<Recipe> getRecipeList(boolean isPartial) {
        final Collection<Recipe> recipes = new ArrayList<>();
        try (final Connection c = connection()) {
            final PreparedStatement st = c.prepareStatement("SELECT * FROM recipe");

            final ResultSet rs = st.executeQuery();
            while (rs.next()) {
                final Recipe recipe = fromResultSet(rs, isPartial);
                recipes.add(recipe);
            }
            rs.close();
            st.close();

            return recipes;
        } catch (final SQLException e) {

            throw new RuntimeException(e);
        }

    }

    public Recipe getRecipeByID(long id, boolean isPartial) {
        try (final Connection c = connection()) {
            final Recipe recipe;
            final PreparedStatement st = c.prepareStatement("SELECT * FROM recipe WHERE RECIPEID=?");
            st.setLong(1, id);

            final ResultSet rs = st.executeQuery();

            rs.next();//get first
            recipe = fromResultSet(rs, isPartial);

            rs.close();
            st.close();

            return recipe;

        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Collection<Recipe> getRecipeByName(final String query, boolean isPartial) {

        try (final Connection c = connection()) {
            final PreparedStatement st = c.prepareStatement("SELECT * FROM recipe WHERE lower(NAME) like ?");

            st.setString(1, "%" + query.toLowerCase() + "%");

            final ResultSet rs = st.executeQuery();
            final Collection<Recipe> recipes = new ArrayList<>();

            while (rs.next()) {
                final Recipe recipe = fromResultSet(rs, isPartial);
                recipes.add(recipe);
            }

            rs.close();
            st.close();

            return recipes;
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Collection<Recipe> getRecipeByIngredient(final String query, boolean isPartial) {

        try (final Connection c = connection()) {
            final PreparedStatement st = c.prepareStatement("SELECT * FROM recipe WHERE lower(INGREDIENTs) like ?");

            st.setString(1, "%" + query.toLowerCase() + "%");

            final ResultSet rs = st.executeQuery();
            final Collection<Recipe> recipes = new ArrayList<>();

            while (rs.next()) {
                final Recipe recipe = fromResultSet(rs, isPartial);
                recipes.add(recipe);
            }

            rs.close();
            st.close();

            return recipes;
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Collection<Recipe> getRecipeByPreptime(final int target, final int range, boolean isPartial) {
        int lower = target - range;
        int upper = target + range;
        try (final Connection c = connection()) {
            final PreparedStatement st = c.prepareStatement("SELECT * FROM recipe WHERE preptime <= ? AND preptime >= ?");

            st.setInt(1, upper);
            st.setInt(2, lower);

            final ResultSet rs = st.executeQuery();
            final Collection<Recipe> recipes = new ArrayList<>();

            while (rs.next()) {
                final Recipe recipe = fromResultSet(rs, isPartial);
                recipes.add(recipe);
            }

            rs.close();
            st.close();

            return recipes;
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Collection<Recipe> getRecipeByServings(final int target, final int range, boolean isPartial) {
        int lower = target - range;
        int upper = target + range;
        try (final Connection c = connection()) {
            final PreparedStatement st = c.prepareStatement("SELECT * FROM recipe WHERE servings <= ? AND servings >= ?");

            st.setInt(1, upper);
            st.setInt(2, lower);

            final ResultSet rs = st.executeQuery();
            final Collection<Recipe> recipes = new ArrayList<>();

            while (rs.next()) {
                final Recipe recipe = fromResultSet(rs, isPartial);
                recipes.add(recipe);
            }

            rs.close();
            st.close();

            return recipes;
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Searches the database for 'query' in the Ingredients OR the Recipe
     * String may consist of multiple tokens deliminated by '&&'
     *
     * @param query String to search ingredient name OR recipe name by
     * @return Collection of recipes with name matching query or with ingredients matching query
     */
    public Collection<Recipe> getRecipeByNameOrIngredient(final String query, boolean isPartial) {
        String[] queries = null;
        if (query.contains("&&"))
            queries = query.split("&&");

        try (final Connection c = connection()) {

            final HashSet<Recipe> recipes = new HashSet<>();
            if (queries == null) {
                final PreparedStatement st = c.prepareStatement("SELECT * FROM recipe WHERE lower(NAME) like ? OR lower(ingredients) like ?");


                st.setString(1, "%" + query.toLowerCase() + "%");
                st.setString(2, "%" + query.toLowerCase() + "%");

                final ResultSet rs = st.executeQuery();


                while (rs.next()) {
                    final Recipe recipe = fromResultSet(rs, isPartial);
                    recipes.add(recipe);
                }

                rs.close();
                st.close();
            } else {
                for (String in : queries) {
                    recipes.addAll(this.getRecipeByNameOrIngredient(in, isPartial));
                }

            }

            return recipes;
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public long storeRecipe(Recipe recipe) {
        try (final Connection c = connection()) {
            final PreparedStatement st = c.prepareStatement("INSERT INTO recipe (name, instructions, preptime, servings, ingredients) VALUES(?, ?, ?, ?, ?)");
            st.setString(1, recipe.getName());

            st.setString(2, parseDirections(recipe.getDirections()));
            st.setInt(3, recipe.getPrepTime());
            st.setInt(4, recipe.getServings());

            st.setString(5, parseIngredients(recipe.getIngredients()));
            st.executeUpdate();


            final PreparedStatement st2 = c.prepareStatement("SELECT * FROM recipe WHERE name like ? AND instructions like ? AND preptime=? AND servings=? AND ingredients like ?", ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);

            st2.setString(1, "%" + recipe.getName() + "%");
            st2.setString(2, "%" + parseDirections(recipe.getDirections()) + "%");
            st2.setInt(3, recipe.getPrepTime());
            st2.setInt(4, recipe.getServings());
            st2.setString(5, "%" + parseIngredients(recipe.getIngredients()) + "%");


            final ResultSet rs = st2.executeQuery();

//            rs.next();//get first
            rs.last();
            Recipe r = fromResultSet(rs, false);

            if (recipe.getPictures() != null) {
                for (Picture p : recipe.getPictures()) {
                    this.addToGallery(p, r);
                }
                //verified recipe with pictures

            }
            final Recipe clone = clone_recipe(r);

            rs.close();
            st.close();
            st2.close();

            return clone.getRecipeId();
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private Recipe clone_recipe(Recipe r) {
        Recipe.RecipeBuilder resultRecipe = new Recipe.RecipeBuilder();
        resultRecipe.setRecipeId(r.getRecipeId());
        resultRecipe.setName(r.getName());
        resultRecipe.setPictures(getGallery(r.getRecipeId(), true));

        resultRecipe.setDirections(r.getDirections());
        resultRecipe.setPrepTime(r.getPrepTime());
        resultRecipe.setServings(r.getServings());

        resultRecipe.setIngredients(r.getIngredients());

        return resultRecipe.build();
    }

    /**
     * @param recipe Recipe object to be edited (Id is used to identify the recipe to overwrite)
     * @return FALSE if recipeID is not in database, TRUE is recipe is updated,
     * Throws error if update goes awry
     */
    public boolean editRecipe(Recipe recipe) {
        try {
            this.getRecipeByID(recipe.getRecipeId(), false);
            try (final Connection c = connection()) {

                final PreparedStatement st = c.prepareStatement("UPDATE recipe SET name=?, instructions=?, preptime=?, servings=?, ingredients=? WHERE recipeID = ?");
                st.setString(1, recipe.getName());

                st.setString(2, parseDirections(recipe.getDirections()));
                st.setInt(3, recipe.getPrepTime());
                st.setInt(4, recipe.getServings());

                st.setString(5, parseIngredients(recipe.getIngredients()));
                st.setLong(6, recipe.getRecipeId());
                st.executeUpdate();

                st.close();

                return true;
            } catch (final SQLException e) {
                throw new RuntimeException(e);
            }

        } catch (final RuntimeException e) {
            //do nothing recipe not found
            return false;
        }


    }

    public Picture getPictureByID(final long id) {
        try (final Connection c = connection()) {

            final PreparedStatement st = c.prepareStatement("SELECT * FROM picture WHERE picture_id=?");
            st.setLong(1, id);

            final ResultSet rs = st.executeQuery();

            rs.next();//get first
            final Picture picture = pictureFromResultSet(rs);


            rs.close();
            st.close();

            return picture;

        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Picture storePicture(long id, final Picture picture) {
        try (final Connection c = connection()) {

            final PreparedStatement st = c.prepareStatement("INSERT INTO picture (picture_id, picture_data) VALUES(?, ?)");
            st.setLong(1, id);
            st.setString(2, new String(Base64.getEncoder().encode(picture.getData())));
            st.executeUpdate();

            final PreparedStatement st2 = c.prepareStatement("SELECT * FROM picture WHERE picture_id = ?");
            st2.setLong(1, id);

            final ResultSet rs = st2.executeQuery();

            rs.next();//get first
            Picture p = pictureFromResultSet(rs);

            rs.close();
            st.close();
            st2.close();

            return p;
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Picture addToGallery(final Picture p, final Recipe r) {
        try (final Connection c = connection()) {

            final PreparedStatement st = c.prepareStatement("INSERT INTO gallery (recipeid) VALUES(?)");
            st.setLong(1, r.getRecipeId());

            st.executeUpdate();


            final PreparedStatement st2 = c.prepareStatement("SELECT picture_id FROM gallery g WHERE recipeid = ? AND g.picture_id NOT IN ( SELECT picture_id FROM picture p WHERE g.picture_id = p.picture_id )", ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);

            st2.setLong(1, r.getRecipeId());

            final ResultSet rs = st2.executeQuery();

            long p_id = 0;
            if (rs.first()) {
                p_id = rs.getLong("picture_id");
            }
            Picture test = this.storePicture(p_id, p);

            rs.close();
            st.close();
            st2.close();

            return test;
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Collection<Picture> getGallery(final long r_id, boolean isPartial) {
        try (final Connection c = connection()) {
            Collection<Picture> gallery = new ArrayList<>();

            final PreparedStatement st = c.prepareStatement("SELECT picture.picture_id, picture.picture_data FROM picture INNER JOIN gallery ON picture.picture_id = gallery.picture_id WHERE recipeid = ?");
            st.setLong(1, r_id);

            final ResultSet rs = st.executeQuery();
            if (isPartial) {
                if (rs.next())
                    gallery.add(pictureFromResultSet(rs));
            } else {
                while (rs.next()) {
                    gallery.add(pictureFromResultSet(rs));
                }
            }
            rs.close();
            st.close();

            return gallery;
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeRecipe(long id) {
        try (final Connection c = connection()) {
            final PreparedStatement st = c.prepareStatement("DELETE FROM recipe WHERE RECIPEID=?");
            st.setLong(1, id);

            st.executeUpdate();
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removePicture(long id) {
        try (final Connection c = connection()) {
            final PreparedStatement st = c.prepareStatement("DELETE FROM gallery WHERE picture_id=?");
            st.setLong(1, id);

            st.executeUpdate();
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public long getRecipeCount() {
        try (final Connection c = connection()) {
            final PreparedStatement st = c.prepareStatement("SELECT * FROM recipe", ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);

            final ResultSet rs = st.executeQuery();

            long result = 0L;
            if (rs.next()) {
                rs.last();
                result = rs.getRow();
            }

            rs.close();
            st.close();

            return result;
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
