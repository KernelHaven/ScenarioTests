package net.ssehub.kernel_haven.scenario_tests.metrics;

import java.io.File;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;

import net.ssehub.kernel_haven.metric_haven.MetricResult;
import net.ssehub.kernel_haven.metric_haven.metric_components.CyclomaticComplexityMetric;
import net.ssehub.kernel_haven.scenario_tests.RunOnlyOnWinOrLinux;

/**
 * Tests metrics execution of classical McCabe metrics with srcML-Extractor.
 * @author El-Sharkawy
 *
 */
@RunWith(value = RunOnlyOnWinOrLinux.class)
public class McCabeTests extends AbstractCodeMetricTests {

    @Override
    protected String getMetric() {
        return CyclomaticComplexityMetric.class.getName();
    }
    
    /**
     * Basic test, test a method, which contains only one semicolon.
     */
    @Test
    public void testEmptyStatement() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "NoVariabilityFunctions.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, null);
        
        assertMetricResult(result.get("funcEmpty"), 4, 1);
    }
    
    /**
     * Basic test, test a method, which contains only one statement.
     */
    @Test
    public void testOneStatement() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "NoVariabilityFunctions.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, null);
        
        assertMetricResult(result.get("funcDecl"), 8, 1);
    }

    
    /**
     * Tests a function with a if-else structure.
     */
    @Test
    public void testIfElseStatements() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "NoVariabilityFunctions.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, null);

        assertMetricResult(result.get("funcIfElse"), 12, 2);
    }
    
    /**
     * Tests a function with a parameter, an if, and a goto-label statement.
     */
    @Test
    public void testGotoLoopStatements() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "NoVariabilityFunctions.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, null);
        
        assertMetricResult(result.get("funcGoto"), 20, 2);
    }
    
    /**
     * Tests a function with a parameter and a do-while loop.
     */
    @Test
    public void testDoWhileLoop() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "NoVariabilityFunctions.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, null);
        
        assertMetricResult(result.get("functDoWhile"), 35, 4);
    }
    
    /**
     * Tests a function with a parameter and a while loop.
     */
    @Test
    public void testWhileLoop() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "NoVariabilityFunctions.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, null);
        
        assertMetricResult(result.get("funcWhile"), 72, 4);
    }
    
    /**
     * Tests a function with a parameter and a for loop.
     */
    @Test
    public void testForLoop() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "NoVariabilityFunctions.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, null);
        
        assertMetricResult(result.get("funcFor"), 55, 4);
    }
    
    /**
     * Tests a function with a parameter and a switch statement.
     */
    @Test
    public void testSwitchStatement() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "NoVariabilityFunctions.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, null);
        
        assertMetricResult(result.get("funcSwitch"), 90, 4);
    }
    
    /**
     * Tests behavior on a real file: font_8x16 taken from the Linux kernel.
     */
    @Test
    public void testRealFont8x16() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "Real/Linux4.15/font_8x16.c");
        runMetric(testfile, null, true);
    }

}
