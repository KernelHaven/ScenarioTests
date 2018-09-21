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
import net.ssehub.kernel_haven.metric_haven.code_metrics.VariablesPerFunctionMetric.VarType;
import net.ssehub.kernel_haven.metric_haven.metric_components.VariablesPerFunctionMetric;
import net.ssehub.kernel_haven.metric_haven.metric_components.config.MetricSettings;
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
public class VariabilityTypeMeasureTest extends AbstractParameterizedTests {

    private static final Properties PROPERTIES = new Properties();
    
    static {
        PROPERTIES.setProperty(VariablesPerFunctionMetric.VARIABLE_TYPE_SETTING.getKey(), VarType.INTERNAL.name());
        
        PROPERTIES.setProperty(MetricSettings.TYPE_MEASURING_SETTING.getKey(),
                VariabilityTypeMeasureType.TYPE_WEIGHTS_BY_FILE.name());
        PROPERTIES.setProperty(MetricSettings.TYPE_WEIGHTS_SETTING.getKey(), "bool:1, tristate:2");
    }
    
    private String typeA;
    
    private String typeB;
    
    /**
     * Retrieves values from {@link #getParameters()}, creates, and executes the test.
     * 
     * @param fileName The name of the file to be tested.
     * @param testedFunctionName The function to test
     * @param expectedLineNo The expected starting line number of the function
     * @param expectedResultValue The expected value of the metric to compute.
     * @param typeA The type of the {@link VariabilityVariable} A.
     * @param typeB The type of the {@link VariabilityVariable} B.
     */
    // CHECKSTYLE:OFF // too many arguments
    public VariabilityTypeMeasureTest(String fileName, String testedFunctionName, int expectedLineNo,
        double expectedResultValue, String typeA, String typeB) {
    // CHECKSTYLE:ON
        super(fileName, testedFunctionName, expectedLineNo, expectedResultValue);
        
        this.typeA = typeA;
        this.typeB = typeB;
        
        // Configuration of variability model
        if (typeA != null) {
            VariabilityVariable varA = new VariabilityVariable("A", typeA);
            VariabilityVariable varB = new VariabilityVariable("B", typeB);
            
            PseudoVariabilityExtractor.configure(new File("mocked_varModel.dimacs"), varA, varB);
        } else {
            VariabilityVariable varB = new VariabilityVariable("B", typeB);
            
            PseudoVariabilityExtractor.configure(new File("mocked_varModel.dimacs"), varB);
        }
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
    @Parameters(name = "VariabilityType-Weight: Types {4} and {5} on {1}")
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
            {"VariabilityFunctions.c", "funcVarNesting", 21, 2, "bool", "bool"},
            {"VariabilityFunctions.c", "funcVarNesting", 21, 3, "tristate", "bool"},
            {"VariabilityFunctions.c", "funcVarNesting", 21, 3, "bool", "tristate"},
            {"VariabilityFunctions.c", "funcVarNesting", 21, 4, "tristate", "tristate"},
            {"VariabilityFunctions.c", "funcVarNesting", 21, 1, null, "bool"},
            {"VariabilityFunctions.c", "funcVarNesting", 21, 1, "doesnt_exist", "bool"},
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
        return this.getClass().getCanonicalName() + typeA + typeB;
    }

}
