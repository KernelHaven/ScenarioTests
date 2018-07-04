package net.ssehub.kernel_haven.scenario_tests.metrics;

import java.io.File;

import org.junit.Test;

import net.ssehub.kernel_haven.metric_haven.metric_components.DLoC;

/**
 * Tests only that Extractor and analysis won't crash in certain situations.
 * @author El-Sharkawy
 *
 */
public class RobostnessTest extends AbstractCodeMetricTests {

    /**
     * Tests behavior on a real file: font_8x16 taken from the Linux kernel.
     */
    @Test
    public void testRealFont8x16() {
        File testfile = new File(AbstractCodeMetricTests.TESTDATA, "Real/Linux4.15/font_8x16.c");
        runMetric(testfile, null, true, false);
    }

    @Override
    protected String getMetric() {
        // Metric is irrelevant as we do not verify the (uncounted) number of results.
        return DLoC.class.getName();
    }
}
