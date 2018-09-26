package net.ssehub.kernel_haven.scenario_tests.metrics;

import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import net.ssehub.kernel_haven.metric_haven.code_metrics.DLoC;
import net.ssehub.kernel_haven.metric_haven.metric_components.config.MetricSettings;

/**
 * Tests metrics execution of Percentage of Lines of Feature Code (PLoF) metrics with srcML-Extractor.
 * @author El-Sharkawy
 *
 */
@RunWith(Parameterized.class)
public class PLoFTests extends AbstractParameterizedTests {
    
    private static final Properties PLOF_SETUP = new Properties();
    
    static {
        PLOF_SETUP.setProperty(MetricSettings.LOC_TYPE_SETTING.getKey(),
            net.ssehub.kernel_haven.metric_haven.code_metrics.DLoC.LoFType.PLOF.name());
    }

    /**
     * Retrieves values from {@link #getParameters()}, creates, and executes the test.
     * @param fileName The name of the file to be tested.
     * @param testedFunctionName The function to test
     * @param expectedLineNo The expected starting line number of the function
     * @param expectedResultValue The expected value of the metric to compute.
     */
    public PLoFTests(String fileName, String testedFunctionName, int expectedLineNo, double expectedResultValue) {
        super(fileName, testedFunctionName, expectedLineNo, expectedResultValue);
    }
    
    /**
     * Creates the parameters for this test.
     * 
     * @return The parameters of this test.
     */
    @Parameters(name = "PLoF: {1}")
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
            {"VariabilityFunctions.c", "funcEmpty", 4, 0},
            {"VariabilityFunctions.c", "funcDecl", 8, 1},
            {"VariabilityFunctions.c", "funcHalfVar", 14, 0.5},
            {"VariabilityFunctions.c", "funcVarNesting", 21, 0.5}
            
        });
    }
    
    @Override
    protected String getMetric() {
        return DLoC.class.getName();
    }
    
    /**
     * Executes the PLoF tests.
     */
    @Test
    public void test() {
        super.test(PLOF_SETUP);
    }
}
