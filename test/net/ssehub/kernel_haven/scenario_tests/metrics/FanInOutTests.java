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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import net.ssehub.kernel_haven.metric_haven.MetricResult;
import net.ssehub.kernel_haven.metric_haven.code_metrics.FanInOut;
import net.ssehub.kernel_haven.metric_haven.code_metrics.FanInOut.FanType;
import net.ssehub.kernel_haven.metric_haven.metric_components.config.MetricSettings;

/**
 * Tests metrics execution of Fan-In/Out metrics with srcML-Extractor.
 * @author El-Sharkawy
 *
 */
@RunWith(Parameterized.class)
public class FanInOutTests extends AbstractParameterizedTests {

    private static final Properties FAN_IN_SETUP = new Properties();
    private static final Properties FAN_OUT_SETUP = new Properties();
    
    static {
        FAN_IN_SETUP.setProperty(MetricSettings.FAN_TYPE_SETTING.getKey(),
            FanType.CLASSICAL_FAN_IN_LOCALLY.name());
        FAN_OUT_SETUP.setProperty(MetricSettings.FAN_TYPE_SETTING.getKey(),
            FanType.CLASSICAL_FAN_OUT_LOCALLY.name());
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
    public FanInOutTests(String fileName, String testedFunctionName, int expectedLineNo,
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
            // FAN-IN
            {"NoVariabilityFunctionsCalls.c", "funcNoCall", 4, 0, FAN_IN_SETUP, "Fan-In"},
            {"NoVariabilityFunctionsCalls.c", "funcCallsOne", 9, 0, FAN_IN_SETUP, "Fan-In"},
            {"NoVariabilityFunctionsCalls.c", "funcCalledByOne", 13, 1, FAN_IN_SETUP, "Fan-In"},
            {"NoVariabilityFunctionsCalls.c", "v7", 18, 3, FAN_IN_SETUP, "Fan-In"},
            {"NoVariabilityFunctionsCalls.c", "v8", 22, 1, FAN_IN_SETUP, "Fan-In"},
            {"NoVariabilityFunctionsCalls.c", "v9", 27, 0, FAN_IN_SETUP, "Fan-In"},
            
            // FAN-OUT
            {"NoVariabilityFunctionsCalls.c", "funcNoCall", 4, 0, FAN_OUT_SETUP, "Fan-Out"},
            {"NoVariabilityFunctionsCalls.c", "funcCallsOne", 9, 1, FAN_OUT_SETUP, "Fan-Out"},
            {"NoVariabilityFunctionsCalls.c", "funcCalledByOne", 13, 0, FAN_OUT_SETUP, "Fan-Out"},
            {"NoVariabilityFunctionsCalls.c", "v7", 18, 1, FAN_OUT_SETUP, "Fan-Out"},
            {"NoVariabilityFunctionsCalls.c", "v8", 22, 2, FAN_OUT_SETUP, "Fan-Out"},
            {"NoVariabilityFunctionsCalls.c", "v9", 27, 1, FAN_OUT_SETUP, "Fan-Out"},
        });
    }
    
    @Override
    protected String getMetric() {
        return FanInOut.class.getName();
    }
    
    @Override
    protected Map<String, MetricResult> runMetricAsMap(File file, Properties properties) {
        return super.runMetricAsMap(file, properties, false, false, true);
    }
    
    /**
     * Executes the tests.
     */
    @Test
    public void test() {
        super.test(config);
    }
}
