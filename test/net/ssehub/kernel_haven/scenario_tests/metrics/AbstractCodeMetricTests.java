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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;

import net.ssehub.kernel_haven.PipelineConfigurator;
import net.ssehub.kernel_haven.SetUpException;
import net.ssehub.kernel_haven.build_model.EmptyBuildModelExtractor;
import net.ssehub.kernel_haven.config.Configuration;
import net.ssehub.kernel_haven.config.DefaultSettings;
import net.ssehub.kernel_haven.metric_haven.MetricResult;
import net.ssehub.kernel_haven.metric_haven.filter_components.CodeFunctionFilter;
import net.ssehub.kernel_haven.metric_haven.filter_components.FunctionMapCreator;
import net.ssehub.kernel_haven.metric_haven.filter_components.feature_size.FeatureSizeEstimator;
import net.ssehub.kernel_haven.metric_haven.filter_components.scattering_degree.VariabilityCounter;
import net.ssehub.kernel_haven.metric_haven.metric_components.CodeMetricsRunner;
import net.ssehub.kernel_haven.metric_haven.metric_components.config.MetricSettings;
import net.ssehub.kernel_haven.metric_haven.metric_components.weights.WeigthsCache;
import net.ssehub.kernel_haven.metric_haven.multi_results.MultiMetricResult;
import net.ssehub.kernel_haven.scenario_tests.AllTests;
import net.ssehub.kernel_haven.srcml.SrcMLExtractor;
import net.ssehub.kernel_haven.test_utils.MemoryTableCollection;
import net.ssehub.kernel_haven.test_utils.MemoryTableWriter;
import net.ssehub.kernel_haven.test_utils.PseudoVariabilityExtractor;
import net.ssehub.kernel_haven.test_utils.TestConfiguration;
import net.ssehub.kernel_haven.util.io.TableCollectionWriterFactory;
import net.ssehub.kernel_haven.variability_model.EmptyVariabilityModelExtractor;

/**
 * Abstract test suite to test code metrics.
 * @author El-Sharkawy
 *
 */
public abstract class AbstractCodeMetricTests {
    
    public static final File TESTDATA = new File(AllTests.TESTDATA, "Metrics");
    public static final File RESOURCE_DIR = new File(TESTDATA, "resources");
    private static final File GEN_FOLDER = new File(TESTDATA, "generated_tmp_Results");
    
    /**
     * Initializes the resource directory.
     */
    @BeforeClass
    public static void initResDir() {
        clearDirectory(RESOURCE_DIR, "resource");
    }
    
    /**
     * Creates resources which are needed for each test to be fresh.
     * <ul>
     *     <li>Output directory</li>
     * </ul>
     */
    @Before
    public void setUp() {
        WeigthsCache.INSTANCE.clear();
        MemoryTableWriter.clear();
        clearDirectory(GEN_FOLDER, "ouput");
    }

    /**
     * Helper for set up methods: Clears a directory.
     * @param folder The folder to be cleaned up.
     * @param type A description if something fails.
     */
    private static void clearDirectory(File folder, String type) {
        if (folder.exists()) {
            try {
                Files.walk(folder.toPath()).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
            } catch (IOException e) {
                Assert.fail("Could not clear " + type + " directory: " + folder.getAbsolutePath()
                    + ", cause: " + e.getMessage());
            }
        }
        folder.mkdir();
    }

    /**
     * Returns the fully qualified class name of the extractor to use. Per default srcML will be used.
     * @return The fully qualified class name of the extractor to use.
     */
    protected String getExtractor() {
        return SrcMLExtractor.class.getName();
    }
    
    /**
     * Returns the fully qualified class name of the metric component (analysis class) to use. 
     * @return The fully qualified class name of the metric component (analysis class) to use.
     */
    protected abstract String getMetric();
    
    /**
     * Runs the specified metric of {@link #getMetric()} on the specified file.
     * @param file The model file to parse (its folder will be treated as source_tree root).
     * @param properties Optional parameters (e.g., configuration parameters for the used metric).
     * @return The result of the metric execution
     */
    protected List<MetricResult> runMetric(File file, Properties properties) {
        return runMetric(file, properties, false, false, false);
    }
    
