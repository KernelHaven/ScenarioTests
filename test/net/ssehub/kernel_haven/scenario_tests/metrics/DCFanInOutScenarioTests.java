package net.ssehub.kernel_haven.scenario_tests.metrics;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.StringJoiner;

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
public class DCFanInOutScenarioTests extends AbstractParameterizedTests {

    private static final Properties DC_IN_SETUP = new Properties();
    private static final Properties DC_OUT_SETUP = new Properties();
    private static final Properties FAN_OUT_SETUP = new Properties();
    
    static {
        DC_IN_SETUP.setProperty(MetricSettings.FAN_TYPE_SETTING.getKey(),
            FanType.DEGREE_CENTRALITY_IN_GLOBALLY.name());
        DC_OUT_SETUP.setProperty(MetricSettings.FAN_TYPE_SETTING.getKey(),
            FanType.DEGREE_CENTRALITY_OUT_GLOBALLY.name());
        FAN_OUT_SETUP.setProperty(MetricSettings.FAN_TYPE_SETTING.getKey(),
                FanType.CLASSICAL_FAN_OUT_LOCALLY.name());
        
        File folder = new File(AbstractCodeMetricTests.TESTDATA, "sysconTestcase");
        StringJoiner sj = new StringJoiner(",");
        for (String fileName : folder.list()) {
            sj.add(fileName);
        }
        
        // Add all files to GLOBAL settings
        DC_IN_SETUP.setProperty("code.extractor.files", sj.toString());
        DC_OUT_SETUP.setProperty("code.extractor.files", sj.toString());
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
    public DCFanInOutScenarioTests(String fileName, String testedFunctionName, int expectedLineNo,
        double expectedResultValue, Properties config, String name) {
    // CHECKSTYLE:ON
        
        super("sysconTestcase/" + fileName, testedFunctionName, expectedLineNo, expectedResultValue);
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
//            // Classical Fan-Out
            {"syscon.c", "of_syscon_register", 20, 10, FAN_OUT_SETUP, "Fan-Out"}, // 11 calls to 10 functions
//            
//            // Degree Centrality In
            {"syscon.c", "of_syscon_register", 20, 0, DC_IN_SETUP, "DC-In"},     // Not called -> Fan-In = 0
            {"io.h", "iounmap", 830, 2, DC_IN_SETUP, "DC-In"},                   // Guarded call function -> Fan-In = 2
            
//            // Degree Centrality Out
            {"syscon.c", "of_syscon_register", 20, 2, DC_OUT_SETUP, "DC-Out"},   // Calls guarded iounmap -> DC-Out = 2
            {"io.h", "iounmap", 830, 0, DC_OUT_SETUP, "DC-Out"},                 // Empty function -> Fan-Out = 0
        });
    }
    
    /**
     * Initializes the properties and sets the variability model for {@link PseudoVariabilityExtractor}.
     */
    @BeforeClass
    public static void setup() {
        // Configuration of variability model
        PseudoVariabilityExtractor.configure(new File("mocked_varModel.dimacs"),
            new VariabilityVariable("CONFIG_MMU", "bool"));
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
