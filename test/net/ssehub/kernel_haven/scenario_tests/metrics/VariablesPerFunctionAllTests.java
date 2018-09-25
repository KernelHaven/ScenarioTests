package net.ssehub.kernel_haven.scenario_tests.metrics;

import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import net.ssehub.kernel_haven.metric_haven.code_metrics.VariablesPerFunction.VarType;
import net.ssehub.kernel_haven.metric_haven.metric_components.VariablesPerFunctionMetric;

/**
 * Tests metrics execution of variables per function (<b>all</b>) metrics with srcML-Extractor.
 * @author El-Sharkawy
 *
 */
@RunWith(Parameterized.class)
public class VariablesPerFunctionAllTests extends AbstractParameterizedTests {

    private static final Properties VAR_ALL = new Properties();
    
    static {
        VAR_ALL.setProperty(VariablesPerFunctionMetric.VARIABLE_TYPE_SETTING.getKey(), VarType.ALL.name());
    }
    
    /**
     * Retrieves values from {@link #getParameters()}, creates, and executes the test.
     * @param fileName The name of the file to be tested.
     * @param testedFunctionName The function to test
     * @param expectedLineNo The expected starting line number of the function
     * @param expectedResultValue The expected value of the metric to compute.
     */
    public VariablesPerFunctionAllTests(String fileName, String testedFunctionName, int expectedLineNo,
        double expectedResultValue) {
        
        super(fileName, testedFunctionName, expectedLineNo, expectedResultValue);
    }
    
    @Override
    protected String getMetric() {
        return VariablesPerFunctionMetric.class.getName();
    }
    
    /**
     * Creates the parameters for this test.
     * 
     * @return The parameters of this test.
     */
    @Parameters(name = "Vars (All): {1}")
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
            {"VariabilityFunctions.c", "funcEmpty", 4, 0},
            {"VariabilityFunctions.c", "funcDecl", 8, 1},
            {"VariabilityFunctions.c", "funcHalfVar", 14, 1},
            {"VariabilityFunctions.c", "funcVarNesting", 21, 2},
            {"VariabilityFunctions.c", "conditionalFunction1", 31, 2},
            {"VariabilityFunctions.c", "conditionalFunction2", 38, 2}
        });
    }
    
    /**
     * Executes the Internal Vars tests.
     */
    @Test
    public void test() {
        super.test(VAR_ALL);
    }

}

