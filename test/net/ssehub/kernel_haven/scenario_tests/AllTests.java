package net.ssehub.kernel_haven.scenario_tests;

import java.io.File;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import net.ssehub.kernel_haven.scenario_tests.metrics.AllMetricTests;

/**
 * All scenario tests.
 *
 * @author Adam
 */
@RunWith(Suite.class)
@SuiteClasses({
    AllMetricTests.class,
    
    UndertakerDeadCode.class,
    })
public class AllTests {
    
    public static final File TESTDATA = new File("testdata");

}
