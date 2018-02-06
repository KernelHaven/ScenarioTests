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
        Assert.assertEquals(0, 1.0, result.get("func").getValue());
    }
    
    /**
     * Tests a function with a if-else structure.
     */
    @Test
    public void testIfElseStatements() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "loc/NoVariabilityFunctions.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, null);
        Assert.assertEquals(0, 4.0, result.get("funcIfElse").getValue());
    }
    
    /**
     * Tests a function with a parameter, an if and a goto-label statement.
     */
    @Test
    public void testGotoLoopStatements() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "loc/NoVariabilityFunctions.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, null);
        Assert.assertEquals(0, 6.0, result.get("funcGoto").getValue());
    }

}
