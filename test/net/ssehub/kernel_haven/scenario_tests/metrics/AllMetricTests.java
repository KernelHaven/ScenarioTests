package net.ssehub.kernel_haven.scenario_tests.metrics;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * All metric scenario tests.
 *
 * @author El-Sharkawy
 */
@RunWith(Suite.class)
@SuiteClasses({
    LoCTests.class,
    McCabeTests.class
    })
public class AllMetricTests {
    

}
