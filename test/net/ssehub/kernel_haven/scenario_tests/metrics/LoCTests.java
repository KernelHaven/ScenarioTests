package net.ssehub.kernel_haven.scenario_tests.metrics;

import java.io.File;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import net.ssehub.kernel_haven.metric_haven.MetricResult;
import net.ssehub.kernel_haven.metric_haven.metric_components.DLoC;
import net.ssehub.kernel_haven.scenario_tests.RunOnlyOnWinOrLinux;

/**
 * Tests metrics execution of Lines of Code metrics with srcML-Extractor.
 * @author El-Sharkawy
 *
 */
@RunWith(value = RunOnlyOnWinOrLinux.class)
public class LoCTests extends AbstractCodeMetricTests {

    @Override
    protected String getMetric() {
        return DLoC.class.getName();
    }
    
    /**
     * Basic test, test a method, which contains only one semicolon.
     */
    @Test
    public void testEmptyStatement() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "loc/LoC_EmptyFunction.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, null);
        Assert.assertEquals(1.0, result.get("func").getValue(), 0);
    }
    
    /**
     * Tests a function with a if-else structure.
     */
    @Test
    public void testIfElseStatements() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "loc/NoVariabilityFunctions.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, null);
        Assert.assertEquals(4.0, result.get("funcIfElse").getValue(), 0);
    }
    
    /**
     * Tests a function with a parameter, an if, and a goto-label statement.
     */
    @Test
    public void testGotoLoopStatements() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "loc/NoVariabilityFunctions.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, null);
        Assert.assertEquals(6.0, result.get("funcGoto").getValue(), 0);
    }
    
    /**
     * Tests a function with a parameter and a do-while loop.
     */
    @Test
    public void testDoWhileLoop() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "loc/NoVariabilityFunctions.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, null);
        Assert.assertEquals(9.0, result.get("functDoWhile").getValue(), 0);
    }
    
    /**
     * Tests a function with a parameter and a while loop.
     */
    @Test
    public void testWhileLoop() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "loc/NoVariabilityFunctions.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, null);
        Assert.assertEquals(9.0, result.get("funcWhile").getValue(), 0);
    }
    
    /**
     * Tests a function with a parameter and a for loop.
     */
    @Test
    public void testForLoop() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "loc/NoVariabilityFunctions.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, null);
        Assert.assertEquals(8.0, result.get("funcFor").getValue(), 0);
    }
    
    /**
     * Tests a function with a parameter and a switch statement.
     */
    @Test
    public void testSwitchStatement() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "loc/NoVariabilityFunctions.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, null);
        Assert.assertEquals(12.0, result.get("funcSwitch").getValue(), 0);
    }

}
