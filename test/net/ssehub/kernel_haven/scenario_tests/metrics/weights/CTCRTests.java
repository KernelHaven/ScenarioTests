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
import net.ssehub.kernel_haven.scenario_tests.metrics.AbstractParameterizedTests;
import net.ssehub.kernel_haven.test_utils.PseudoVariabilityExtractor;
import net.ssehub.kernel_haven.variability_model.VariabilityModelDescriptor.Attribute;
import net.ssehub.kernel_haven.variability_model.VariabilityVariable;

/**
 * Tests metrics execution of variables per function (<b>internal</b>) metrics with srcML-Extractor.
 * @author El-Sharkawy
 *
 */
@RunWith(Parameterized.class)
public class CTCRTests extends AbstractParameterizedTests {

    private static final Properties VAR_INTERNAL = new Properties();
    
    private CTCRType ctcrType;
    
    /**
     * Retrieves values from {@link #getParameters()}, creates, and executes the test.
     * @param fileName The name of the file to be tested.
     * @param testedFunctionName The function to test
     * @param expectedLineNo The expected starting line number of the function
     * @param expectedResultValue The expected value of the metric to compute.
     * @param ctcrType The CTCR variation to test.
     */
    public CTCRTests(String fileName, String testedFunctionName, int expectedLineNo,
        double expectedResultValue, CTCRType ctcrType) {
        
        super(fileName, testedFunctionName, expectedLineNo, expectedResultValue);
        this.ctcrType = ctcrType;
    }
    
    /**
     * Initializes the properties and sets the variability model for {@link PseudoVariabilityExtractor}.
     */
    @BeforeClass
    public static void setup() {
        VAR_INTERNAL.setProperty(MetricSettings.VARIABLE_TYPE_SETTING.getKey(), VarType.INTERNAL.name());
        
        // Configuration of variability model
        VariabilityVariable varA = new VariabilityVariable("A", "bool");
        VariabilityVariable varB = new VariabilityVariable("B", "bool");
        VariabilityVariable varC = new VariabilityVariable("C", "bool");
        VariabilityVariable varD = new VariabilityVariable("D", "bool");
        // Outgoing: A = 3, B = 1 -> 4
        Set<VariabilityVariable> varAUses = new HashSet<>();
        varAUses.add(varB);
        varAUses.add(varC);
        varAUses.add(varD);
        varA.setVariablesUsedInConstraints(varAUses);
        Set<VariabilityVariable> varBUses = new HashSet<>();
        varBUses.add(varC);
        varB.setVariablesUsedInConstraints(varBUses);
        
        // Incoming: A = 0, B = 2 -> 2
        Set<VariabilityVariable> varBUsed = new HashSet<>();
        varBUsed.add(varA);
        varBUsed.add(varC);
        varB.setUsedInConstraintsOfOtherVariables(varBUsed);
        
        // ALL_CTCR: A = 3, B = 2 -> 5 (C is used twice for B)
        
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
    @Parameters(name = "CTCR-Weight: {4} on {1}")
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
            {"VariabilityFunctions.c", "funcVarNesting", 21, 4, CTCRType.OUTGOING_CONNECTIONS},
            {"VariabilityFunctions.c", "funcVarNesting", 21, 2, CTCRType.INCOMIG_CONNECTIONS},
            {"VariabilityFunctions.c", "funcVarNesting", 21, 5, CTCRType.ALL_CTCR},
            {"VariabilityFunctions.c", "funcVarNesting", 21, 2, CTCRType.NO_CTCR}
        });
    }
    
    /**
     * Executes the Internal Vars tests.
     */
    @Test
    public void test() {
        VAR_INTERNAL.setProperty(MetricSettings.CTCR_USAGE_SETTING.getKey(), ctcrType.name());
        super.test(VAR_INTERNAL);
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
        return this.getClass().getCanonicalName() + ctcrType.name();
    }
}

