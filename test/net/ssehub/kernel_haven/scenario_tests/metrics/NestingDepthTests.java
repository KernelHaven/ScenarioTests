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

import net.ssehub.kernel_haven.metric_haven.code_metrics.NestingDepth;
import net.ssehub.kernel_haven.metric_haven.code_metrics.NestingDepth.NDType;
import net.ssehub.kernel_haven.metric_haven.metric_components.config.MetricSettings;

/**
 * Tests metrics execution of nesting depth metrics with srcML-Extractor.
 * @author El-Sharkawy
 *
 */
@RunWith(Parameterized.class)
public class NestingDepthTests extends AbstractParameterizedTests {

    private static final Properties ND_AVG_SETUP = new Properties();
    private static final Properties ND_MAX_SETUP = new Properties();
    private static final Properties VP_AVG_SETUP = new Properties();
    private static final Properties VP_MAX_SETUP = new Properties();
    
    static {
        ND_AVG_SETUP.setProperty(MetricSettings.ND_TYPE_SETTING.getKey(),
            NDType.CLASSIC_ND_AVG.name());
        ND_MAX_SETUP.setProperty(MetricSettings.ND_TYPE_SETTING.getKey(),
            NDType.CLASSIC_ND_MAX.name());
        VP_AVG_SETUP.setProperty(MetricSettings.ND_TYPE_SETTING.getKey(),
            NDType.VP_ND_AVG.name());
        VP_MAX_SETUP.setProperty(MetricSettings.ND_TYPE_SETTING.getKey(),
            NDType.VP_ND_MAX.name());
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
    public NestingDepthTests(String fileName, String testedFunctionName, int expectedLineNo,
        double expectedResultValue, Properties config, String name) {
    // CHECKSTYLE:ON
        
        super(fileName, testedFunctionName, expectedLineNo, expectedResultValue);
        this.config = config;
    }
    
    @Override
    protected String getMetric() {
        return NestingDepth.class.getName();
    }
    

    /**
     * Creates the parameters for this test.
     * 
     * @return The parameters of this test.
     */
    @Parameters(name = "{5}: {1}")
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
            // Classical ND average
            // Code without CPP
            {"NoVariabilityFunctions.c", "funcEmpty", 4, 1, ND_AVG_SETUP, "NDavg"},
            {"NoVariabilityFunctions.c", "funcDecl", 8, 1, ND_AVG_SETUP, "NDavg"},
            {"NoVariabilityFunctions.c", "funcIfElse", 12, 2, ND_AVG_SETUP, "NDavg"},
            {"NoVariabilityFunctions.c", "funcGoto", 20, 6.0 / 5, ND_AVG_SETUP, "NDavg"},
            {"NoVariabilityFunctions.c", "functDoWhile", 35, 2, ND_AVG_SETUP, "NDavg"},
            {"NoVariabilityFunctions.c", "funcWhile", 72, 2, ND_AVG_SETUP, "NDavg"},
            {"NoVariabilityFunctions.c", "funcFor", 55, 2, ND_AVG_SETUP, "NDavg"},
            {"NoVariabilityFunctions.c", "funcSwitch", 90, 12.0 / 7, ND_AVG_SETUP, "NDavg"},
            
            // Code with CPP
            {"VariabilityFunctions.c", "funcEmpty", 4, 1, ND_AVG_SETUP, "NDavg"},
            {"VariabilityFunctions.c", "funcDecl", 8, 1, ND_AVG_SETUP, "NDavg"},
            {"VariabilityFunctions.c", "funcHalfVar", 14, 1, ND_AVG_SETUP, "NDavg"},
            {"VariabilityFunctions.c", "funcVarNesting", 21, 1, ND_AVG_SETUP, "NDavg"},
            {"VariabilityFunctions.c", "conditionalFunction1", 31, 1, ND_AVG_SETUP, "NDavg"},
            {"VariabilityFunctions.c", "conditionalFunction2", 38, 1, ND_AVG_SETUP, "NDavg"},
            {"VariabilityFunctions.c", "funcCppElse", 46, 1, ND_AVG_SETUP, "NDavg"},
            
            // Classical ND max
            // Code without CPP
            {"NoVariabilityFunctions.c", "funcEmpty", 4, 1, ND_MAX_SETUP, "NDmax"},
            {"NoVariabilityFunctions.c", "funcDecl", 8, 1, ND_MAX_SETUP, "NDmax"},
            {"NoVariabilityFunctions.c", "funcIfElse", 12, 2, ND_MAX_SETUP, "NDmax"},
            {"NoVariabilityFunctions.c", "funcGoto", 20, 2, ND_MAX_SETUP, "NDmax"},
            {"NoVariabilityFunctions.c", "functDoWhile", 35, 3, ND_MAX_SETUP, "NDmax"},
            {"NoVariabilityFunctions.c", "funcWhile", 72, 3, ND_MAX_SETUP, "NDmax"},
            {"NoVariabilityFunctions.c", "funcFor", 55, 3, ND_MAX_SETUP, "NDmax"},
            {"NoVariabilityFunctions.c", "funcSwitch", 90, 2, ND_MAX_SETUP, "NDmax"},
            
            // Code with CPP
            {"VariabilityFunctions.c", "funcEmpty", 4, 1, ND_MAX_SETUP, "NDmax"},
            {"VariabilityFunctions.c", "funcDecl", 8, 1, ND_MAX_SETUP, "NDmax"},
            {"VariabilityFunctions.c", "funcHalfVar", 14, 1, ND_MAX_SETUP, "NDmax"},
            {"VariabilityFunctions.c", "funcVarNesting", 21, 1, ND_MAX_SETUP, "NDmax"},
            {"VariabilityFunctions.c", "conditionalFunction1", 31, 1, ND_MAX_SETUP, "NDmax"},
            {"VariabilityFunctions.c", "conditionalFunction2", 38, 1, ND_MAX_SETUP, "NDmax"},
            {"VariabilityFunctions.c", "funcCppElse", 46, 1, ND_MAX_SETUP, "NDmax"},
            
            // VP ND avg
            {"VariabilityFunctions.c", "funcEmpty", 4, 0, VP_AVG_SETUP, "VP-ND_avg"},
            {"VariabilityFunctions.c", "funcDecl", 8, 1, VP_AVG_SETUP, "VP-ND_avg"},
            {"VariabilityFunctions.c", "funcHalfVar", 14, 1.0 / 2, VP_AVG_SETUP, "VP-ND_avg"},
            {"VariabilityFunctions.c", "funcVarNesting", 21, 2.0 / 2, VP_AVG_SETUP, "VP-ND_avg"},
            {"VariabilityFunctions.c", "conditionalFunction1", 31, 1.0 / 1, VP_AVG_SETUP, "VP-ND_avg"},
            {"VariabilityFunctions.c", "conditionalFunction2", 38, 2.0 / 1, VP_AVG_SETUP, "VP-ND_avg"},
            {"VariabilityFunctions.c", "funcCppElse", 46, 3.0 / 4.0, VP_AVG_SETUP, "VP-ND_avg"},
            
            // VP ND max
            {"VariabilityFunctions.c", "funcEmpty", 4, 0, VP_MAX_SETUP, "VP-ND_max"},
            {"VariabilityFunctions.c", "funcDecl", 8, 1, VP_MAX_SETUP, "VP-ND_max"},
            {"VariabilityFunctions.c", "funcHalfVar", 14, 1, VP_MAX_SETUP, "VP-ND_max"},
            {"VariabilityFunctions.c", "funcVarNesting", 21, 2, VP_MAX_SETUP, "VP-ND_max"},
            {"VariabilityFunctions.c", "conditionalFunction1", 31, 1, VP_MAX_SETUP, "VP-ND_max"},
            {"VariabilityFunctions.c", "conditionalFunction2", 38, 2, VP_MAX_SETUP, "VP-ND_max"},
            {"VariabilityFunctions.c", "funcCppElse", 46, 1, VP_MAX_SETUP, "VP-ND_max"},
        });
    }

    /**
     * Executes the NDavg tests.
     */
    @Test
    public void test() {
        super.test(config);
    }
}
