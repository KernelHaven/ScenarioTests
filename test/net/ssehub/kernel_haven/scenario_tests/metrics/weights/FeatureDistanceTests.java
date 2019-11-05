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
import net.ssehub.kernel_haven.metric_haven.metric_components.config.FeatureDistanceType;
import net.ssehub.kernel_haven.metric_haven.metric_components.config.MetricSettings;
import net.ssehub.kernel_haven.scenario_tests.metrics.AbstractCodeMetricTests;
import net.ssehub.kernel_haven.scenario_tests.metrics.AbstractParameterizedTests;
import net.ssehub.kernel_haven.test_utils.PseudoVariabilityExtractor;
import net.ssehub.kernel_haven.variability_model.SourceLocation;
import net.ssehub.kernel_haven.variability_model.VariabilityModelDescriptor.Attribute;
import net.ssehub.kernel_haven.variability_model.VariabilityVariable;

/**
 * Tests metrics execution of variables per function (<b>internal</b>) metrics with srcML-Extractor.
 * @author El-Sharkawy
 *
 */
@RunWith(Parameterized.class)
public class FeatureDistanceTests extends AbstractParameterizedTests {

    private static final Properties DISTANCE_SETTING = new Properties();
    
    static {
        DISTANCE_SETTING.setProperty(MetricSettings.VARIABLE_TYPE_SETTING.getKey(), VarType.INTERNAL.name());
        DISTANCE_SETTING.setProperty(MetricSettings.LOCATION_DISTANCE_SETTING.getKey(),
            FeatureDistanceType.SHORTEST_DISTANCE.name());
        DISTANCE_SETTING.setProperty("source_tree", AbstractCodeMetricTests.TESTDATA.getAbsolutePath());  
    }
    
    /**
     * Specification of variables to use for the test.
     * @author El-Sharkawy
     */
    private static class VarSpec {
        private String variableName;
        private File path;
        
        /**
         * Sole constructor.
         * @param variableName The name of the variable.
         * @param path The folder where the variable should be specified.
         */
        private VarSpec(String variableName, File path) {
            this.variableName = variableName;
            this.path = path;
        }
    }
    
    private Properties prop;
    
    /**
     * Retrieves values from {@link #getParameters()}, creates, and executes the test.
     * @param fileName The name of the file to be tested.
     * @param testedFunctionName The function to test
     * @param expectedLineNo The expected starting line number of the function
     * @param expectedResultValue The expected value of the metric to compute.
     * @param variables Specification of variables to use for the test.
     */
    public FeatureDistanceTests(String fileName, String testedFunctionName, int expectedLineNo,
        double expectedResultValue, VarSpec[] variables) {
        
        super(fileName, testedFunctionName, expectedLineNo, expectedResultValue);
        
        // Configuration of variability model
        VariabilityVariable[] vars = new VariabilityVariable[variables.length];
        for (int i = 0; i < variables.length; i++) {
            vars[i] = new VariabilityVariable(variables[i].variableName, "bool");
            SourceLocation location = new SourceLocation(new File(variables[i].path, variables[i].variableName
                + ".varModel"), 1);
            vars[i].addLocation(location);
            
            System.out.println(location.getSource().getAbsolutePath());
        }
        
        prop = new Properties();
        prop.putAll(DISTANCE_SETTING);
        prop.setProperty("code.extractor.files", fileName);
        
        PseudoVariabilityExtractor.configure(new File("mocked_varModel.dimacs"), vars);
        PseudoVariabilityExtractor.setAttributes(Attribute.SOURCE_LOCATIONS);
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
    @Parameters(name = "Feature Distance Weight: {1}")
    public static Collection<Object[]> getParameters() {
        File folder = new File("VariabilityFunctions.c").getParentFile();
       
        return Arrays.asList(new Object[][] {
            {"VariabilityFunctions.c", "funcVarNesting", 21, 1, new VarSpec[] {
                // Variable A's distance is 0 (same folder as code file)
                new VarSpec("A", folder),
                // Variable B's distance is 1 (sub folder of code folder)
                new VarSpec("B", new File("subFolder"))
                }
            },
            
            {"subFolderForDistance/VariabilityFunctions.c", "funcVarNesting", 21, 3, new VarSpec[] {
                // Variable A's distance is 1 (parent folder of code folder)
                new VarSpec("A", folder),
                // Variable B's distance is 2 (parallel to code folder (one up and down to another folder))
                new VarSpec("B", new File("subFolder"))
                }
            }
        });
    }
    
    /**
     * Executes the Internal Vars tests.
     */
    @Test
    public void test() {
        super.test(prop);
    }

    @Override
    protected List<MetricResult> runMetric(File file, Properties properties) {
        
        return runMetric(file, properties, false, true, true);
    }
    
    @Override
    protected Map<String, MetricResult> runMetricAsMap(File file, Properties properties) {
        return super.runMetricAsMap(file, properties, false, true, true);
    }
}

