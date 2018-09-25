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
import net.ssehub.kernel_haven.metric_haven.code_metrics.VariablesPerFunction.VarType;
import net.ssehub.kernel_haven.metric_haven.metric_components.VariablesPerFunctionMetric;
import net.ssehub.kernel_haven.metric_haven.metric_components.config.FeatureDistanceType;
import net.ssehub.kernel_haven.metric_haven.metric_components.config.MetricSettings;
import net.ssehub.kernel_haven.scenario_tests.metrics.AbstractParameterizedTests;
import net.ssehub.kernel_haven.test_utils.PseudoVariabilityExtractor;
import net.ssehub.kernel_haven.variability_model.SourceLocation;
import net.ssehub.kernel_haven.variability_model.VariabilityModelDescriptor.Attribute;
import net.ssehub.kernel_haven.variability_model.VariabilityVariable;

/**
 * Tests metrics execution of variables per function (<b>internal</b>) metrics with srcML-Extractor.
 * @author El-Sharkawy
 *
 */
@RunWith(Parameterized.class)
public class FeatureDistanceTests extends AbstractParameterizedTests {

    private static final Properties VAR_INTERNAL = new Properties();
    
    static {
        VAR_INTERNAL.setProperty(VariablesPerFunctionMetric.VARIABLE_TYPE_SETTING.getKey(), VarType.INTERNAL.name());
        VAR_INTERNAL.setProperty(MetricSettings.LOCATION_DISTANCE_SETTING.getKey(),
            FeatureDistanceType.SHORTEST_DISTANCE.name());
        
    }
    
    /**
     * Retrieves values from {@link #getParameters()}, creates, and executes the test.
     * @param fileName The name of the file to be tested.
     * @param testedFunctionName The function to test
     * @param expectedLineNo The expected starting line number of the function
     * @param expectedResultValue The expected value of the metric to compute.
     */
    public FeatureDistanceTests(String fileName, String testedFunctionName, int expectedLineNo,
        double expectedResultValue) {
        
        super(fileName, testedFunctionName, expectedLineNo, expectedResultValue);
        
        // Configuration of variability model
        // Variable A's distance is 2
        VariabilityVariable varA = new VariabilityVariable("A", "bool");
        SourceLocation srcVarA = new SourceLocation(new File(getTestFile().getAbsoluteFile().getParentFile(),
            "A.varModel"), 1);
        varA.addLocation(srcVarA);
        // Variable B's distance is 3
        VariabilityVariable varB = new VariabilityVariable("B", "bool");
        SourceLocation srcVarB = new SourceLocation(new File(getTestFile().getAbsoluteFile(), "B.varModel"), 1);
        varB.addLocation(srcVarB);
        
        PseudoVariabilityExtractor.configure(new File("mocked_varModel.dimacs"), varA, varB);
        PseudoVariabilityExtractor.setAttributes(Attribute.SOURCE_LOCATIONS);
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
    @Parameters(name = "Feature Distance Weight: {1}")
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
            {"VariabilityFunctions.c", "funcVarNesting", 21, 5}
        });
    }
    
    /**
     * Executes the Internal Vars tests.
     */
    @Test
    public void test() {
        super.test(VAR_INTERNAL);
    }

    @Override
    protected List<MetricResult> runMetric(File file, Properties properties) {
        
        return runMetric(file, properties, false, true);
    }
}

