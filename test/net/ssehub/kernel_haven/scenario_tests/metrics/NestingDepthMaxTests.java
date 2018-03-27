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
 * Tests metrics execution of nesting depth (<b>max</b>) metrics (NDmax) with srcML-Extractor.
 * @author El-Sharkawy
 *
 */
@RunWith(Parameterized.class)
public class NestingDepthMaxTests extends AbstractParameterizedTests {

    private static final Properties ND_MAX_SETUP = new Properties();
    
    static {
        ND_MAX_SETUP.setProperty(NestingDepthMetric.ND_TYPE_SETTING.getKey(),
            NestingDepthMetric.NDType.CLASSIC_ND_MAX.name());
    }
    
    /**
     * Retrieves values from {@link #getParameters()}, creates, and executes the test.
     * @param fileName The name of the file to be tested.
     * @param testedFunctionName The function to test
     * @param expectedLineNo The expected starting line number of the function
     * @param expectedResultValue The expected value of the metric to compute.
     */
    public NestingDepthMaxTests(String fileName, String testedFunctionName, int expectedLineNo,
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
    @Parameters(name = "NDmax: {1}")
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
            {"NoVariabilityFunctions.c", "funcEmpty", 4, 1},
            {"NoVariabilityFunctions.c", "funcDecl", 8, 1},
            {"NoVariabilityFunctions.c", "funcIfElse", 12, 2},
            {"NoVariabilityFunctions.c", "funcGoto", 20, 2},
            {"NoVariabilityFunctions.c", "functDoWhile", 35, 3},
            {"NoVariabilityFunctions.c", "funcWhile", 72, 3},
            {"NoVariabilityFunctions.c", "funcFor", 55, 3},
            {"NoVariabilityFunctions.c", "funcSwitch", 90, 2}
            
        });
    }
    
    /**
     * Executes the NDmax tests.
     */
    @Test
    public void test() {
        super.test(ND_MAX_SETUP);
    }

}
