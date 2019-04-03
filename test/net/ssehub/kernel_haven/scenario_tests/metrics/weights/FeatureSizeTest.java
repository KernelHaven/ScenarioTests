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
package net.ssehub.kernel_haven.scenario_tests.metrics.weights;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import net.ssehub.kernel_haven.metric_haven.MetricResult;
import net.ssehub.kernel_haven.metric_haven.code_metrics.VariablesPerFunction;
import net.ssehub.kernel_haven.metric_haven.metric_components.config.FeatureSizeType;
import net.ssehub.kernel_haven.metric_haven.metric_components.config.MetricSettings;
import net.ssehub.kernel_haven.metric_haven.metric_components.weights.FeatureDistanceWeight;
import net.ssehub.kernel_haven.scenario_tests.metrics.AbstractParameterizedTests;
import net.ssehub.kernel_haven.test_utils.PseudoVariabilityExtractor;
import net.ssehub.kernel_haven.variability_model.VariabilityVariable;

/**
 * Tests the {@link FeatureDistanceWeight}.
 * @author bargfeldt
 *
 */
@RunWith(Parameterized.class)
public class FeatureSizeTest extends AbstractParameterizedTests {

    private static final Properties PROPERTIES_POSITIVE = new Properties();
    private static final Properties PROPERTIES_TOTAL = new Properties();
    
    static {
        PROPERTIES_POSITIVE.setProperty(MetricSettings.FEATURE_SIZE_MEASURING_SETTING.getKey(),
            FeatureSizeType.POSITIVE_SIZES.name());
        PROPERTIES_TOTAL.setProperty(MetricSettings.FEATURE_SIZE_MEASURING_SETTING.getKey(),
            FeatureSizeType.TOTAL_SIZES.name());
    }
    
    private boolean countPositivesOnly;
    
    /**
     * Retrieves values from {@link #getParameters()}, creates, and executes the test.
     * 
     * @param fileName The name of the file to be tested.
     * @param testedFunctionName The function to test
     * @param expectedLineNo The expected starting line number of the function
     * @param expectedResultValue The expected value of the metric to compute.
     */
    // CHECKSTYLE:OFF // too many arguments
    public FeatureSizeTest(String fileName, String testedFunctionName, int expectedLineNo,
        double expectedResultValue, boolean countPositivesOnly) {
    // CHECKSTYLE:ON
        super(fileName, testedFunctionName, expectedLineNo, expectedResultValue);
        this.countPositivesOnly = countPositivesOnly;
        
        // Configuration of variability model
        VariabilityVariable varA = new VariabilityVariable("FIRST_VAR", "bool");
        VariabilityVariable varB = new VariabilityVariable("SECOND_VAR", "bool");
        VariabilityVariable varC = new VariabilityVariable("THIRD_VAR", "bool");
        VariabilityVariable varD = new VariabilityVariable("FOURTH_VAR", "bool");
        VariabilityVariable varE = new VariabilityVariable("FITH_VAR", "bool");
        VariabilityVariable varF = new VariabilityVariable("SIXTH_VAR", "bool");
        VariabilityVariable varG = new VariabilityVariable("FOO", "bool");
        VariabilityVariable varH = new VariabilityVariable("BAR", "bool");
        VariabilityVariable varI = new VariabilityVariable("ANOTHER_VAR", "bool");
        VariabilityVariable varNestedA = new VariabilityVariable("VAR_A", "bool");
        VariabilityVariable varNestedB = new VariabilityVariable("VAR_B", "bool");
        VariabilityVariable varNestedC = new VariabilityVariable("VAR_C", "bool");
        VariabilityVariable varNestedD = new VariabilityVariable("VAR_D", "bool");
        
        PseudoVariabilityExtractor.configure(new File("mocked_varModel.dimacs"), varA, varB, varC, varD, varE,
            varF, varG, varH, varI, varNestedA, varNestedB, varNestedC, varNestedD);
    }
    
    @Override
    protected String getMetric() {
        return VariablesPerFunction.class.getName();
    }
    
    /**
     * Creates the parameters for this test.
     * 
     * @return The parameters of this test.
     */
    @Parameters(name = "FeatureSize-Weight: Positives {4} on {1}")
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
            {"FeatureSizes.c", "funcNoVariability", 2, 0, true},
            {"FeatureSizes.c", "funcNoVariability", 2, 0, false},
            {"FeatureSizes.c", "aPositiveVariable", 7, 1, true},
            {"FeatureSizes.c", "aPositiveVariable", 7, 1, false},
            {"FeatureSizes.c", "positiveAndNegativeVariable", 13, 1, true},
            {"FeatureSizes.c", "positiveAndNegativeVariable", 13, 3, false},
            {"FeatureSizes.c", "twoVariables", 22, 4, true},
            {"FeatureSizes.c", "twoVariables", 22, 4, false},
            {"FeatureSizes.c", "complex", 29, 3, true},
            {"FeatureSizes.c", "complex", 29, 4, false},
            {"FeatureSizes.c", "variableInTwoFunctions1", 38, 7, true},
            {"FeatureSizes.c", "variableInTwoFunctions1", 38, 18, false},
            {"FeatureSizes.c", "variableInTwoFunctions2", 50, 10, true},
            {"FeatureSizes.c", "variableInTwoFunctions2", 50, 21, false},
            {"FeatureSizes.c", "nestedFunction", 65, 12, true},
            {"FeatureSizes.c", "nestedFunction", 65, 16, false},
        });
    }
    
    /**
     * Executes the test.
     */
    @Test
    public void test() {
        if (countPositivesOnly) {
            super.test(PROPERTIES_POSITIVE);            
        } else {
            super.test(PROPERTIES_TOTAL);
        }
    }

    @Override
    protected List<MetricResult> runMetric(File file, Properties properties) {
        return runMetric(file, properties, false, true, true);
    }
    
    @Override
    protected Map<String, MetricResult> runMetricAsMap(File file, Properties properties) {
        
        return super.runMetricAsMap(file, properties, false, true, true);
    }
    
    @Override
    public Object getIdentifier() {
        return this.getClass().getCanonicalName() + countPositivesOnly;
    }

}
