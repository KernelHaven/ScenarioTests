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

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.ssehub.kernel_haven.metric_haven.MetricResult;

/**
 * Super class to test metrics in a <a href="https://github.com/junit-team/junit4/wiki/parameterized-tests">
 * parameterized test</a>.
 * @author El-Sharkawy
 *
 */
public abstract class AbstractParameterizedTests extends AbstractCodeMetricTests {
    
    private static Map<Object, Map<String, MetricResult>> chachedResults
        = new HashMap<>();
    private static Map<Object, File> chachedFiles = new HashMap<>();
    private static Properties lastConfig;
    
    private File testCodeFile;
    private String testedFunctionName;
    
    private int expectedLineNo;
    private double expectedResultValue;
    
    /**
     * Creates and executes a metric test.
     * @param fileName The name of the file to be tested.
     * @param testedFunctionName The function to test
     * @param expectedLineNo The expected starting line number of the function
     * @param expectedResultValue The expected value of the metric to compute.
     */
    public AbstractParameterizedTests(String fileName, String testedFunctionName, int expectedLineNo,
        double expectedResultValue) {
        
        testCodeFile = new File(AbstractCodeMetricTests.TESTDATA, fileName);
        this.testedFunctionName = testedFunctionName;
        this.expectedLineNo = expectedLineNo;
        this.expectedResultValue = expectedResultValue;
    }

    /**
     * Returns the File representation of the file to be tested.
     * @return The measured test file.
     */
    protected File getTestFile() {
        return testCodeFile;
    }
    
    /**
     * Tests the correct metric result.
     * @param properties Optional parameters (e.g., configuration parameters for the used metric).
     */
    public void test(Properties properties) {
        // Avoid running extractor multiple times on same file
        Map<String, MetricResult> result;
        if (testCodeFile.equals(chachedFiles.get(getIdentifier())) && chachedResults.get(getIdentifier()) != null
            && lastConfig == properties) {
            // For the same sub class and the same test file and result already exists.
            result = chachedResults.get(getIdentifier());
        } else {
            // No cached result exist, parse the file
            result = runMetricAsMap(testCodeFile, properties);
            
            chachedFiles.put(getIdentifier(), testCodeFile);
            chachedResults.put(getIdentifier(), result);
            lastConfig = properties;
        }
        
        assertMetricResult(result.get(testedFunctionName), expectedLineNo, expectedResultValue);
    }
    
    /**
     * Returns a unique identifier for the test case. Whenever the identifier returns the same object, cached
     * metric results may be reused to avoid multiple parsing and visiting of functions as different elements of the
     * same metric result are used in tests belonging to the same identifier.
     * @return A unique identifier, specifying when a set of already collected metric results can be reused.
     */
    protected Object getIdentifier() {
        return this.getClass();
    }
}
