package net.ssehub.kernel_haven.scenario_tests.metrics;

import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import net.ssehub.kernel_haven.metric_haven.metric_components.NestingDepthMetric;

/**
 * Tests metrics execution of nesting depth (<b>avg</b>) for variation point metrics (VP-NDavg) with srcML-Extractor.
 * @author El-Sharkawy
 *
 */
@RunWith(Parameterized.class)
public class VPNestingDepthAvgTests extends AbstractParameterizedTests {

    private static final Properties ND_AVG_SETUP = new Properties();
    
    static {
        ND_AVG_SETUP.setProperty(NestingDepthMetric.ND_TYPE_SETTING.getKey(),
            NestingDepthMetric.NDType.VP_ND_AVG.name());
    }
    
    /**
     * Retrieves values from {@link #getParameters()}, creates, and executes the test.
     * @param fileName The name of the file to be tested.
     * @param testedFunctionName The function to test
     * @param expectedLineNo The expected starting line number of the function
     * @param expectedResultValue The expected value of the metric to compute.
     */
    public VPNestingDepthAvgTests(String fileName, String testedFunctionName, int expectedLineNo,
        double expectedResultValue) {
        
        super(fileName, testedFunctionName, expectedLineNo, expectedResultValue);
    }
    
    @Override
    protected String getMetric() {
        return NestingDepthMetric.class.getName();
    }
    
    /**
     * Creates the parameters for this test.
     * 
     * @return The parameters of this test.
     */
    @Parameters(name = "VP-NDavg: {1}")
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
            {"VariabilityFunctions.c", "funcEmpty", 4, 0},
            {"VariabilityFunctions.c", "funcDecl", 8, 1},
            {"VariabilityFunctions.c", "funcHalfVar", 14, 1.0 / 2},
            {"VariabilityFunctions.c", "funcVarNesting", 21, 2.0 / 2}
            
        });
    }
    
    /**
     * Executes the VP-NDavg tests.
     */
    @Test
    public void test() {
        super.test(ND_AVG_SETUP);
    }

}
