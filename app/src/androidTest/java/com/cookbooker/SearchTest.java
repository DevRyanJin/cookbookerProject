package com.cookbooker;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.KeyEvent;
import android.widget.EditText;

import com.cookbooker.presentation.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;


@RunWith(AndroidJUnit4.class)
public class SearchTest {
    @Rule
    /*
     * Set the starting activity to the Main Activity for each test
     */
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void searchForRecipe() {
        //Be cautious of your capitalization here, It's case-sensitive
        String recipeToSearchFor = "Lemon meringue pie";
        //Open the top menu/toolbar
        onView(withContentDescription("Open navigation drawer")).perform(click());
        //Open the search bar at the top
        onView(withText("Search")).perform(click());
        //Search has opened up and gained focus of the kbd
        onView(isAssignableFrom(EditText.class)).perform(typeText(recipeToSearchFor), pressKey(KeyEvent.KEYCODE_ENTER));
        //Kbd should automagially disappear
        //Click the first element
        onView(ViewMatchers.withId(R.id.recyclerViewRecipes)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        //Check that the recipe title is displayed and matches
        onView(withText(recipeToSearchFor)).check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.recipe_title)).check(matches(withText(containsString(recipeToSearchFor))));
        //Exit to the search results
        pressBack();
        //Exit to the main menu
        pressBack();
    }

    @Test
    public void searchForIngredient() {
        //Be cautious of your capitalization here, It's case-sensitive
        String ingredientToSearchFor = "vanilla";
        //Open the top menu/toolbar
        onView(withContentDescription("Open navigation drawer")).perform(click());
        //Open the search bar at the top
        onView(withText("Search")).perform(click());
        //Search has opened up and gained focus of the kbd
        onView(isAssignableFrom(EditText.class)).perform(typeText(ingredientToSearchFor), pressKey(KeyEvent.KEYCODE_ENTER));
        //Kbd should automagially disappear
        //Click the first element
        onView(ViewMatchers.withId(R.id.recyclerViewRecipes)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        //Check that a recipe ingredient matches
        onView(ViewMatchers.withId(R.id.ingredients_body)).check(matches(withText(containsString(ingredientToSearchFor))));
        //Exit to the search results
        pressBack();
        //Exit to the main menu
        pressBack();
    }
}