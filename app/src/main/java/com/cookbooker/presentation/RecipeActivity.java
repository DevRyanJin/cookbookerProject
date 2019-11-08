package com.cookbooker.presentation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cookbooker.R;
import com.cookbooker.logic.Service;
import com.cookbooker.logic.exceptions.InvalidIngredientException;
import com.cookbooker.objects.Ingredient;
import com.cookbooker.objects.Picture;
import com.cookbooker.objects.Recipe;

import java.util.List;

public class RecipeActivity extends AppCompatActivity {
    private Long recipeID;

    public RecipeActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        try {
            Bundle bundle = getIntent().getExtras();
            this.recipeID = bundle.getLong("cookbooker_" + R.string.recipe_id);
        } catch (Exception e) {
            e.printStackTrace();
            Message.warning(this, "Failed to parse cookbooker_recipe_id from bundle");
            Log.e("RecipeActivity.OnCreate", e.getMessage());
            finish();
        }
        updateView();
    }

    public Dialog createAnnotationPopup(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(msg);
        return builder.create();
    }

    protected void makeLinkClickable(final SpannableStringBuilder strBuilder, final URLSpan span) {
        final int start = strBuilder.getSpanStart(span);
        final int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan() {
            public void onClick(View view) {
                createAnnotationPopup("" + strBuilder.subSequence(start, end), span.getURL()).show();
            }
        };
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span);
    }

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

    protected void updateView() {
        try {
            Recipe trecipe = null;
            try {
                trecipe = Service.getAccessRecipe().getRecipe(recipeID);
            } catch (RuntimeException e) {
                //We don't need to worry about warning the user as it'll just keep working back until it finds a value
                Message.warning(getParent(), e.getMessage());
                Log.e("TEST", "Do I even get here?");
                finish();
            }
            //JAVA LIKES TO COMPLAIN A BIT TOO MUCH.. so we must dupe
            final Recipe recipe = trecipe;

            TextView recipeTitle = findViewById(R.id.recipe_title);
            recipeTitle.setText(recipe.getName());

            TextView recipeInfo = findViewById(R.id.recipe_info);
            String strBuff = "\tServes " + recipe.getServings() + " people.\n";
            strBuff += "\tTakes " + recipe.getPrepTime() + "m to create.";
            recipeInfo.setText(strBuff);

            strBuff = "";

            for (Ingredient ingred : recipe.getIngredients()) {
                strBuff += (ingred.amount() + " " + ingred.unit() + "\t\t" + ingred.substance() + "\n");
            }
            ((TextView) findViewById(R.id.ingredients_body)).setText(strBuff);

            setTextViewHTML((TextView) findViewById(R.id.directions_body), recipe.getDirections());

            findViewById(R.id.edit_button).setOnClickListener(new View.OnClickListener() {

                //Temporary, does not save into database
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), EditRecipeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("edit_recipe_id", recipe.getRecipeId());
                    intent.putExtra("edit_recipe_type", "edit recipe");
                    v.getContext().startActivity(intent);
                }

            });

            findViewById(R.id.edit_copy_button).setOnClickListener(new View.OnClickListener() {

                //Temporary, does not save into database
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), EditRecipeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("edit_recipe_id", recipe.getRecipeId());
                    intent.putExtra("edit_recipe_type", "copy recipe");
                    v.getContext().startActivity(intent);
                }

            });

            LinearLayout layout = (LinearLayout) findViewById(R.id.image_carousel);
            for (Picture pic : recipe.getPictures()) {
                if (findViewById(Integer.parseInt(pic.getId() + "")) == null) {
                    ImageView imageView = new ImageView(this);
                    imageView.setId(Integer.parseInt(pic.getId() + ""));
                    imageView.setPadding(2, 2, 2, 2);

                    Bitmap bmap = BitmapFactory.decodeByteArray(pic.getData(), 0, pic.getData().length);
                    imageView.setImageBitmap(bmap);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    layout.addView(imageView);
                }
            }
        } catch (final InvalidIngredientException e) {
            Message.warning(getParent(), e.getMessage());
        }
    }

    public void onResume() {
        try {
            super.onResume();
            updateView();
        } catch (Exception e) {
            finish();
        }
    }
}