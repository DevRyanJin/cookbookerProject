package com.cookbooker;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.view.KeyEvent;
import android.widget.EditText;

import com.cookbooker.presentation.MainActivity;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;

public class NewRecipeTest {
    @Rule
    /*
     * Set the starting activity to the Main Activity for each test
     */
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);
    private String rName = "IceCubes";

    @Test
    public void deleteIceCube() {
        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText("Search")).perform(click());
        //Search has opened up and gained focus of the kbd
        onView(isAssignableFrom(EditText.class)).perform(typeText(rName), pressKey(KeyEvent.KEYCODE_ENTER));
        //Kbd should automagially disappear
        //Click the first element
        onView(withId(R.id.recyclerViewRecipes)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        //Check that the recipe title is displayed and matches
        onView(withText(rName)).check(matches(isDisplayed()));
        onView(withId(R.id.recipe_title)).check(matches(withText(containsString(rName))));
        //Open the EditRecipeActivity
        onView(withText("Edit Recipe")).perform(click());
        onView(withText("Delete Recipe")).perform(click());

        //Here is where we're going in blind
        pressBack();
        pressBack();
        pressBack();
        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText("Search")).perform(click());
        //Search has opened up and gained focus of the kbd
        onView(isAssignableFrom(EditText.class)).perform(typeText(rName), pressKey(KeyEvent.KEYCODE_ENTER));
        //Our search should turn up empty!
        onView(withId(R.id.recyclerViewRecipes)).check(doesNotExist());
    }

    @Test
    public void makeIceCubesAnew() {
        int servings = 1;
        int preptime = 3;
        String[] rIngredientName = {"water", "Water"};
        String[] rDirection = {"Mix 4 cups of water with 2 tablespoons of water. Add more water to taste.", "Put in the freezer until frozen. Serve chilled."};
        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText("New Recipe")).perform(click());

        //Change the title
        onView(withId(R.id.recipe_title)).perform(clearText(), typeText(rName), pressKey(KeyEvent.KEYCODE_ENTER));
        closeSoftKeyboard();

        onView(allOf(withId(R.id.ingredients_edit_label), withText(""))).perform(clearText(), typeText(rIngredientName[0]));
        onView(allOf(withId(R.id.amount_edit_label), hasSibling(withText(rIngredientName[0])))).perform(clearText(), typeText("4"));
        onView(withId(R.id.add_ingredient_button)).perform(click());
        onView(allOf(withId(R.id.ingredients_edit_label), withText(""))).perform(typeText(rIngredientName[1]));
        onView(allOf(withId(R.id.amount_edit_label), hasSibling(withText(rIngredientName[1])))).perform(clearText(), typeText("2"));

        onView(allOf(withId(R.id.description_edit_label), withText(""))).perform(scrollTo(), typeText(rDirection[0]));
        closeSoftKeyboard();
        onView(withId(R.id.add_direction_button)).perform(click());
        onView(allOf(withId(R.id.description_edit_label), withText(""))).perform(scrollTo(), typeText(rDirection[1]));

        onView(withId(R.id.servings_value)).perform(scrollTo(), replaceText(servings + ""));
        onView(withId(R.id.preptime_value)).perform(scrollTo(), replaceText(preptime + ""));
        closeSoftKeyboard();
        onView(withId(R.id.save_button)).perform(scrollTo(), click());

        //Exit back to main Activity
        pressBack();
        //Need to search for new recipe
        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText("Search")).perform(click());
        //Search has opened up and gained focus of the kbd
        onView(isAssignableFrom(EditText.class)).perform(typeText(rName), pressKey(KeyEvent.KEYCODE_ENTER));
        //Kbd should automagially disappear
        //Click the first element
        onView(withId(R.id.recyclerViewRecipes)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        //Check that the recipe title is displayed and matches
        onView(withText(rName)).check(matches(isDisplayed()));
        onView(withId(R.id.recipe_title)).check(matches(withText(containsString(rName))));
        onView(withId(R.id.ingredients_body)).check(matches(withText(containsString(rIngredientName[0]))));
        onView(allOf(withId(R.id.directions_body), withText(containsString(rDirection[0])))).perform(scrollTo()).check(matches(isDisplayed()));
        onView(withId(R.id.recipe_info)).check(matches(withText(containsString("Takes " + preptime + "m to create."))));
        onView(withId(R.id.recipe_info)).check(matches(withText(containsString("Serves " + servings + " people."))));

        //Exit to the main activity
        pressBack();
    }
}