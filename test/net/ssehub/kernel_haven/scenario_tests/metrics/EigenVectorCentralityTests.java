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
import net.ssehub.kernel_haven.metric_haven.code_metrics.EigenVectorCentrality;
import net.ssehub.kernel_haven.metric_haven.code_metrics.FanInOut.FanType;
import net.ssehub.kernel_haven.metric_haven.metric_components.config.MetricSettings;

/**
 * Tests metrics execution of EigenVectorCentrality metrics with srcML-Extractor.
 * @author El-Sharkawy
 *
 */
@RunWith(Parameterized.class)
public class EigenVectorCentralityTests extends AbstractParameterizedTests {

    private static final Properties FAN_OUT = new Properties();
    private static final Properties FAN_IN = new Properties();
    
    static {
        FAN_OUT.setProperty(MetricSettings.FAN_TYPE_SETTING.getKey(),
            FanType.CLASSICAL_FAN_OUT_LOCALLY.name());
        FAN_IN.setProperty(MetricSettings.FAN_TYPE_SETTING.getKey(),
            FanType.CLASSICAL_FAN_IN_LOCALLY.name());
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
    public EigenVectorCentralityTests(String fileName, String testedFunctionName, int expectedLineNo,
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
            // Based on Fan-Out
            {"EigenVectorCentrality.c", "v1", 3, 8, FAN_OUT, "EV-OUT (classical)"}, 
            {"EigenVectorCentrality.c", "v2", 9, 6, FAN_OUT, "EV-OUT (classical)"}, 
            {"EigenVectorCentrality.c", "v3", 14, 8, FAN_OUT, "EV-OUT (classical)"}, 
            {"EigenVectorCentrality.c", "v4", 20, 7, FAN_OUT, "EV-OUT (classical)"}, 
            {"EigenVectorCentrality.c", "v5", 26, 3, FAN_OUT, "EV-OUT (classical)"}, 
            {"EigenVectorCentrality.c", "v6", 30, 0, FAN_OUT, "EV-OUT (classical)"},
            {"EigenVectorCentrality.c", "v7", 34, 3, FAN_OUT, "EV-OUT (classical)"},
            {"EigenVectorCentrality.c", "v8", 38, 1, FAN_OUT, "EV-OUT (classical)"},
            {"EigenVectorCentrality.c", "v9", 43, 1, FAN_OUT, "EV-OUT (classical)"},
            
            // Based on Fan-In
            {"EigenVectorCentrality.c", "v1", 3, 8, FAN_IN, "EV-IN (classical)"}, 
            {"EigenVectorCentrality.c", "v2", 9, 6, FAN_IN, "EV-IN (classical)"}, 
            {"EigenVectorCentrality.c", "v3", 14, 8, FAN_IN, "EV-IN (classical)"}, 
            {"EigenVectorCentrality.c", "v4", 20, 7, FAN_IN, "EV-IN (classical)"}, 
            {"EigenVectorCentrality.c", "v5", 26, 3, FAN_IN, "EV-IN (classical)"}, 
            {"EigenVectorCentrality.c", "v6", 30, 0, FAN_IN, "EV-IN (classical)"}, 
            {"EigenVectorCentrality.c", "v7", 34, 1, FAN_IN, "EV-IN (classical)"}, 
            {"EigenVectorCentrality.c", "v8", 38, 3, FAN_IN, "EV-IN (classical)"}, 
            {"EigenVectorCentrality.c", "v9", 43, 3, FAN_IN, "EV-IN (classical)"}, 
        });
    }
    
    @Override
    protected String getMetric() {
        return EigenVectorCentrality.class.getName();
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
