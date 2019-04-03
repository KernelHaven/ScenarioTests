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
import net.ssehub.kernel_haven.metric_haven.code_metrics.VariablesPerFunction.VarType;
import net.ssehub.kernel_haven.metric_haven.metric_components.config.MetricSettings;
import net.ssehub.kernel_haven.metric_haven.metric_components.config.SDType;
import net.ssehub.kernel_haven.metric_haven.metric_components.config.VariabilityTypeMeasureType;
import net.ssehub.kernel_haven.scenario_tests.metrics.AbstractParameterizedTests;
import net.ssehub.kernel_haven.test_utils.PseudoVariabilityExtractor;
import net.ssehub.kernel_haven.variability_model.VariabilityVariable;

/**
 * Tests the {@link VariabilityTypeMeasureType} weight using the {@link VariablesPerFunctionMetric} (internal).
 *
 * @author Adam
 */
@RunWith(Parameterized.class)
public class ScatteringWeightTest extends AbstractParameterizedTests {

    private static final Properties PROPERTIES = new Properties();
    
    static {
        PROPERTIES.setProperty(MetricSettings.VARIABLE_TYPE_SETTING.getKey(), VarType.INTERNAL.name());
        
        // override code.extractor.files to consider more than 1 file
        PROPERTIES.setProperty("code.extractor.files", "sd_1.c, sd_2.c");
    }
    
    private SDType type;
    
    /**
     * Retrieves values from {@link #getParameters()}, creates, and executes the test.
     * 
     * @param fileName The name of the file to be tested.
     * @param testedFunctionName The function to test
     * @param expectedLineNo The expected starting line number of the function
     * @param expectedResultValue The expected value of the metric to compute.
     */
    // CHECKSTYLE:OFF // too many arguments
    public ScatteringWeightTest(String fileName, String testedFunctionName, int expectedLineNo,
        double expectedResultValue, SDType type) {
    // CHECKSTYLE:ON
        super(fileName, testedFunctionName, expectedLineNo, expectedResultValue);
        
        this.type = type;
        
        // Configuration of variability model
        VariabilityVariable varA = new VariabilityVariable("A", "bool");
        VariabilityVariable varB = new VariabilityVariable("B", "bool");
        
        PseudoVariabilityExtractor.configure(new File("mocked_varModel.dimacs"), varA, varB);
        
        PROPERTIES.setProperty(MetricSettings.SCATTERING_DEGREE_USAGE_SETTING.getKey(), type.name());
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
    @Parameters(name = "ScatteringDegree ({4}) Weight")
    public static Collection<Object[]> getParameters() {
        // A is used 3 times in sd_1.c (one of which is in func (internal)), and 2 times in sd_2.c
        
        return Arrays.asList(new Object[][] {
            {"sd_1.c", "func", 3, 1, SDType.NO_SCATTERING},
            {"sd_1.c", "func", 3, 3 + 2, SDType.SD_VP},
            {"sd_1.c", "func", 3, 2, SDType.SD_FILE},
        });
    }
    
    /**
     * Executes the test.
     */
    @Test
    public void test() {
        super.test(PROPERTIES);
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
        return this.getClass().getCanonicalName() + type;
    }

}
