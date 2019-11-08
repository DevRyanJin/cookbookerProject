package com.cookbooker;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.cookbooker.presentation.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;


@RunWith(AndroidJUnit4.class)
public class ShoppingListTest {
    @Rule
    /*
     * Set the starting activity to the Main Activity for each test
     */
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void shoppingListTest() {
        //Open the top menu/toolbar
        onView(withContentDescription("Open navigation drawer")).perform(click());

        //Open the Shopping List on the navigation bar
        onView(withText("Shopping List")).perform(click());

        //Type "strawberries" and add it to shopping list
        onView(withId(R.id.shopSearchText)).perform(typeText("strawberries"));
        onView(withId(R.id.shopAddButton)).perform(click());

        //Type "vanilla" and add it to shopping list
        onView(withId(R.id.shopSearchText)).perform(typeText("vanilla"));
        onView(withId(R.id.shopAddButton)).perform(click());

        //Type "redcurrants" and add it to shopping list
        onView(withId(R.id.shopSearchText)).perform(typeText("redcurrants"));
        onView(withId(R.id.shopAddButton)).perform(click());

        //Check box of "strawberries" is checked
        closeSoftKeyboard();

        onData(anything()).inAdapterView(withId(R.id.shopSearchResult)).atPosition(0).onChildView(withId(R.id.check_box)).perform(scrollTo(), click());

        //Check box of "vanilla" is checked
        onData(anything()).inAdapterView(withId(R.id.shopSearchResult)).atPosition(1).onChildView(withId(R.id.check_box)).perform(scrollTo(), click());

        //Check box of "redcurrants" is checked
        onData(anything()).inAdapterView(withId(R.id.shopSearchResult)).atPosition(2).onChildView(withId(R.id.check_box)).perform(scrollTo(), click());

        //Uncheck a check box of "strawberries" is checked
        onData(anything()).inAdapterView(withId(R.id.shopSearchResult)).atPosition(1).onChildView(withId(R.id.check_box)).perform(scrollTo(), click());

        //see the result
        onView(withId(R.id.shopSearchButton)).perform(click());

        //Click the first element
        onView(ViewMatchers.withId(R.id.recyclerViewRecipes)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //Check that a recipes ingredients match
        onView(ViewMatchers.withId(R.id.ingredients_body)).check(matches(withText(containsString("strawberries"))));
        onView(ViewMatchers.withId(R.id.ingredients_body)).check(matches(withText(containsString("redcurrants"))));

        //Exit to the search results
        pressBack();
        //Exit to the Shopping list
        pressBack();

        onData(anything()).inAdapterView(withId(R.id.shopSearchResult)).atPosition(1).onChildView(withId(R.id.shopping_input)).check(matches(withText("vanilla")));

        //remove "vanilla" from shopping list
        onData(anything()).inAdapterView(withId(R.id.shopSearchResult)).atPosition(1).onChildView(withId(R.id.remove_button)).perform(scrollTo(), click());

        //Check that vanilla no longer exists
        onData(anything()).inAdapterView(withId(R.id.shopSearchResult)).atPosition(1).onChildView(withId(R.id.shopping_input)).check(matches(not(withText("vanilla"))));

        //Exit to the main menu
        pressBack();
    }
}
