package com.cookbooker;

import android.content.Context;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.view.KeyEvent;
import android.widget.EditText;

import com.cookbooker.presentation.MainActivity;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.not;

public class EditRecipeTest {
    @Rule
    /*
     * Set the starting activity to the Main Activity for each test
     */
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void editLemonMeringuePie() {
        String oldName = "Lemon meringue pie";
        String newName = "Meringue";
        String newIngredient = "fresh egg white";
        String newDirection = "Broil in the oven for about 10 minutes or until peaks turn golden brown, remove and allow to cool before serving.";
        //Don't know who's where and we want specifically the Lemon Meringue Pie one..
        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText("Search")).perform(click());
        //Search has opened up and gained focus of the kbd
        onView(isAssignableFrom(EditText.class)).perform(typeText(oldName), pressKey(KeyEvent.KEYCODE_ENTER));
        //Kbd should automagially disappear
        //Click the first element
        onView(withId(R.id.recyclerViewRecipes)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        //Check that the recipe title is displayed and matches
        onView(withText(oldName)).check(matches(isDisplayed()));
        onView(withId(R.id.recipe_title)).check(matches(withText(containsString(oldName))));

        //Open the EditRecipeActivity
        onView(withText("Edit Recipe")).perform(click());
        //Change the title
        onView(withId(R.id.recipe_title)).perform(clearText(), typeText(newName), pressKey(KeyEvent.KEYCODE_ENTER));
        closeSoftKeyboard();

        //Delete the unused ingredients
        onView(allOf(withId(R.id.delete_button), hasSibling(withText("egg yolk")))).perform(scrollTo(), click());
        onView(allOf(withId(R.id.delete_button), hasSibling(withText("plain flour")))).perform(scrollTo(), click());
        onView(allOf(withId(R.id.delete_button), hasSibling(withText("butter")))).perform(scrollTo(), click());
        onView(allOf(withId(R.id.delete_button), hasSibling(withText("icing sugar")))).perform(scrollTo(), click());
        onView(allOf(withId(R.id.delete_button), hasSibling(withText("large egg")))).perform(scrollTo(), click());
        onView(allOf(withId(R.id.delete_button), hasSibling(withText("lemon's zest")))).perform(scrollTo(), click());
        onView(allOf(withId(R.id.delete_button), hasSibling(withText("lemon's juice")))).perform(scrollTo(), click());

        //Remove all the unused directions
        onView(allOf(withId(R.id.delete_button), hasSibling(withText(containsString("Spoon on top of the filled"))))).perform(scrollTo(), click());
        onView(allOf(withId(R.id.delete_button), hasSibling(withText(containsString("Pre-heat"))))).perform(scrollTo(), click());
        onView(allOf(withId(R.id.delete_button), hasSibling(withText(containsString("First make the pastry"))))).perform(scrollTo(), click());
        onView(allOf(withId(R.id.delete_button), hasSibling(withText(containsString("Tip the pastry"))))).perform(scrollTo(), click());
        onView(allOf(withId(R.id.delete_button), hasSibling(withText(containsString("Take the pastry"))))).perform(scrollTo(), click());
        onView(allOf(withId(R.id.delete_button), hasSibling(withText(containsString("Line the pastry"))))).perform(scrollTo(), click());
        onView(allOf(withId(R.id.delete_button), hasSibling(withText(containsString("Remove from the oven"))))).perform(scrollTo(), click());
        onView(allOf(withId(R.id.delete_button), hasSibling(withText(containsString("For the filling"))))).perform(scrollTo(), click());
        onView(allOf(withId(R.id.delete_button), hasSibling(withText(containsString("In a bowl mix together"))))).perform(scrollTo(), click());
        onView(withText(containsString("Bake in the oven"))).perform(replaceText(newDirection));
        onView(allOf(withId(R.id.ingredients_edit_label), hasSibling(withText(containsString("egg white"))))).perform(replaceText(newIngredient));

        onView(withId(R.id.servings_value)).perform(replaceText("1"));
        onView(withId(R.id.preptime_value)).perform(replaceText("17"));

        closeSoftKeyboard();
        onView(withId(R.id.save_button)).perform(scrollTo(), click());

        //Roll back into the RecipeView to verify all changes
        pressBack();

        onView(withText(newName)).check(matches(isDisplayed()));
        onView(withId(R.id.recipe_title)).check(matches(withText(containsString(newName))));
        onView(withId(R.id.ingredients_body)).check(matches(withText(containsString(newIngredient))));
        //Be sure that the ingredient was actually deleted
        onView(withId(R.id.ingredients_body)).check(matches(not(withText(containsString("egg yolk")))));
        onView(withId(R.id.ingredients_body)).check(matches(not(withText(containsString("butter")))));
        onView(withId(R.id.ingredients_body)).check(matches(not(withText(containsString("plain flour")))));
        onView(allOf(withId(R.id.directions_body), withText(containsString(newDirection)))).perform(scrollTo()).check(matches(isDisplayed()));
        onView(withId(R.id.recipe_info)).check(matches(withText(containsString("Takes 17m to create."))));
        onView(withId(R.id.recipe_info)).check(matches(withText(containsString("Serves 1 people."))));

        pressBack();
        //Exit to the main menu
        pressBack();
    }

    @After
    //Need to delete the altered DB once the tests have completed
    public void afterTest() {
        for (File file : activityRule.getActivity().getDir("database", Context.MODE_PRIVATE).listFiles()) {
            file.delete();
        }
    }
}
