/*
 * Copyright 2017-2019 University of Hildesheim, Software Systems Engineering
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
            {"DCFanInOut.c", "callingFunction", 3, 0, DC_IN_SETUP, "DC-In"}, // Never called -> Fan-In = 0
            {"DCFanInOut.c", "calledFunction", 11, 3, DC_IN_SETUP, "DC-In"}, // B, C, + 1 -> Fan-In = 3
            {"DCFanInOut.c", "callingFunction2", 18, 0, DC_IN_SETUP, "DC-In"}, // Never called -> Fan-In = 0
            {"DCFanInOut.c", "calledFunction2", 27, 4, DC_IN_SETUP, "DC-In"}, // !F, G, H + 1 -> Fan-In = 4
            
            // Degree Centrality Out
            {"DCFanInOut.c", "callingFunction", 3, 3, DC_OUT_SETUP, "DC-Out"}, // B, C, + 1 -> Fan-Out = 3
            {"DCFanInOut.c", "calledFunction", 11, 0, DC_OUT_SETUP, "DC-Out"}, // No calls -> Fan-Out = 0
            {"DCFanInOut.c", "callingFunction2", 18, 4, DC_OUT_SETUP, "DC-Out"}, // !F, G, H + 1 -> Fan-Out = 4
            {"DCFanInOut.c", "calledFunction2", 27, 0, DC_OUT_SETUP, "DC-Out"}, // Never called -> Fan-Out = 0
            
            // Tests alternative function implementation stuff
            {"FanOutAlternativeImplementations1.c", "func2", 12, 4, DC_OUT_SETUP, "DC-Out (ifdef impls)"},
            {"FanOutAlternativeImplementations2.c", "func2", 10, 4, DC_OUT_SETUP, "DC-Out (ifdef calls)"},
            
            // Tests a function implementation together with an empty function stub (dummy function)
            {"FanOutDummyImplementations1.c", "func2", 11, 4, DC_OUT_SETUP, "DC-Out (dummy ifdef impls)"},
            {"FanOutDummyImplementations2.c", "func2", 7, 2, DC_OUT_SETUP, "DC-Out (dummy ifdef calls)"},
        });
    }
    
    /**
     * Initializes the properties and sets the variability model for {@link PseudoVariabilityExtractor}.
     */
    @BeforeClass
    public static void setup() {
        // Configuration of variability model
        PseudoVariabilityExtractor.configure(new File("mocked_varModel.dimacs"),
            new VariabilityVariable("A", "bool"),
            new VariabilityVariable("B", "bool"),
            new VariabilityVariable("C", "bool"),
            new VariabilityVariable("E", "bool"),
            new VariabilityVariable("F", "bool"),
            new VariabilityVariable("G", "bool"),
            new VariabilityVariable("H", "bool"));
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
