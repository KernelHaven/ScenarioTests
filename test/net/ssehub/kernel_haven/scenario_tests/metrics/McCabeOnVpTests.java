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

import net.ssehub.kernel_haven.metric_haven.code_metrics.CyclomaticComplexity;
import net.ssehub.kernel_haven.metric_haven.code_metrics.CyclomaticComplexity.CCType;
import net.ssehub.kernel_haven.metric_haven.metric_components.config.MetricSettings;

/**
 * Tests metrics execution of McCabe on variation points with srcML-Extractor.
 * @author El-Sharkawy
 *
 */
@RunWith(Parameterized.class)
public class McCabeOnVpTests extends AbstractParameterizedTests {

    private static final Properties CC_VP = new Properties();
    
    static {
        CC_VP.setProperty(MetricSettings.CC_VARIABLE_TYPE_SETTING.getKey(),
            CCType.VARIATION_POINTS.name());
    }
    
    /**
     * Retrieves values from {@link #getParameters()}, creates, and executes the test.
     * @param fileName The name of the file to be tested.
     * @param testedFunctionName The function to test
     * @param expectedLineNo The expected starting line number of the function
     * @param expectedResultValue The expected value of the metric to compute.
     */
    public McCabeOnVpTests(String fileName, String testedFunctionName, int expectedLineNo,
        double expectedResultValue) {
        
        super(fileName, testedFunctionName, expectedLineNo, expectedResultValue);
    }
    
    @Override
    protected String getMetric() {
        return CyclomaticComplexity.class.getName();
    }
    

    /**
     * Creates the parameters for this test.
     * 
     * @return The parameters of this test.
     */
    @Parameters(name = "CC-VP: {1}")
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
            // Code without CPP
            {"NoVariabilityFunctions.c", "funcEmpty", 4, 1},
            {"NoVariabilityFunctions.c", "funcDecl", 8, 1},
            {"NoVariabilityFunctions.c", "funcIfElse", 12, 1},
            {"NoVariabilityFunctions.c", "funcGoto", 20, 1},
            {"NoVariabilityFunctions.c", "functDoWhile", 35, 1},
            {"NoVariabilityFunctions.c", "funcWhile", 72, 1},
            {"NoVariabilityFunctions.c", "funcFor", 55, 1},
            {"NoVariabilityFunctions.c", "funcSwitch", 90, 1},
            
            // Code with CPP
            {"VariabilityFunctions.c", "funcEmpty", 4, 1},
            {"VariabilityFunctions.c", "funcDecl", 8, 2},
            {"VariabilityFunctions.c", "funcHalfVar", 14, 2},
            {"VariabilityFunctions.c", "funcVarNesting", 21, 3}
            
        });
    }

    /**
     * Executes the NDavg tests.
     */
    @Test
    public void test() {
        super.test(CC_VP);
    }
}
