
package com.cookbooker;

import com.cookbooker.Integration.AllIntegrationTests;
import com.cookbooker.Unit.AllUnitTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AllIntegrationTests.class,
        AllUnitTests.class
})

/**
 * Ease of use class to run both Integration and Unit tests with a single click
 */
public class AllTests {
}