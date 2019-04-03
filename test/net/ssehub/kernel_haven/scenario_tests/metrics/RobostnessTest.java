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

import org.junit.Test;

import net.ssehub.kernel_haven.metric_haven.code_metrics.DLoC;

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
        runMetric(testfile, null, true, false, false);
    }

    @Override
    protected String getMetric() {
        // Metric is irrelevant as we do not verify the (uncounted) number of results.
        return DLoC.class.getName();
    }
}
