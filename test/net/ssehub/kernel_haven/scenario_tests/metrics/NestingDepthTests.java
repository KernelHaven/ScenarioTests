package net.ssehub.kernel_haven.scenario_tests.metrics;

import java.io.File;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;

import net.ssehub.kernel_haven.metric_haven.MetricResult;
import net.ssehub.kernel_haven.metric_haven.metric_components.NestingDepthMetric;
import net.ssehub.kernel_haven.scenario_tests.RunOnlyOnWinOrLinux;

/**
 * Tests metrics execution of Lines of Code metrics with srcML-Extractor.
 * @author El-Sharkawy
 *
 */
@RunWith(value = RunOnlyOnWinOrLinux.class)
public class NestingDepthTests extends AbstractCodeMetricTests {

    private static final Properties ND_MAX_SETUP = new Properties();
    private static final Properties ND_AVG_SETUP = new Properties();
    
    static {
        ND_MAX_SETUP.setProperty(NestingDepthMetric.ND_TYPE_SETTING.getKey(),
            NestingDepthMetric.NDType.CLASSIC_ND_MAX.name());
        ND_AVG_SETUP.setProperty(NestingDepthMetric.ND_TYPE_SETTING.getKey(),
                NestingDepthMetric.NDType.CLASSIC_ND_AVG.name());
    }
    
    @Override
    protected String getMetric() {
        return NestingDepthMetric.class.getName();
    }
    
    /**
     * Basic test, test a method, which contains only one semicolon. <br/>
     * ND type: <b>MAX</b>
     */
    @Test
    public void testEmptyStatementMax() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "NoVariabilityFunctions.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, ND_MAX_SETUP);
        
        assertMetricResult(result.get("funcEmpty"), 4, 1);
    }
    
    /**
     * Basic test, test a method, which contains only one semicolon. <br/>
     * ND type: <b>Average</b>
     */
    @Test
    public void testEmptyStatementAvg() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "NoVariabilityFunctions.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, ND_AVG_SETUP);
        
        assertMetricResult(result.get("funcEmpty"), 4, 1);
    }
    
    /**
     * Basic test, test a method, which contains only one statement. <br/>
     * ND type: <b>MAX</b>
     */
    @Test
    public void testOneStatementMax() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "NoVariabilityFunctions.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, ND_MAX_SETUP);
        
        assertMetricResult(result.get("funcDecl"), 8, 1);
    }
    
    /**
     * Basic test, test a method, which contains only one statement. <br/>
     * ND type: <b>Average</b>
     */
    @Test
    public void testOneStatementAvg() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "NoVariabilityFunctions.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, ND_AVG_SETUP);
        
        assertMetricResult(result.get("funcDecl"), 8, 1);
    }
    
    /**
     * Tests a function with a if-else structure. <br/>
     * ND type: <b>MAX</b>
     */
    @Test
    public void testIfElseStatementsMax() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "NoVariabilityFunctions.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, ND_MAX_SETUP);

        assertMetricResult(result.get("funcIfElse"), 12, 2);
    }
    
    /**
     * Tests a function with a if-else structure. <br/>
     * ND type: <b>Average</b>
     */
    @Test
    public void testIfElseStatementsAvg() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "NoVariabilityFunctions.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, ND_AVG_SETUP);
        
        assertMetricResult(result.get("funcIfElse"), 12, 2);
    }
    
    /**
     * Tests a function with a parameter, an if, and a goto-label statement. <br/>
     * ND type: <b>MAX</b>
     */
    @Test
    public void testGotoLoopStatementsMax() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "NoVariabilityFunctions.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, ND_MAX_SETUP);
        
        assertMetricResult(result.get("funcGoto"), 20, 2);
    }
    
    /**
     * Tests a function with a parameter, an if, and a goto-label statement. <br/>
     * ND type: <b>Average</b>
     */
    @Test
    public void testGotoLoopStatementsAvg() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "NoVariabilityFunctions.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, ND_AVG_SETUP);
        
        assertMetricResult(result.get("funcGoto"), 20, 6.0 / 5);
    }
    
    /**
     * Tests a function with a parameter and a do-while loop. <br/>
     * ND type: <b>MAX</b>
     */
    @Test
    public void testDoWhileLoopMax() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "NoVariabilityFunctions.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, ND_MAX_SETUP);
        
        assertMetricResult(result.get("functDoWhile"), 35, 3);
    }
    
    /**
     * Tests a function with a parameter and a do-while loop. <br/>
     * ND type: <b>Average</b>
     */
    @Test
    public void testDoWhileLoopAvg() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "NoVariabilityFunctions.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, ND_AVG_SETUP);
        
        assertMetricResult(result.get("functDoWhile"), 35, 2);
    }
    
    /**
     * Tests a function with a parameter and a while loop. <br/>
     * ND type: <b>MAX</b>
     */
    @Test
    public void testWhileLoopMax() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "NoVariabilityFunctions.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, ND_MAX_SETUP);
        
        assertMetricResult(result.get("funcWhile"), 72, 3);
    }
    
    /**
     * Tests a function with a parameter and a while loop. <br/>
     * ND type: <b>Average</b>
     */
    @Test
    public void testWhileLoopAvg() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "NoVariabilityFunctions.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, ND_AVG_SETUP);
        
        assertMetricResult(result.get("funcWhile"), 72, 2);
    }
    
    /**
     * Tests a function with a parameter and a for loop. <br/>
     * ND type: <b>MAX</b>
     */
    @Test
    public void testForLoopMax() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "NoVariabilityFunctions.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, ND_MAX_SETUP);
        
        assertMetricResult(result.get("funcFor"), 55, 3);
    }
    
    /**
     * Tests a function with a parameter and a for loop. <br/>
     * ND type: <b>Average</b>
     */
    @Test
    public void testForLoopAvg() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "NoVariabilityFunctions.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, ND_AVG_SETUP);
        
        assertMetricResult(result.get("funcFor"), 55, 2);
    }
    
    /**
     * Tests a function with a parameter and a switch statement. <br/>
     * ND type: <b>MAX</b>
     */
    @Test
    public void testSwitchStatementMax() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "NoVariabilityFunctions.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, ND_MAX_SETUP);
        
        assertMetricResult(result.get("funcSwitch"), 90, 2);
    }
    
    /**
     * Tests a function with a parameter and a switch statement. <br/>
     * ND type: <b>Average</b>
     */
    @Test
    public void testSwitchStatementAvg() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "NoVariabilityFunctions.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, ND_AVG_SETUP);
        
        assertMetricResult(result.get("funcSwitch"), 90, 12.0 / 7);
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