    /**
     * Runs the specified metric of {@link #getMetric()} on the specified file.
     * @param file The model file to parse (its folder will be treated as source_tree root).
     * @param properties Optional parameters (e.g., configuration parameters for the used metric).
     * @param emptyResultExpected Specifies whether no result is expected (i.e., no function is contained in file)
     * @param usePseudoVariabilityExtractor If <tt>true</tt> the {@link PseudoVariabilityExtractor} is used
     *     (but not configured).
     * @param useFullInputs Whether to create a {@link CodeMetricsRunner} with all inputs available.
     *     
     * @return The result of the metric execution
     */
    protected List<MetricResult> runMetric(File file, Properties properties, boolean emptyResultExpected,
        boolean usePseudoVariabilityExtractor, boolean useFullInputs) {
        
        Assert.assertTrue("Specified test file does not exist: " + file, file.isFile());
        TableCollectionWriterFactory.INSTANCE.registerHandler("memory", MemoryTableCollection.class);
        
        Properties props = new Properties();
        // General part
        props.setProperty("resource_dir", RESOURCE_DIR.getAbsolutePath());
        props.setProperty("source_tree", file.getParentFile().getAbsolutePath());
        props.setProperty(DefaultSettings.PLUGINS_DIR.getKey(), RESOURCE_DIR.getAbsolutePath());
        props.setProperty(DefaultSettings.OUTPUT_DIR.getKey(), GEN_FOLDER.getAbsolutePath());
        props.setProperty(DefaultSettings.LOG_DIR.getKey(), GEN_FOLDER.getAbsolutePath());
        props.setProperty(DefaultSettings.LOG_FILE.getKey(), Boolean.FALSE.toString());
        props.setProperty(DefaultSettings.ANALYSIS_RESULT.getKey(), "memory");
        
        // Extractor
        props.setProperty("code.extractor.class", getExtractor());
        props.setProperty("code.extractor.files", file.getName());
        
        // Metric / Analysis
        props.setProperty("analysis.class", "net.ssehub.kernel_haven.analysis.ConfiguredPipelineAnalysis");
        
        props.setProperty(CodeMetricsRunner.METRICS_SETTING.getKey() + ".0", getMetric());
        if (useFullInputs) {
            props.setProperty("analysis.pipeline", CodeMetricsRunner.class.getName() + "("
                + CodeFunctionFilter.class.getName() + "(cmComponent()), "
                + "vmComponent(), "
                + "bmComponent(), "
                + VariabilityCounter.class.getName() + "(vmComponent(), cmComponent()), "
                + FunctionMapCreator.class.getName() + "(" + CodeFunctionFilter.class.getName() + "(cmComponent())),"
                + FeatureSizeEstimator.class.getName() + "(vmComponent(), cmComponent(), bmComponent())"
                + ")");
        } else if (usePseudoVariabilityExtractor) {
            props.setProperty("analysis.pipeline", CodeMetricsRunner.class.getName() + "("
                    + CodeFunctionFilter.class.getName() + "(cmComponent()), "
                    + "vmComponent()"
                    + ")");
        } else {
            props.setProperty("analysis.pipeline", CodeMetricsRunner.class.getName() + "("
                    + CodeFunctionFilter.class.getName() + "(cmComponent())"
                    + ")");
        }
        
        props.setProperty(MetricSettings.ALL_METRIC_VARIATIONS.getKey(), "false");
        props.setProperty(CodeMetricsRunner.METRICS_SETTING.getKey(), getMetric());
        
        // Additional settings
        if (null != properties) {
            props.putAll(properties);
        }
        // Variability model extractor
        if (usePseudoVariabilityExtractor) {
            props.put("variability.extractor.class", PseudoVariabilityExtractor.class.getName());
        } else {
            props.setProperty("variability.extractor.class", EmptyVariabilityModelExtractor.class.getName());
        }
        // Build model extractor
        props.setProperty("build.extractor.class", EmptyBuildModelExtractor.class.getName());
        
        executePipeline(props);
        
        // There should be exactly one result:
        Assert.assertEquals(1, MemoryTableWriter.getTableNames().size());
        String resultName = MemoryTableWriter.getTableNames().iterator().next();
        List<Object[]> result = MemoryTableWriter.getTable(resultName);
        Assert.assertNotNull("Result of " + resultName + " is null.", result);
        Assert.assertEquals("Result of " + resultName + " is empty.", emptyResultExpected, result.isEmpty());
        List<MetricResult> resultList = convertToMetricResult(result);
        return resultList;
    }

