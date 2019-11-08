package com.cookbooker;

import org.junit.After;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import android.content.Context;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;

import com.cookbooker.presentation.MainActivity;

import java.io.File;

/*
 * Acceptance tests are allowed to assume that there are 3 recipes in the database as they are
 * included in the base install of the app. (Lemon Meringue Pie, Brown Butter and Toffee Chocolate
 * Chip Cookies, and Strawberry Pavlova.
 */

@LargeTest
@RunWith(Suite.class)
@Suite.SuiteClasses({
        SearchTest.class,
        ShoppingListTest.class,
        RandomRecipeTest.class,
        CopyRecipeTest.class,
        EditRecipeTest.class,
        NewRecipeTest.class
})

public class AllAcceptanceTests {}