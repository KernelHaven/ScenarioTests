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
import net.ssehub.kernel_haven.config.DefaultSettings;
import net.ssehub.kernel_haven.metric_haven.MetricResult;
import net.ssehub.kernel_haven.metric_haven.filter_components.CodeFunctionFilter;
import net.ssehub.kernel_haven.scenario_tests.AllTests;
import net.ssehub.kernel_haven.srcml.SrcMLExtractor;
import net.ssehub.kernel_haven.test_utils.MemoryTableCollection;
import net.ssehub.kernel_haven.test_utils.MemoryTableWriter;
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
     * Returns the fully qualified class name of the extractor to use. PEr default srcML will be used.
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
        return runMetric(file, properties, false);
    }
    
    /**
     * Runs the specified metric of {@link #getMetric()} on the specified file.
     * @param file The model file to parse (its folder will be treated as source_tree root).
     * @param properties Optional parameters (e.g., configuration parameters for the used metric).
     * @param emptyResultExpected Specifies whether no result is expected (i.e., no function is contained in file)
     * @return The result of the metric execution
     */
    protected List<MetricResult> runMetric(File file, Properties properties, boolean emptyResultExpected) {
        Assert.assertTrue("Specified test file does not exist: " + file, file.isFile());
        
        try {
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
            props.setProperty("analysis.pipeline", getMetric() + "(" + CodeFunctionFilter.class.getName()
                    + "(cmComponent()))");
            
            // Additional settings
            if (null != properties) {
                props.putAll(properties);
            }
            
            // Unused extractors
            props.setProperty("variability.extractor.class", EmptyVariabilityModelExtractor.class.getName());
            props.setProperty("build.extractor.class", EmptyBuildModelExtractor.class.getName());
            
            TestConfiguration config = new TestConfiguration(props);
            
            try {
                DefaultSettings.registerAllSettings(config);
                PipelineConfigurator.instance().init(config);
                
            } catch (SetUpException e) {
                Assert.fail("Invalid configuration detected: " + e.getMessage());
            }
            PipelineConfigurator.instance().instantiateExtractors();
            PipelineConfigurator.instance().execute();
        } catch (SetUpException exc) {
            Assert.fail("Failed to initialize metric analysis: " + exc.getMessage());
        }
        
        // There should be exactly one result:
        Assert.assertEquals(1, MemoryTableWriter.getTableNames().size());
        String resultName = MemoryTableWriter.getTableNames().iterator().next();
        List<Object[]> result = MemoryTableWriter.getTable(resultName);
        Assert.assertNotNull("Result of " + resultName + " is null.", result);
        Assert.assertEquals("Result of " + resultName + " is empty.", emptyResultExpected, result.isEmpty());
        
        List<MetricResult> resultList = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            resultList.add((MetricResult) result.get(i)[0]);
        }
        return resultList;
    }
    
    /**
     * Runs the specified metric of {@link #getMetric()} on the specified file.
     * @param file The model file to parse (its folder will be treated as source_tree root).
     * @param properties Optional parameters (e.g., configuration parameters for the used metric).
     * @return The result of the metric execution as a map (function, result)
     */
    protected Map<String, MetricResult> runMetricAsMap(File file, Properties properties) {
        List<MetricResult> resultList = runMetric(file, properties);
        Map<String, MetricResult> resultMap = new HashMap<>();
        for (int i = 0; i < resultList.size(); i++) {
            resultMap.put(resultList.get(i).getContext(), resultList.get(i));
        }
        
        return resultMap;
    }
    
    /**
     * Tests the correct output of a function metric.
     * @param metricResult The result of a metric for one function to test.
     * @param expectedLine The expected start line of the function.
     * @param expectedResult The expected result of the metric for the specified function.
     */
    protected void assertMetricResult(MetricResult metricResult, int expectedLine, double expectedResult) {
        Assert.assertEquals(expectedLine, metricResult.getLine());
        Assert.assertEquals(expectedResult, metricResult.getValue(), 0);
    }
}