    /**
     * Reads the metric results and converts them into a {@link MetricResult}.
     * @param result The results of the metric execution (after last great refactoring, all metrics should
     *     return {@link MultiMetricResult}s).
     * @return The converted results.
     */
    private List<MetricResult> convertToMetricResult(List<Object[]> result) {
        List<MetricResult> resultList = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            MultiMetricResult multiResult = (MultiMetricResult) result.get(i)[0];
            File measuredFile = new File(multiResult.getMeasuredItem().getMainFile());
            File includeFile = null;
            if (null != multiResult.getMeasuredItem().getIncludedFile()) {
                includeFile = new File(multiResult.getMeasuredItem().getIncludedFile());
            }
            int lineNo = multiResult.getMeasuredItem().getLineNo();
            String function = multiResult.getMeasuredItem().getElement();
            double value = multiResult.getValues()[0];
            resultList.add(new MetricResult(measuredFile, includeFile, lineNo, function, value));
        }
        return resultList;
    }

    /**
     * Creates a {@link Configuration} based on the given properties and executes the configured pipeline.
     * @param props The settings to take for the pipeline.
     */
    private void executePipeline(Properties props) {
        try {
            TestConfiguration config = new TestConfiguration(props);
            DefaultSettings.registerAllSettings(config);
            PipelineConfigurator.instance().init(config);
            
        } catch (SetUpException e) {
            Assert.fail("Invalid configuration detected: " + e.getMessage());
        }
        try {
            PipelineConfigurator.instance().instantiateExtractors();
            PipelineConfigurator.instance().execute();
        } catch (SetUpException exc) {
            Assert.fail("Failed to initialize metric analysis: " + exc.getMessage());
        }
    }
    
    /**
     * Runs the specified metric of {@link #getMetric()} on the specified file.
     * @param file The model file to parse (its folder will be treated as source_tree root).
     * @param properties Optional parameters (e.g., configuration parameters for the used metric).
     * @return The result of the metric execution as a map (function, result)
     */
    protected Map<String, MetricResult> runMetricAsMap(File file, Properties properties) {
        return runMetricAsMap(file, properties, false, false, false);
    }
    
    /**
     * Runs the specified metric of {@link #getMetric()} on the specified file.
     * @param file The model file to parse (its folder will be treated as source_tree root).
     * @param properties Optional parameters (e.g., configuration parameters for the used metric).
     * @param emptyResultExpected Specifies whether no result is expected (i.e., no function is contained in file)
     * @param usePseudoVariabilityExtractor If <tt>true</tt> the {@link PseudoVariabilityExtractor} is used
     *     (but not configured).
     * @param useFullInputs Whether to create a {@link CodeMetricsRunner} with all inputs available.
     * @return The result of the metric execution as a map (function, result)
     */
    protected Map<String, MetricResult> runMetricAsMap(File file, Properties properties, boolean emptyResultExpected,
        boolean usePseudoVariabilityExtractor, boolean useFullInputs) {
        
        List<MetricResult> resultList = runMetric(file, properties,
            emptyResultExpected, usePseudoVariabilityExtractor, useFullInputs);
        Map<String, MetricResult> resultMap = new HashMap<>();
        for (int i = 0; i < resultList.size(); i++) {
            resultMap.put(resultList.get(i).getContext(), resultList.get(i));
        }
        
        return resultMap;
    }
    
    /**
     * Tests the correct output of a function metric.
     * @param metricResult The result of a metric for one function to test.
     * @param expectedLine The expected start line of the function (0 if this should not be tested).
     * @param expectedResult The expected result of the metric for the specified function.
     */
    protected void assertMetricResult(MetricResult metricResult, int expectedLine, double expectedResult) {
        if (expectedLine > 0) {
            Assert.assertEquals("Wrong start line for \"" + metricResult.getContext() + "\"", expectedLine,
                metricResult.getLine());
        }
        Assert.assertEquals("Wrong metric result for \"" + metricResult.getContext() + "\"",
            expectedResult, metricResult.getValue(), 0);
    }
}
