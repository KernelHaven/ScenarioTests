package net.ssehub.kernel_haven.scenario_tests.metrics;

import java.io.File;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;

import net.ssehub.kernel_haven.metric_haven.MetricResult;
import net.ssehub.kernel_haven.metric_haven.code_metrics.FanInOut.FanType;
import net.ssehub.kernel_haven.metric_haven.metric_components.FanInOutMetric;
import net.ssehub.kernel_haven.test_utils.RunOnlyOnWinOrLinux;

/**
 * Tests metrics execution of Lines of Code metrics with srcML-Extractor.
 * @author El-Sharkawy
 *
 */
@RunWith(value = RunOnlyOnWinOrLinux.class)
public class FanInOutTests extends AbstractCodeMetricTests {

    private static final Properties FAN_IN_SETUP = new Properties();
    private static final Properties FAN_OUT_SETUP = new Properties();
    
    static {
        FAN_IN_SETUP.setProperty(FanInOutMetric.FAN_TYPE_SETTING.getKey(),
            FanType.CLASSICAL_FAN_IN_LOCALLY.name());
        FAN_OUT_SETUP.setProperty(FanInOutMetric.FAN_TYPE_SETTING.getKey(),
            FanType.CLASSICAL_FAN_OUT_LOCALLY.name());
    }
    
    @Override
    protected String getMetric() {
        return FanInOutMetric.class.getName();
    }
    
    /**
     * Tests <b>fan-in</b> on method which isn't called and does not call any function.
     */
    @Test
    public void funcNoCallTestFanIn() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "NoVariabilityFunctionsCalls.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, FAN_IN_SETUP);
        
        assertMetricResult(result.get("funcNoCall"), 4, 0);
    }
    
    /**
     * Tests <b>fan-out</b> on method which isn't called and does not call any function.
     */
    @Test
    public void funcNoCallTestFanOut() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "NoVariabilityFunctionsCalls.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, FAN_OUT_SETUP);
        
        assertMetricResult(result.get("funcNoCall"), 4, 0);
    }
    
    /**
     * Tests <b>fan-in</b> on method which calls other function, but isn't called.
     */
    @Test
    public void funcCallsOneTestFanIn() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "NoVariabilityFunctionsCalls.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, FAN_IN_SETUP);
        
        assertMetricResult(result.get("funcCallsOne"), 9, 0);
    }
    
    /**
     * Tests <b>fan-out</b> on method which calls other function, but isn't called.
     */
    @Test
    public void funcCallsOneTestFanOut() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "NoVariabilityFunctionsCalls.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, FAN_OUT_SETUP);
        
        assertMetricResult(result.get("funcCallsOne"), 9, 1);
    }
    
    /**
     * Tests <b>fan-in</b> on method which is called by one, but does not call other functions.
     */
    @Test
    public void funcCalledByOneTestFanIn() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "NoVariabilityFunctionsCalls.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, FAN_IN_SETUP);
        
        assertMetricResult(result.get("funcCalledByOne"), 13, 1);
    }
    
    /**
     * Tests <b>fan-out</b> on method which is called by one, but does not call other functions.
     */
    @Test
    public void funcCalledByOneTestFanOut() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "NoVariabilityFunctionsCalls.c");
        Map<String, MetricResult> result = runMetricAsMap(testfile, FAN_OUT_SETUP);
        
        assertMetricResult(result.get("funcCalledByOne"), 13, 0);
    }
}
