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
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

import net.ssehub.kernel_haven.metric_haven.MetricResult;
import net.ssehub.kernel_haven.metric_haven.code_metrics.FanInOut;
import net.ssehub.kernel_haven.metric_haven.code_metrics.FanInOut.FanType;
import net.ssehub.kernel_haven.metric_haven.metric_components.config.MetricSettings;

/**
 * Tests metrics execution of Fan-In/Out metrics with srcML-Extractor.
 * @author El-Sharkawy
 *
 */
public class FanInOutTests extends AbstractCodeMetricTests {

    private static final Properties FAN_IN_SETUP = new Properties();
    private static final Properties FAN_OUT_SETUP = new Properties();
    
    static {
        FAN_IN_SETUP.setProperty(MetricSettings.FAN_TYPE_SETTING.getKey(),
            FanType.CLASSICAL_FAN_IN_LOCALLY.name());
        FAN_OUT_SETUP.setProperty(MetricSettings.FAN_TYPE_SETTING.getKey(),
            FanType.CLASSICAL_FAN_OUT_LOCALLY.name());
    }
    
    @Override
    protected String getMetric() {
        return FanInOut.class.getName();
    }
    
    /**
     * Tests <b>fan-in</b> on method which isn't called and does not call any function.
     */
    @Test
    public void funcNoCallTestFanIn() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "NoVariabilityFunctionsCalls.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, FAN_IN_SETUP, false, false, true);
        
        assertMetricResult(result.get("funcNoCall"), 4, 0);
    }
    
    /**
     * Tests <b>fan-out</b> on method which isn't called and does not call any function.
     */
    @Test
    public void funcNoCallTestFanOut() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "NoVariabilityFunctionsCalls.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, FAN_OUT_SETUP, false, false, true);
        
        assertMetricResult(result.get("funcNoCall"), 4, 0);
    }
    
    /**
     * Tests <b>fan-in</b> on method which calls other function, but isn't called.
     */
    @Test
    public void funcCallsOneTestFanIn() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "NoVariabilityFunctionsCalls.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, FAN_IN_SETUP, false, false, true);
        
        assertMetricResult(result.get("funcCallsOne"), 9, 0);
    }
    
    /**
     * Tests <b>fan-out</b> on method which calls other function, but isn't called.
     */
    @Test
    public void funcCallsOneTestFanOut() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "NoVariabilityFunctionsCalls.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, FAN_OUT_SETUP, false, false, true);
        
        assertMetricResult(result.get("funcCallsOne"), 9, 1);
    }
    
    /**
     * Tests <b>fan-in</b> on method which is called by one, but does not call other functions.
     */
    @Test
    public void funcCalledByOneTestFanIn() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "NoVariabilityFunctionsCalls.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, FAN_IN_SETUP, false, false, true);
        
        assertMetricResult(result.get("funcCalledByOne"), 13, 1);
    }
    
    /**
     * Tests <b>fan-out</b> on method which is called by one, but does not call other functions.
     */
    @Test
    public void funcCalledByOneTestFanOut() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "NoVariabilityFunctionsCalls.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, FAN_OUT_SETUP, false, false, true);
        
        assertMetricResult(result.get("funcCalledByOne"), 13, 0);
    }
}
