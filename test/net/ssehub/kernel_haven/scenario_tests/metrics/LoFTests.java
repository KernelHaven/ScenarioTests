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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import net.ssehub.kernel_haven.metric_haven.code_metrics.LoCMetric;
import net.ssehub.kernel_haven.metric_haven.code_metrics.LoCMetric.LoCType;
import net.ssehub.kernel_haven.metric_haven.metric_components.config.MetricSettings;

/**
 * Tests metrics execution of Lines of Feature Code (LoF) metrics with srcML-Extractor.
 * @author El-Sharkawy
 *
 */
@RunWith(Parameterized.class)
public class LoFTests extends AbstractParameterizedTests {
    
    private static final Map<LoCType, Properties> SETTINGS;
    
    static {
        Map<LoCType, Properties> settings = new HashMap<>();
        
        Properties prop = new Properties();
        prop.setProperty(MetricSettings.LOC_TYPE_SETTING.getKey(), LoCType.SCOC.name());
        settings.put(LoCType.SCOC, prop);
        
        prop = new Properties();
        prop.setProperty(MetricSettings.LOC_TYPE_SETTING.getKey(), LoCType.SCOF.name());
        settings.put(LoCType.SCOF, prop);
        
        prop = new Properties();
        prop.setProperty(MetricSettings.LOC_TYPE_SETTING.getKey(), LoCType.PSCOF.name());
        settings.put(LoCType.PSCOF, prop);
        
        prop = new Properties();
        prop.setProperty(MetricSettings.LOC_TYPE_SETTING.getKey(), LoCType.LOC.name());
        settings.put(LoCType.LOC, prop);

        prop = new Properties();
        prop.setProperty(MetricSettings.LOC_TYPE_SETTING.getKey(), LoCType.LOF.name());
        settings.put(LoCType.LOF, prop);
        
        prop = new Properties();
        prop.setProperty(MetricSettings.LOC_TYPE_SETTING.getKey(), LoCType.PLOF.name());
        settings.put(LoCType.PLOF, prop);
        
        SETTINGS = Collections.unmodifiableMap(settings);
    }
    
    private LoCType type;

    /**
     * Retrieves values from {@link #getParameters()}, creates, and executes the test.
     * @param fileName The name of the file to be tested.
     * @param testedFunctionName The function to test
     * @param expectedLineNo The expected starting line number of the function
     * @param expectedResultValue The expected value of the metric to compute.
     * @param type The LoC type to be used for the test
     */
    public LoFTests(String fileName, String testedFunctionName, int expectedLineNo, double expectedResultValue,
        LoCType type) {
        
        super(fileName, testedFunctionName, expectedLineNo, expectedResultValue);
        this.type = type;
    }
    
    /**
     * Creates the parameters for this test.
     * 
     * @return The parameters of this test.
     */
    @Parameters(name = "{4}: {1}")
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
            
            // Statements of Code
            {"VariabilityFunctions.c", "funcEmpty", 4, 1, LoCType.SCOC},
            {"VariabilityFunctions.c", "funcDecl", 8, 1, LoCType.SCOC},
            {"VariabilityFunctions.c", "funcHalfVar", 14, 2, LoCType.SCOC},
            {"VariabilityFunctions.c", "funcVarNesting", 21, 2, LoCType.SCOC},
            {"VariabilityFunctions.c", "conditionalFunction1", 31, 1, LoCType.SCOC},
            {"VariabilityFunctions.c", "conditionalFunction2", 39, 1, LoCType.SCOC},
            {"VariabilityFunctions.c", "funcCppElse", 48, 4, LoCType.SCOC},
            
            // Statements of Feature Code
            {"VariabilityFunctions.c", "funcEmpty", 4, 0, LoCType.SCOF},
            {"VariabilityFunctions.c", "funcDecl", 8, 1, LoCType.SCOF},
            {"VariabilityFunctions.c", "funcHalfVar", 14, 1, LoCType.SCOF},
            {"VariabilityFunctions.c", "funcVarNesting", 21, 1, LoCType.SCOF},
            {"VariabilityFunctions.c", "conditionalFunction1", 31, 1, LoCType.SCOF},
            {"VariabilityFunctions.c", "conditionalFunction2", 39, 1, LoCType.SCOF},
            {"VariabilityFunctions.c", "funcCppElse", 48, 3, LoCType.SCOF},
            
            // Percentage of Statements of Feature Code
            {"VariabilityFunctions.c", "funcEmpty", 4, 0, LoCType.PSCOF},
            {"VariabilityFunctions.c", "funcDecl", 8, 1, LoCType.PSCOF},
            {"VariabilityFunctions.c", "funcHalfVar", 14, 1d / 2, LoCType.PSCOF},
            {"VariabilityFunctions.c", "funcVarNesting", 21, 1d / 2, LoCType.PSCOF},
            {"VariabilityFunctions.c", "conditionalFunction1", 31, 1, LoCType.PSCOF},
            {"VariabilityFunctions.c", "conditionalFunction2", 39, 1, LoCType.PSCOF},
            {"VariabilityFunctions.c", "funcCppElse", 48, 3d / 4, LoCType.PSCOF},
            
            // Lines of Code
            {"VariabilityFunctions.c", "funcEmpty", 4, 3, LoCType.LOC},
            {"VariabilityFunctions.c", "funcDecl", 8, 5, LoCType.LOC},
            {"VariabilityFunctions.c", "funcHalfVar", 14, 6, LoCType.LOC},
            {"VariabilityFunctions.c", "funcVarNesting", 21, 8, LoCType.LOC},
            {"VariabilityFunctions.c", "conditionalFunction1", 31, 5, LoCType.LOC},
            {"VariabilityFunctions.c", "conditionalFunction2", 39, 7, LoCType.LOC},
            {"VariabilityFunctions.c", "funcCppElse", 48, 9, LoCType.LOC},
            
            
            // Lines of Feature Code
            {"VariabilityFunctions.c", "funcEmpty", 4, 0, LoCType.LOF},
            {"VariabilityFunctions.c", "funcDecl", 8, 3, LoCType.LOF},
            {"VariabilityFunctions.c", "funcHalfVar", 14, 3, LoCType.LOF},
            {"VariabilityFunctions.c", "funcVarNesting", 21, 5, LoCType.LOF},
            {"VariabilityFunctions.c", "conditionalFunction1", 31, 3, LoCType.LOF},
            {"VariabilityFunctions.c", "conditionalFunction2", 39, 5, LoCType.LOF},
            {"VariabilityFunctions.c", "funcCppElse", 48, 6, LoCType.LOF},
            
            // Percentage of Lines of Feature Code
            {"VariabilityFunctions.c", "funcEmpty", 4, 0, LoCType.PLOF},
            {"VariabilityFunctions.c", "funcDecl", 8, 3d / 5, LoCType.PLOF},
            {"VariabilityFunctions.c", "funcHalfVar", 14, 3d / 6, LoCType.PLOF},
            {"VariabilityFunctions.c", "funcVarNesting", 21, 5d / 8, LoCType.PLOF},
            {"VariabilityFunctions.c", "conditionalFunction1", 31, 3d / 5, LoCType.PLOF},
            {"VariabilityFunctions.c", "conditionalFunction2", 39, 5d / 7, LoCType.PLOF},
            {"VariabilityFunctions.c", "funcCppElse", 48, 6d / 9, LoCType.PLOF},
            
        });
    }
    
    @Override
    protected String getMetric() {
        return LoCMetric.class.getName();
    }
    
    /**
     * Executes the LoF tests.
     */
    @Test
    public void test() {
        super.test(SETTINGS.get(type));
    }
    
}
