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
 * Tests metrics execution of Lines of Feature Code (LoF) metrics with srcML-Extractor.
 * @author El-Sharkawy
 *
 */
@RunWith(Parameterized.class)
public class LoFTests extends AbstractParameterizedTests {
    
    private static final Properties LOF_SETUP = new Properties();
    
    static {
        LOF_SETUP.setProperty(MetricSettings.LOC_TYPE_SETTING.getKey(),
            net.ssehub.kernel_haven.metric_haven.code_metrics.DLoC.LoFType.LOF.name());
    }

    /**
     * Retrieves values from {@link #getParameters()}, creates, and executes the test.
     * @param fileName The name of the file to be tested.
     * @param testedFunctionName The function to test
     * @param expectedLineNo The expected starting line number of the function
     * @param expectedResultValue The expected value of the metric to compute.
     */
    public LoFTests(String fileName, String testedFunctionName, int expectedLineNo, double expectedResultValue) {
        super(fileName, testedFunctionName, expectedLineNo, expectedResultValue);
    }
    
    /**
     * Creates the parameters for this test.
     * 
     * @return The parameters of this test.
     */
    @Parameters(name = "LoF: {1}")
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
            {"VariabilityFunctions.c", "funcEmpty", 4, 0},
            {"VariabilityFunctions.c", "funcDecl", 8, 1},
            {"VariabilityFunctions.c", "funcHalfVar", 14, 1},
            {"VariabilityFunctions.c", "funcVarNesting", 21, 1}
            
        });
    }
    
    @Override
    protected String getMetric() {
        return DLoC.class.getName();
    }
    
    /**
     * Executes the LoF tests.
     */
    @Test
    public void test() {
        super.test(LOF_SETUP);
    }
    
}
