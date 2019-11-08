package com.cookbooker.presentation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cookbooker.R;
import com.cookbooker.logic.AccessRecipe;
import com.cookbooker.logic.Service;
import com.cookbooker.logic.exceptions.AccessRecipeException;
import com.cookbooker.logic.exceptions.InvalidRecipeException;
import com.cookbooker.objects.Ingredient;
import com.cookbooker.objects.Recipe;

import java.util.ArrayList;
import java.util.List;

public class EditRecipeActivity extends AppCompatActivity {

    Recipe recipe;
    Button saveButton;
    Button addIngredientButton;
    Button addDirectionButton;
    Button deleteButton;
    EditText recipeTitle;
    EditText servingsText;
    EditText prepTimeText;
    EditText directionsBody;
    private LinearLayout ingredientsContent;
    private LinearLayout directionsContent;
    private String intentType;

    public EditRecipeActivity() {
        intentType = "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_recipe);
        long recipeID = -1L;
        Bundle bundle = getIntent().getExtras();
        try {
            intentType = bundle.getString("edit_recipe_type");
            recipeID = bundle.getLong("edit_recipe_id");
        } catch (Exception e) {
            e.printStackTrace();
            Message.fatalError(this, "Failed to parse edit_recipe from bundle" + e.getMessage());
            finish();
        }

        viewNewRecipe(recipeID);

        initButton();
        initIngredientLayout();
        this.recipeTitle = findViewById(R.id.recipe_title);
        this.servingsText = findViewById(R.id.servings_value);
        this.prepTimeText = findViewById(R.id.preptime_value);

        switch (this.intentType) {
            // Copy OR Edit has same functionality for display
            case "copy recipe":
            case "edit recipe":
                recipeTitle.setText(recipe.getName());
                servingsText.setText(String.valueOf(recipe.getServings()));
                prepTimeText.setText(String.valueOf(recipe.getPrepTime()));
                for (Ingredient ingred : recipe.getIngredients()) {
                    addIngredientLayout(ingred.amount(), ingred.substance(), ingred.unit());
                }

                for (String direction : recipe.getDirections()) {
                    addDirectionLayout(direction);
                }
                break;
            case "save recipe":
                recipeTitle.setText(R.string.new_recipe_label);
                servingsText.setText(String.valueOf(0));
                prepTimeText.setText(String.valueOf(0));
                addIngredientLayout(0f, "", "");
                addDirectionLayout("");
                break;

            default:
                finish();
                break;
        }

    }

    private void viewNewRecipe(long id) {
        this.recipe = Service.getAccessRecipe().getRecipe(id);
    }

