package com.cookbooker.Integration;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        RecipeValidatorTest.class,
        Database_HSQLTest.class
})
public class AllIntegrationTests {
}