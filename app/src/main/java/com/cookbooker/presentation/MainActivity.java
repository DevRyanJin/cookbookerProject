package com.cookbooker.presentation;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.cookbooker.R;
import com.cookbooker.logic.Service;
import com.cookbooker.objects.Recipe;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Service.databaseExists())
            copyDatabaseToDevice();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setupRecipeAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        setupRecipeAdapter();
    }

    private void setupRecipeAdapter() {
        ArrayList<String> queries = new ArrayList<>();
        Intent intent = getIntent();
        if (intent.hasExtra("cookbooker_" + R.string.shopping_list_query))
            queries.addAll(intent.getExtras().getStringArrayList("cookbooker_" + R.string.shopping_list_query));
        if (Intent.ACTION_SEARCH.equals(intent.getAction()))
            queries.add(intent.getStringExtra(SearchManager.QUERY));

        RecyclerView viewRecipes = findViewById(R.id.recyclerViewRecipes);
        Collection<Recipe> recipes = Service.getAccessRecipe().searchForRecipes(queries);
        if (recipes.size() == 0)
            Message.info(this, "No recipes to show");
        viewRecipes.setAdapter(new RecipeAdapter(recipes));
        viewRecipes.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    //NonNull in params tells the compiler that the arg must not be null
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_recipe_of_the_day) {
            Intent intent = new Intent(this, RecipeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("cookbooker_" + R.string.recipe_id, Service.getAccessRecipe().generateDailyRecipe(new Random(), new ArrayList<>(Service.getAccessRecipe().getAllRecipes())).getRecipeId());
            startActivity(intent);
        } else if (id == R.id.nav_search) {
            onSearchRequested();
        } else if (id == R.id.nav_shopping_list) {
            startActivity(new Intent(this, ShoppingListActivity.class));

        } else if (id == R.id.nav_setting) {
            Message.info(this, "Currently there are no settings to change.");

        } else if (id == R.id.nav_new_recipe) {
            Intent intent = new Intent(this, EditRecipeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("edit_recipe_type", "save recipe");
            startActivity(intent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Copied from Franklin's sample project to get the database working
     */
    private void copyDatabaseToDevice() {
        final String DB_PATH = "database";

        Context context = getApplicationContext();
        File dataDirectory = context.getDir(DB_PATH, Context.MODE_PRIVATE);
        AssetManager assetManager = getAssets();

        try {

            //Pull out the HSQL Lock file if it exists
            ArrayList<String> arr = new ArrayList<>();
            for (String asset : assetManager.list(DB_PATH))
                if (!arr.contains(".lck"))
                    arr.add(asset);

            String[] assetNames = new String[arr.size()];
            int i = 0;
            for (String name : arr)
                assetNames[i++] = name;

            for (i = 0; i < assetNames.length; i++) {
                assetNames[i] = DB_PATH + "/" + assetNames[i];
            }


            copyAssetsToDirectory(assetNames, dataDirectory);

            Service.setDBPathName(dataDirectory.toString() + "/" + Service.getDBPathName());

        } catch (final IOException ioe) {
            Message.warning(this, "Unable to access application data: Cannot Copy Database Files to Device.");
            Log.e("MainActiv.CopyDBToDev", ioe.getMessage());
        }
    }

    /**
     * Copied from Franklin's sample project to get the database working
     *
     * @param assets    List of assets to copy to directory
     * @param directory Destination directory for assets to get copied to.
     * @throws IOException Throws when unable to open the requested files
     */
    public void copyAssetsToDirectory(String[] assets, File directory) throws IOException {
        AssetManager assetManager = getAssets();

        for (String asset : assets) {
            String[] components = asset.split("/");
            String copyPath = directory.toString() + "/" + components[components.length - 1];

            char[] buffer = new char[1024];
            int count;

            File outFile = new File(copyPath);

            if (!outFile.exists()) {
                InputStreamReader in = new InputStreamReader(assetManager.open(asset));
                FileWriter out = new FileWriter(outFile);

                count = in.read(buffer);
                while (count != -1) {
                    out.write(buffer, 0, count);
                    count = in.read(buffer);
                }

                out.close();
                in.close();
            }
        }
    }
}