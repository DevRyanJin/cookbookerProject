package com.cookbooker.Unit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AccessRecipeTest.class,
        RecipeBuilderTest.class,
        IngredientTest.class
})
public class AllUnitTests {
}