    private void initButton() {
        addIngredientButton = findViewById(R.id.add_ingredient_button);
        addDirectionButton = findViewById(R.id.add_direction_button);
        saveButton = findViewById(R.id.save_button);
        deleteButton = findViewById(R.id.delete_recipe_button);
        switch (intentType) {
            case "edit recipe":
                deleteButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //We can assume that the delete button only exists with intenttype= "edit recipe"

                        Toast toast = Toast.makeText(getApplicationContext(), "Recipe: \'" + recipe.getName() + "\' was not deleted!", Toast.LENGTH_SHORT);
                        try {
                            Service.getAccessRecipe().deleteRecipe(recipe);
                            toast = Toast.makeText(getApplicationContext(), "Recipe: \'" + recipe.getName() + "\' was deleted!", Toast.LENGTH_SHORT);
                        } catch(AccessRecipeException e) {
                            System.err.println(e.getMessage());
                            e.printStackTrace();
                        }
                        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();
                    }
                });
                break;
            default:
                //Assumes our edit layout has a delete
                ViewGroup layout = (ViewGroup) deleteButton.getParent();
                layout.removeView(deleteButton);
                deleteButton = null;
                break;
        }
        saveButton.setOnClickListener(new View.OnClickListener() {

            //Temporary, does not save into database
            @Override
            public void onClick(View v) {

                EditText amountField;
                Spinner spinner;
                EditText descriptionField;
                Toast toast = null;
                Recipe.RecipeBuilder builder = new Recipe.RecipeBuilder();

                Ingredient ingredients;
                ArrayList<Ingredient> ingredientsList = new ArrayList<>();
                for (int i = 0; i < ingredientsContent.getChildCount() - 1; i++) {
                    amountField = ingredientsContent.getChildAt(i).findViewById(R.id.amount_edit_label);
                    spinner = ingredientsContent.getChildAt(i).findViewById(R.id.metrics_spinner);
                    descriptionField = ingredientsContent.getChildAt(i).findViewById(R.id.ingredients_edit_label);
                    ingredients = new Ingredient(Float.parseFloat(amountField.getText().toString()), spinner.getSelectedItem().toString(), descriptionField.getText().toString());
                    ingredientsList.add(ingredients);
                }

                List<String> directions = new ArrayList<>();
                for (int i = 0; i < directionsContent.getChildCount() - 1; i++) {
                    descriptionField = directionsContent.getChildAt(i).findViewById(R.id.description_edit_label);
                    directions.add(descriptionField.getText().toString());
                }

                try {
                    builder.setName(recipeTitle.getText().toString());
                    builder.setIngredients(ingredientsList);
                    builder.setDirections(directions);
                    builder.setServings(Integer.valueOf(servingsText.getText().toString()));
                    builder.setPrepTime(Integer.valueOf(prepTimeText.getText().toString()));

                    switch (intentType) {
                        case "edit recipe":
                            builder.setPictures(recipe.getPictures());
                            builder.setRecipeId(recipe.getRecipeId());
                            try {
                                Service.getAccessRecipe().editRecipe(builder.build());
                                toast = Toast.makeText(getApplicationContext(), "Recipe: \'" + recipe.getName() + "\' has been saved!", Toast.LENGTH_SHORT);
                            } catch (AccessRecipeException e) {
                                //Attempted to edit a recipe that no longer exists.
                                Log.e("Presentation.EditRecipe", e.getMessage());
                                //Add it as a new recipe for the user, if it no longer exists
                                try {
                                    Service.getAccessRecipe().addRecipe(builder.build());
                                    toast = Toast.makeText(getApplicationContext(), "A new copy of \'" + recipe.getName() + "\' has been saved!", Toast.LENGTH_SHORT);
                                    finish();
                                } catch (InvalidRecipeException ee) {
                                    Message.warning(EditRecipeActivity.this, ee.getMessage());
                                    toast = Toast.makeText(getApplicationContext(), "Recipe \'" + recipe.getName() + "\' has not been saved!", Toast.LENGTH_SHORT);
                                } catch (RuntimeException eee) {
                                    Log.e("", eee.getMessage());
                                    eee.printStackTrace();
                                    toast = Toast.makeText(getApplicationContext(), "Recipe \'" + recipe.getName() + "\' has not been saved!", Toast.LENGTH_SHORT);
                                }
                            }
                            break;
                        case "save recipe":
                            //by default, recipe builder sets picture collection to an empty list
                            recipe = builder.build();
                            Service.getAccessRecipe().addRecipe(recipe);

                            toast = Toast.makeText(getApplicationContext(), "Recipe \'" + recipe.getName() + "\' has been saved!", Toast.LENGTH_SHORT);
                            break;
                        case "copy recipe":
                            builder.setPictures(recipe.getPictures());
                            recipe = builder.build();
                            Service.getAccessRecipe().addRecipe(recipe);
                            toast = Toast.makeText(getApplicationContext(), "Recipe: \'" + recipe.getName() + "\' has been saved!", Toast.LENGTH_SHORT);
                            finish();
                    }


                } catch (final InvalidRecipeException e) {
                    toast = Toast.makeText(getApplicationContext(), "Recipe: \'" + recipe.getName() + "\' was not saved!", Toast.LENGTH_SHORT);

                    Message.warning(EditRecipeActivity.this, e.getMessage());
                    Log.e("cookbooker.presentation", e.getMessage());
                    e.printStackTrace();
                } catch (final RuntimeException e) {
                    toast = Toast.makeText(getApplicationContext(), "Recipe: \'" + recipe.getName() + "\' was not saved!", Toast.LENGTH_SHORT);
                    //Note: try block can also throw an intenttype exception
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                }
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }

        });

        addIngredientButton.setOnClickListener(new View.OnClickListener() {

            //Temporary, does not save into database
            @Override
            public void onClick(View v) {
                addIngredientLayout(0f, "", "");
            }

        });

        addDirectionButton.setOnClickListener(new View.OnClickListener() {

            //Temporary, does not save into database
            @Override
            public void onClick(View v) {
                addDirectionLayout("");
            }

        });
    }

    private void initIngredientLayout() {
        // get ahold of the instance of your layout
        this.ingredientsContent = findViewById(R.id.ingredients_edit_layout);
        this.directionsContent = findViewById(R.id.directions_edit_layout);
    }

    /**
     * @param amount      Volume of ingredient
     * @param description Name of ingredient
     * @param measurement Unit of measurement
     */
    private void addIngredientLayout(float amount, String description, String measurement) {

        LayoutInflater inflater = getLayoutInflater();
        final View rowView = inflater.inflate(R.layout.ingredients_edit_format, ingredientsContent, false);
        EditText amountField = rowView.findViewById(R.id.amount_edit_label);
        Spinner spinner = rowView.findViewById(R.id.metrics_spinner);
        EditText descriptionField = rowView.findViewById(R.id.ingredients_edit_label);
        Button deleteButton = rowView.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {

            //Temporary, does not save into database
            @Override
            public void onClick(View v) {
                ingredientsContent.removeView((View) v.getParent());
            }

        });

        amountField.setText(amount + "");
        setSpinText(spinner, measurement);
        descriptionField.setText(description);

        // Add the new row before the add field button.
        ingredientsContent.addView(rowView, ingredientsContent.getChildCount() - 1);
    }

    private void addDirectionLayout(String description) {
        LayoutInflater inflater = getLayoutInflater();
        final View rowView = inflater.inflate(R.layout.description_edit_format, directionsContent, false);
        EditText descriptionField = rowView.findViewById(R.id.description_edit_label);
        Button deleteButton = rowView.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {

            //Temporary, does not save into database
            @Override
            public void onClick(View v) {
                directionsContent.removeView((View) v.getParent());
            }

        });
        descriptionField.setText(description);

        // Add the new row before the add field button.
        directionsContent.addView(rowView, directionsContent.getChildCount() - 1);
    }

    public Dialog createAnnotationPopup(String title, String msg) {
        EditText text = new EditText(this);
        text.setText(msg);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setView(text);
        return builder.create();
    }

    private void makeLinkClickable(final SpannableStringBuilder strBuilder, final URLSpan span) {
        final int start = strBuilder.getSpanStart(span);
        final int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        URLSpan clickable = new URLSpan(span.getURL()) {
            public void onClick(View view) {
                createAnnotationPopup("" + strBuilder.subSequence(start, end), span.getURL()).show();
            }
        };
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span); //don't do this if you want html to be recoverable
    }

    //only supports /n escape character
    protected void setTextViewHTML(TextView text, List<String> list) {
        String strBuff = "";
        for (String dir : list) {
            strBuff += dir + "<br />";
        }
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(Html.fromHtml(strBuff));
        for (URLSpan span : strBuilder.getSpans(0, strBuff.length(), URLSpan.class)) {
            makeLinkClickable(strBuilder, span);
        }
        text.setText(strBuilder);
        text.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setSpinText(Spinner spin, String text) {
        spin.setSelection(0);
        for (int i = 0; i < spin.getAdapter().getCount(); i++) {
            if (spin.getAdapter().getItem(i).toString().equals(text)) {
                spin.setSelection(i);
            }
        }

    }
}
