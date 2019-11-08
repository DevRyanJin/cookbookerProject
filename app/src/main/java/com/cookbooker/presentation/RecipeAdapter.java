package com.cookbooker.presentation;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cookbooker.R;
import com.cookbooker.objects.Picture;
import com.cookbooker.objects.Recipe;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Creation of this document was done by following a guide:
 * https://github.com/codepath/android_guides/wiki/Using-the-RecyclerView
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private ArrayList<Recipe> recipes;

    public RecipeAdapter(Collection<Recipe> recipes) {
        this.recipes = new ArrayList<>(recipes);
    }

    // Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.recipe_list_items_main, parent, false);

        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Recipe recipe = this.recipes.get(position);
        try {
            ArrayList<Picture> pics = new ArrayList<>(recipe.getPictures());
            //Use the first image as the default
            Picture bPic = pics.get(0);
            BitmapDrawable bgDr = new BitmapDrawable(null, BitmapFactory.decodeByteArray(bPic.getData(), 0, bPic.getData().length));
            viewHolder.recipeBackgound.setBackground(bgDr);

        } catch (Exception e) {
            Log.e("RecipeAdapter_BindVH", "Failed to load background image for " + recipe);
            Log.e("RecipeAdapter_BindVH", e.getMessage());
            e.printStackTrace();
            //Loading of file failed, revert to default colour scheme
            int[] colours = {Color.DKGRAY, Color.GRAY, Color.LTGRAY, Color.WHITE, Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA};
            viewHolder.recipeBackgound.setBackgroundColor(colours[(int) (recipe.getRecipeId() % 10)]);
        }

        viewHolder.nameTextView.setText(recipe.getName());
        viewHolder.recipeIdTextView.setText(recipe.getRecipeId().toString());
        viewHolder.recipeBackgound.setOnClickListener(clickListner);
    }

    @Override
    public int getItemCount() {
        return this.recipes.size();
    }

    //Start up a recipe_view when clicked
    private View.OnClickListener clickListner = new View.OnClickListener() {
        public void onClick(View backgroundView) {
            if (backgroundView.getId() == R.id.recipe_background) {
                long clickedRecipeID = 0L;//Default to zero
                View child;
                //Pull out the recipe_id (see recipe_list_items_main.xml)
                for (int itemPos = 0; itemPos < ((ViewGroup) backgroundView).getChildCount(); itemPos++) {
                    child = ((ViewGroup) backgroundView).getChildAt(itemPos);
                    if (child.getId() == R.id.recipe_id) {
                        clickedRecipeID = (Long.parseLong(((TextView) child).getText().toString()));
                        break;
                    }
                }
                Intent intent = new Intent(backgroundView.getContext(), RecipeActivity.class);
                //Following proper guidelines
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("cookbooker_" + R.string.recipe_id, clickedRecipeID);
                //Just grabbing the local context.. we'll see if this screws things up, may want to call on the main application context Activity.getApplicationContext();
                backgroundView.getContext().startActivity(intent);

            }
        }
    };

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Holder contains a member variable for any view that will be set as rows render
        private TextView nameTextView;
        private TextView recipeIdTextView;
        private LinearLayout recipeBackgound;

        // Constructor accepts the entire item row and does the view lookups to find each subview
        private ViewHolder(View itemView) {
            super(itemView);
            recipeBackgound = itemView.findViewById(R.id.recipe_background);
            nameTextView = itemView.findViewById(R.id.recipe_title);
            recipeIdTextView = itemView.findViewById(R.id.recipe_id);
        }
    }
}