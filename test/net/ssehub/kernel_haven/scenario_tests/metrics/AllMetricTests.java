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
    // Lines of Code variations
    LoCTests.class,
    LoFTests.class,
    PLoFTests.class,
    
    // Nesting depth tests
    NestingDepthMaxTests.class,
    NestingDepthAvgTests.class,
    VPNestingDepthMaxTests.class,
    VPNestingDepthAvgTests.class,
    
    // McCabe Tests
    McCabeTests.class,
    McCabeOnVpTests.class,
    
    FanInOutTests.class,
    RobostnessTest.class
    })
public class AllMetricTests {
    

}
