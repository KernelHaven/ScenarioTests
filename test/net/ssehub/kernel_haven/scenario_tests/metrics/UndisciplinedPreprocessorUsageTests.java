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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import net.ssehub.kernel_haven.metric_haven.code_metrics.UndisciplinedPreprocessorUsage;

/**
 * Tests metrics execution of {@link UndisciplinedPreprocessorUsage} metrics with srcML-Extractor.
 * @author El-Sharkawy
 *
 */
@RunWith(Parameterized.class)
public class UndisciplinedPreprocessorUsageTests extends AbstractParameterizedTests {

    /**
     * Retrieves values from {@link #getParameters()}, creates, and executes the test.
     * @param fileName The name of the file to be tested.
     * @param testedFunctionName The function to test
     * @param expectedLineNo The expected starting line number of the function
     * @param expectedResultValue The expected value of the metric to compute.
     */
    public UndisciplinedPreprocessorUsageTests(String fileName, String testedFunctionName,
        int expectedLineNo, double expectedResultValue) {
        
        super(fileName, testedFunctionName, expectedLineNo, expectedResultValue);
    }
    
    /**
     * Creates the parameters for this test.
     * 
     * @return The parameters of this test.
     */
    @Parameters(name = "UndisciplinedCPP: {1}")
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
            // No CPP -> All should be 0
            {"NoVariabilityFunctions.c", "funcEmpty", 4, 0},
            {"NoVariabilityFunctions.c", "funcDecl", 8, 0},
            {"NoVariabilityFunctions.c", "funcIfElse", 12, 0},
            {"NoVariabilityFunctions.c", "funcGoto", 20, 0},
            {"NoVariabilityFunctions.c", "functDoWhile", 35, 0},
            {"NoVariabilityFunctions.c", "funcWhile", 72, 0},
            {"NoVariabilityFunctions.c", "funcFor", 55, 0},
            {"NoVariabilityFunctions.c", "funcSwitch", 90, 0},
            
            // Disciplined CPP usage -> All should be 0
            {"VariabilityFunctions.c", "funcEmpty", 4, 0},
            {"VariabilityFunctions.c", "funcDecl", 8, 0},
            {"VariabilityFunctions.c", "funcHalfVar", 14, 0},
            {"VariabilityFunctions.c", "funcVarNesting", 21, 0},
            
            // Undisciplined CPP usage
            {"UndisciplinedCPPs.c", "condLoopSplitedInOnePart", 1, 1},
            {"UndisciplinedCPPs.c", "condLoopSplitedInTwoParts", 10, 2},
            {"UndisciplinedCPPs.c", "condIfSplitedInOnePart", 20, 1},
            {"UndisciplinedCPPs.c", "condIfExpression", 29, 1},
//            {"UndisciplinedCPPs.c", "condTypeOfDeclaration", 42, 2},      // ErrorElement
//            {"UndisciplinedCPPs.c", "fig1_bad", 0, 2},                    // Currently not parseable
//            {"UndisciplinedCPPs.c", "fig1_good", 0, 0},                   // Currently not parseable
            {"UndisciplinedCPPs.c", "ref1_bad", 0, 2}, 
            {"UndisciplinedCPPs.c", "ref1_good", 0, 0}, 
            {"UndisciplinedCPPs.c", "ref2_bad", 0, 1}, 
            {"UndisciplinedCPPs.c", "ref2_good", 0, 0}, 
//            {"UndisciplinedCPPs.c", "ref3_bad", 0, 2},           // next function will be nested inside this function
//            {"UndisciplinedCPPs.c", "ref3_good", 0, 0}, 
            {"UndisciplinedCPPs.c", "ref4_bad", 0, 1},
            {"UndisciplinedCPPs.c", "ref4_good", 0, 0},
            {"UndisciplinedCPPs.c", "ref5_bad", 0, 1},
            {"UndisciplinedCPPs.c", "ref5_good", 0, 0},
            {"UndisciplinedCPPs.c", "ref6_bad", 0, 1},
            {"UndisciplinedCPPs.c", "ref6_good", 0, 0},
            
        });
    }
    
    @Override
    protected String getMetric() {
        return UndisciplinedPreprocessorUsage.class.getName();
    }
    
    /**
     * Executes the LoC tests.
     */
    @Test
    public void test() {
        super.test(null);
    }
    
}
