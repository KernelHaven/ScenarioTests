package net.ssehub.kernel_haven.scenario_tests.metrics.weights;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * All weight scenario tests.
 *
 * @author El-Sharkawy
 */
@RunWith(Suite.class)
@SuiteClasses({
    CTCRTests.class,
    FeatureDistanceTests.class,
    VariabilityTypeMeasureTest.class,
    HierarchyWeightTest.class,
    })
public class AllWeightsTests {
    

}
