package net.ssehub.kernel_haven.scenario_tests.metrics;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import net.ssehub.kernel_haven.metric_haven.MetricResult;
import net.ssehub.kernel_haven.metric_haven.code_metrics.FanInOut;
import net.ssehub.kernel_haven.metric_haven.code_metrics.FanInOut.FanType;
import net.ssehub.kernel_haven.metric_haven.metric_components.config.MetricSettings;
import net.ssehub.kernel_haven.test_utils.PseudoVariabilityExtractor;
import net.ssehub.kernel_haven.variability_model.VariabilityVariable;

/**
 * Tests metrics execution of DegreeCentrality (variability-aware version of Fan-In/Out) metrics with srcML-Extractor.
 * @author El-Sharkawy
 *
 */
@RunWith(Parameterized.class)
public class DCFanInOutTests extends AbstractParameterizedTests {

    private static final Properties DC_IN_SETUP = new Properties();
    private static final Properties DC_OUT_SETUP = new Properties();
    
    static {
        DC_IN_SETUP.setProperty(MetricSettings.FAN_TYPE_SETTING.getKey(),
            FanType.DEGREE_CENTRALITY_IN_LOCALLY.name());
        DC_OUT_SETUP.setProperty(MetricSettings.FAN_TYPE_SETTING.getKey(),
            FanType.DEGREE_CENTRALITY_OUT_LOCALLY.name());
    }
    
    private Properties config;
    
    /**
     * Retrieves values from {@link #getParameters()}, creates, and executes the test.
     * @param fileName The name of the file to be tested.
     * @param testedFunctionName The function to test
     * @param expectedLineNo The expected starting line number of the function
     * @param expectedResultValue The expected value of the metric to compute.
     * @param config The configuration (variation through parameters) to use.
     * @param name The name for the test (will be ignored, only used for JUnit)
     */
    // CHECKSTYLE:OFF
    public DCFanInOutTests(String fileName, String testedFunctionName, int expectedLineNo,
        double expectedResultValue, Properties config, String name) {
    // CHECKSTYLE:ON
        
        super(fileName, testedFunctionName, expectedLineNo, expectedResultValue);
        this.config = config;
    }
    
    /**
     * Creates the parameters for this test.
     * 
     * @return The parameters of this test.
     */
    @Parameters(name = "{5}: {1}")
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
            // Degree Centrality In
//            {"DCFanInOut.c", "callingFunction", 2, 0, DC_IN_SETUP, "DC-In"}, // Never called -> Fan-In = 0
            {"DCFanInOut.c", "calledFunction", 10, 3, DC_IN_SETUP, "DC-In"}, // B, C, + 1 -> Fan-In = 3
            
            // Degree Centrality Out
//            {"DCFanInOut.c", "callingFunction", 2, 3, DC_OUT_SETUP, "DC-Out"}, // B, C, + 1 -> Fan-Out = 3
//            {"DCFanInOut.c", "calledFunction", 10, 0, DC_OUT_SETUP, "DC-Out"}, // No calls -> Fan-Out = 0
        });
    }
    
    /**
     * Initializes the properties and sets the variability model for {@link PseudoVariabilityExtractor}.
     */
    @BeforeClass
    public static void setup() {
        // Configuration of variability model
        VariabilityVariable varA = new VariabilityVariable("A", "bool");
        VariabilityVariable varB = new VariabilityVariable("B", "bool");
        VariabilityVariable varC = new VariabilityVariable("C", "bool");
        VariabilityVariable varD = new VariabilityVariable("D", "bool");
        
        PseudoVariabilityExtractor.configure(new File("mocked_varModel.dimacs"), varA, varB, varC, varD);
    }
    
    @Override
    protected String getMetric() {
        return FanInOut.class.getName();
    }
    
    @Override
    protected Map<String, MetricResult> runMetricAsMap(File file, Properties properties) {
        return super.runMetricAsMap(file, properties, false, true, true);
    }
    
    /**
     * Executes the NDavg tests.
     */
    @Test
    public void test() {
        super.test(config);
    }
}