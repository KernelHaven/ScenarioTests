package net.ssehub.kernel_haven.scenario_tests.metrics;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import net.ssehub.kernel_haven.scenario_tests.metrics.weights.AllWeightsTests;

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
    NestingDepthTests.class,
    
    // McCabe Tests
    McCabeTests.class,
    McCabeOnVpTests.class,
    
    // Variables per Function Tests
    VariablesPerFunctionAllTests.class,
    VariablesPerFunctionExternalTests.class,
    VariablesPerFunctionInternalTests.class,
    
    // Fan-In/Out
    FanInOutTests.class,
    DCFanInOutTests.class,
    
    
    RobostnessTest.class,
    
    AllWeightsTests.class,
    })
public class AllMetricTests {
    

}
