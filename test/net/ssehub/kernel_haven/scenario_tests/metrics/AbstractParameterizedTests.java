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
abstract class AbstractParameterizedTests extends AbstractCodeMetricTests {
    
    private static Map<Class<? extends AbstractParameterizedTests>, Map<String, MetricResult>> chachedResults
        = new HashMap<>();
    private static Map<Class<? extends AbstractParameterizedTests>, File> chachedFiles = new HashMap<>();
    
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
    AbstractParameterizedTests(String fileName, String testedFunctionName, int expectedLineNo,
        double expectedResultValue) {
        
        testCodeFile = new File(AbstractCodeMetricTests.TESTDATA, fileName);
        this.testedFunctionName = testedFunctionName;
        this.expectedLineNo = expectedLineNo;
        this.expectedResultValue = expectedResultValue;
    }

    
    /**
     * Tests the correct metric result.
     * @param properties Optional parameters (e.g., configuration parameters for the used metric).
     */
    public void test(Properties properties) {
        // Avoid running extractor multiple times on same file
        Map<String, MetricResult> result;
        if (testCodeFile.equals(chachedFiles.get(getClass())) && chachedResults.get(getClass()) != null) {
            // For the same sub lass and the same test file and result already exists.
            result = chachedResults.get(getClass());
        } else {
            // No cached result exist, parse the file
            result = runMetricAsMap(testCodeFile, properties);
            
            chachedFiles.put(getClass(), testCodeFile);
            chachedResults.put(getClass(), result);
        }
        
        assertMetricResult(result.get(testedFunctionName), expectedLineNo, expectedResultValue);
    }
}
