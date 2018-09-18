package net.ssehub.kernel_haven.scenario_tests.metrics.weights;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import net.ssehub.kernel_haven.metric_haven.MetricResult;
import net.ssehub.kernel_haven.metric_haven.filter_components.CodeFunctionFilter;
import net.ssehub.kernel_haven.metric_haven.filter_components.scattering_degree.VariabilityCounter;
import net.ssehub.kernel_haven.metric_haven.metric_components.VariablesPerFunctionMetric;
import net.ssehub.kernel_haven.metric_haven.metric_components.VariablesPerFunctionMetric.VarType;
import net.ssehub.kernel_haven.metric_haven.metric_components.config.MetricSettings;
import net.ssehub.kernel_haven.metric_haven.metric_components.config.SDType;
import net.ssehub.kernel_haven.metric_haven.metric_components.config.VariabilityTypeMeasureType;
import net.ssehub.kernel_haven.scenario_tests.metrics.AbstractParameterizedTests;
import net.ssehub.kernel_haven.test_utils.PseudoVariabilityExtractor;
import net.ssehub.kernel_haven.variability_model.VariabilityVariable;

/**
 * Tests the {@link VariabilityTypeMeasureType} weight using the {@link VariablesPerFunctionMetric} (internal).
 *
 * @author Adam
 */
@RunWith(Parameterized.class)
public class ScatteringWeightTest extends AbstractParameterizedTests {

    private static final Properties PROPERTIES = new Properties();
    
    static {
        PROPERTIES.setProperty(VariablesPerFunctionMetric.VARIABLE_TYPE_SETTING.getKey(), VarType.INTERNAL.name());
        
        // override code.extractor.files to consider more than 1 file
        PROPERTIES.setProperty("code.extractor.files", "sd_1.c, sd_2.c");
        
        // override analysis.pipeline to include a scattering component
        PROPERTIES.setProperty("analysis.pipeline", VariablesPerFunctionMetric.class.getName() + "("
                + CodeFunctionFilter.class.getName() + "(cmComponent()), "
                + "vmComponent(),"
                + VariabilityCounter.class.getName() + "(vmComponent(), cmComponent())"
                + ")");
        
        System.out.println(PROPERTIES.get("analysis.pipeline"));
    }
    
    private SDType type;
    
    /**
     * Retrieves values from {@link #getParameters()}, creates, and executes the test.
     * 
     * @param fileName The name of the file to be tested.
     * @param testedFunctionName The function to test
     * @param expectedLineNo The expected starting line number of the function
     * @param expectedResultValue The expected value of the metric to compute.
     */
    // CHECKSTYLE:OFF // too many arguments
    public ScatteringWeightTest(String fileName, String testedFunctionName, int expectedLineNo,
        double expectedResultValue, SDType type) {
    // CHECKSTYLE:ON
        super(fileName, testedFunctionName, expectedLineNo, expectedResultValue);
        
        this.type = type;
        
        // Configuration of variability model
        VariabilityVariable varA = new VariabilityVariable("A", "bool");
        VariabilityVariable varB = new VariabilityVariable("B", "bool");
        
        PseudoVariabilityExtractor.configure(new File("mocked_varModel.dimacs"), varA, varB);
        
        PROPERTIES.setProperty(MetricSettings.SCATTERING_DEGREE_USAGE_SETTING.getKey(), type.name());
    }
    
    @Override
    protected String getMetric() {
        return VariablesPerFunctionMetric.class.getName();
    }
    
    /**
     * Creates the parameters for this test.
     * 
     * @return The parameters of this test.
     */
    @Parameters(name = "ScatteringDegree ({4}) Weight")
    public static Collection<Object[]> getParameters() {
        // A is used 3 times in sd_1.c (one of which is in func (internal)), and 2 times in sd_2.c
        
        return Arrays.asList(new Object[][] {
            {"sd_1.c", "func", 3, 1, SDType.NO_SCATTERING},
            {"sd_1.c", "func", 3, 3 + 2, SDType.SD_VP},
            {"sd_1.c", "func", 3, 2, SDType.SD_FILE},
        });
    }
    
    /**
     * Executes the test.
     */
    @Test
    public void test() {
        super.test(PROPERTIES);
    }

    @Override
    protected List<MetricResult> runMetric(File file, Properties properties) {
        return runMetric(file, properties, false, true);
    }
    
    @Override
    public Object getIdentifier() {
        return this.getClass().getCanonicalName() + type;
    }

}
