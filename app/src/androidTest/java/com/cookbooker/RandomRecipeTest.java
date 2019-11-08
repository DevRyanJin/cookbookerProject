package com.cookbooker;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.cookbooker.presentation.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class RandomRecipeTest {
    @Rule
    /*
     * Set the starting activity to the Main Activity for each test
     */
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void randomRecipeTest() {

        //Open the top menu/toolbar
        onView(withContentDescription("Open navigation drawer")).perform(click());
        //Open the search bar at the top
        onView(withText("Find a Random Recipe!")).perform(click());
        onView(withId(R.id.recipe_title)).check(matches(isDisplayed()));

        //Exit to the main activity
        pressBack();
    }
}
