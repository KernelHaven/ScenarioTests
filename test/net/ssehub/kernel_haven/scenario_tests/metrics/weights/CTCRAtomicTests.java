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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import net.ssehub.kernel_haven.metric_haven.MetricResult;
import net.ssehub.kernel_haven.metric_haven.code_metrics.VariablesPerFunction;
import net.ssehub.kernel_haven.metric_haven.code_metrics.VariablesPerFunction.VarType;
import net.ssehub.kernel_haven.metric_haven.metric_components.config.CTCRType;
import net.ssehub.kernel_haven.metric_haven.metric_components.config.MetricSettings;
import net.ssehub.kernel_haven.metric_haven.metric_components.weights.CtcrWeight;
import net.ssehub.kernel_haven.scenario_tests.metrics.AbstractParameterizedTests;
import net.ssehub.kernel_haven.test_utils.PseudoVariabilityExtractor;
import net.ssehub.kernel_haven.variability_model.VariabilityModelDescriptor.Attribute;
import net.ssehub.kernel_haven.variability_model.VariabilityVariable;

/**
 * Additional tests to {@link CTCRTests}, which test the {@link CtcrWeight} with more atomic measures.
 * @author El-Sharkawy
 *
 */
@RunWith(Parameterized.class)
public class CTCRAtomicTests extends AbstractParameterizedTests {

    private static final Properties IN = new Properties();
    private static final Properties OUT = new Properties();
    private static final Properties ALL = new Properties();
    private static final Properties NO_CTCR = new Properties();

    private Properties props;
    
    /**
     * Retrieves values from {@link #getParameters()}, creates, and executes the test.
     * @param testedFunctionName The function to test
     * @param expectedLineNo The expected starting line number of the function
     * @param expectedResultValue The expected value of the metric to compute.
     * @param props The properties to use for the test (one from above)
     * @param type The name of the chosen weight setting (only for naming the test case, won't affect the execution)
     */
    public CTCRAtomicTests(String testedFunctionName, int expectedLineNo,
        double expectedResultValue, Properties props, String type) {
        
        super("CTCRAtomicTests.c", testedFunctionName, expectedLineNo, expectedResultValue);
        this.props = props;
    }
    
    /**
     * Initializes the properties and sets the variability model for {@link PseudoVariabilityExtractor}.
     */
    @BeforeClass
    public static void setup() {
        IN.setProperty(MetricSettings.VARIABLE_TYPE_SETTING.getKey(), VarType.INTERNAL.name());
        IN.setProperty(MetricSettings.CTCR_USAGE_SETTING.getKey(), CTCRType.INCOMIG_CONNECTIONS.name());
        
        OUT.setProperty(MetricSettings.VARIABLE_TYPE_SETTING.getKey(), VarType.INTERNAL.name());
        OUT.setProperty(MetricSettings.CTCR_USAGE_SETTING.getKey(), CTCRType.OUTGOING_CONNECTIONS.name());
        
        ALL.setProperty(MetricSettings.VARIABLE_TYPE_SETTING.getKey(), VarType.INTERNAL.name());
        ALL.setProperty(MetricSettings.CTCR_USAGE_SETTING.getKey(), CTCRType.ALL_CTCR.name());
        
        NO_CTCR.setProperty(MetricSettings.VARIABLE_TYPE_SETTING.getKey(), VarType.INTERNAL.name());
        NO_CTCR.setProperty(MetricSettings.CTCR_USAGE_SETTING.getKey(), CTCRType.NO_CTCR.name());
        
        // Configuration of variability model
        VariabilityVariable varA = new VariabilityVariable("A", "bool");
        VariabilityVariable varB = new VariabilityVariable("B", "bool");
        VariabilityVariable varC = new VariabilityVariable("C", "bool");
        VariabilityVariable varD = new VariabilityVariable("D", "bool");
        
        // A -> {B, C, D}
        // A <- {}
        Set<VariabilityVariable> usedVars = new HashSet<>();
        usedVars.add(varB);
        usedVars.add(varC);
        usedVars.add(varD);
        varA.setVariablesUsedInConstraints(usedVars);
        
        // B -> {C, D}
        // B <- {A}
        usedVars = new HashSet<>();
        usedVars.add(varC);
        usedVars.add(varD);
        varB.setVariablesUsedInConstraints(usedVars);
        usedVars = new HashSet<>();
        usedVars.add(varA);
        varB.setUsedInConstraintsOfOtherVariables(usedVars);
        
        // C -> {}
        // C <- {A, B}
        usedVars = new HashSet<>();
        usedVars.add(varA);
        usedVars.add(varB);
        varC.setUsedInConstraintsOfOtherVariables(usedVars);
       
        // D -> {B}
        // D <- {A, B}
        usedVars = new HashSet<>();
        usedVars.add(varB);
        varD.setVariablesUsedInConstraints(usedVars);
        usedVars = new HashSet<>();
        usedVars.add(varA);
        usedVars.add(varB);
        varD.setUsedInConstraintsOfOtherVariables(usedVars);
        
        PseudoVariabilityExtractor.configure(new File("mocked_varModel.dimacs"), varA, varB, varC, varD);
        PseudoVariabilityExtractor.setAttributes(Attribute.CONSTRAINT_USAGE);
        
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
    @Parameters(name = "CTCR-Weight: {4} on {0}")
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
            // Incoming connections
            {"testFeatureA", 1,  0, IN, "Incoming"},
            {"testFeatureB", 7,  1, IN, "Incoming"},
            {"testFeatureC", 13, 2, IN, "Incoming"},
            {"testFeatureD", 19, 2, IN, "Incoming"},
            
            // Outgoing connections
            {"testFeatureA", 1,  3, OUT, "Outgoing"},
            {"testFeatureB", 7,  2, OUT, "Outgoing"},
            {"testFeatureC", 13, 0, OUT, "Outgoing"},
            {"testFeatureD", 19, 1, OUT, "Outgoing"},
            
            // All connections
            {"testFeatureA", 1,  3, ALL, "All Connections"},
            {"testFeatureB", 7,  3, ALL, "All Connections"},
            {"testFeatureC", 13, 2, ALL, "All Connections"},
            {"testFeatureD", 19, 2, ALL, "All Connections"},
            
            // Disabled CTCR
            {"testFeatureA", 1,  1, NO_CTCR, "Disabled CTCR"},
            {"testFeatureB", 7,  1, NO_CTCR, "Disabled CTCR"},
            {"testFeatureC", 13, 1, NO_CTCR, "Disabled CTCR"},
            {"testFeatureD", 19, 1, NO_CTCR, "Disabled CTCR"},
        });
    }
    
    /**
     * Executes the Internal Vars tests.
     */
    @Test
    public void test() {
        super.test(props);
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

