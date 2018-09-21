package net.ssehub.kernel_haven.scenario_tests.metrics.weights;

import static net.ssehub.kernel_haven.metric_haven.metric_components.config.HierarchyType.HIERARCHY_WEIGHTS_BY_FILE;
import static net.ssehub.kernel_haven.metric_haven.metric_components.config.HierarchyType.HIERARCHY_WEIGHTS_BY_LEVEL;

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
import net.ssehub.kernel_haven.metric_haven.metric_components.config.HierarchyType;
import net.ssehub.kernel_haven.metric_haven.metric_components.config.MetricSettings;
import net.ssehub.kernel_haven.scenario_tests.metrics.AbstractParameterizedTests;
import net.ssehub.kernel_haven.test_utils.PseudoVariabilityExtractor;
import net.ssehub.kernel_haven.variability_model.HierarchicalVariable;
import net.ssehub.kernel_haven.variability_model.VariabilityModelDescriptor.Attribute;
import net.ssehub.kernel_haven.variability_model.VariabilityVariable;

/**
 * Tests the {@link HierarchyType} weight using the {@link VariablesPerFunctionMetric} (internal).
 *
 * @author Adam
 */
@RunWith(Parameterized.class)
public class HierarchyWeightTest extends AbstractParameterizedTests {

    private static final Properties PROPERTIES = new Properties();
    
    static {
        PROPERTIES.setProperty(VariablesPerFunctionMetric.VARIABLE_TYPE_SETTING.getKey(), VarType.INTERNAL.name());
        
        PROPERTIES.setProperty(MetricSettings.HIERARCHY_WEIGHTS_SETTING.getKey(), "top:1, intermediate:10, leaf:100");
    }
    
    private String hierarchyTypeA;
    
    private String hierarchyTypeB;
    
    private HierarchyType type;
    
    /**
     * Retrieves values from {@link #getParameters()}, creates, and executes the test.
     * 
     * @param fileName The name of the file to be tested.
     * @param testedFunctionName The function to test
     * @param expectedLineNo The expected starting line number of the function
     * @param expectedResultValue The expected value of the metric to compute.
     * @param hierarchyTypeA The hierarchy type of the {@link VariabilityVariable} A.
     * @param hierarchyTypeB The hierarchy type of the {@link VariabilityVariable} B.
     * @param type The type of hierarchy weight that should be measured (by file or by level).
     */
    // CHECKSTYLE:OFF // too many arguments
    public HierarchyWeightTest(String fileName, String testedFunctionName, int expectedLineNo,
        double expectedResultValue, String hierarchyTypeA, String hierarchyTypeB, HierarchyType type) {
    // CHECKSTYLE:ON
        super(fileName, testedFunctionName, expectedLineNo, expectedResultValue);
        
        this.hierarchyTypeA = hierarchyTypeA;
        this.hierarchyTypeB = hierarchyTypeB;
        this.type = type;
        
        // Configuration of variability model
        HierarchicalVariable parent = new HierarchicalVariable("PARENT", "bool");
        HierarchicalVariable childA = new HierarchicalVariable("CHILD_A", "bool");
        HierarchicalVariable childB = new HierarchicalVariable("CHILD_B", "bool");
        
        HierarchicalVariable varA = new HierarchicalVariable("A", "bool");
        if (!hierarchyTypeA.equals("top")) {
            varA.setParent(parent);
        }
        if (!hierarchyTypeA.equals("leaf")) {
            childA.setParent(varA);
        }
        
        HierarchicalVariable varB = new HierarchicalVariable("B", "bool");
        if (!hierarchyTypeB.equals("top")) {
            varB.setParent(parent);
        }
        if (!hierarchyTypeB.equals("leaf")) {
            childB.setParent(varB);
        }
        
        PseudoVariabilityExtractor.configure(new File("mocked_varModel.dimacs"), varA, varB, parent, childA, childB);
        PseudoVariabilityExtractor.setAttributes(Attribute.HIERARCHICAL);
        
        PROPERTIES.setProperty(MetricSettings.HIERARCHY_TYPE_MEASURING_SETTING.getKey(),
                type.name());
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
    @Parameters(name = "{6}-Weight: Levels {4} and {5} on {1}")
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
            {"VariabilityFunctions.c", "funcVarNesting", 21, 2, "top", "top", HIERARCHY_WEIGHTS_BY_LEVEL},
            {"VariabilityFunctions.c", "funcVarNesting", 21, 3, "top", "intermediate", HIERARCHY_WEIGHTS_BY_LEVEL},
            {"VariabilityFunctions.c", "funcVarNesting", 21, 3, "intermediate", "top", HIERARCHY_WEIGHTS_BY_LEVEL},
            {"VariabilityFunctions.c", "funcVarNesting", 21, 4, "intermediate", "intermediate",
                HIERARCHY_WEIGHTS_BY_LEVEL},
            
            {"VariabilityFunctions.c", "funcVarNesting", 21, 2, "top", "top", HIERARCHY_WEIGHTS_BY_FILE},
            {"VariabilityFunctions.c", "funcVarNesting", 21, 11, "top", "intermediate", HIERARCHY_WEIGHTS_BY_FILE},
            {"VariabilityFunctions.c", "funcVarNesting", 21, 101, "top", "leaf", HIERARCHY_WEIGHTS_BY_FILE},
            
            {"VariabilityFunctions.c", "funcVarNesting", 21, 11, "intermediate", "top", HIERARCHY_WEIGHTS_BY_FILE},
            {"VariabilityFunctions.c", "funcVarNesting", 21, 20, "intermediate", "intermediate",
                HIERARCHY_WEIGHTS_BY_FILE},
            {"VariabilityFunctions.c", "funcVarNesting", 21, 110, "intermediate", "leaf", HIERARCHY_WEIGHTS_BY_FILE},
            
            {"VariabilityFunctions.c", "funcVarNesting", 21, 101, "leaf", "top", HIERARCHY_WEIGHTS_BY_FILE},
            {"VariabilityFunctions.c", "funcVarNesting", 21, 110, "leaf", "intermediate", HIERARCHY_WEIGHTS_BY_FILE},
            {"VariabilityFunctions.c", "funcVarNesting", 21, 200, "leaf", "leaf", HIERARCHY_WEIGHTS_BY_FILE},
            
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
        return this.getClass().getCanonicalName() + hierarchyTypeA + hierarchyTypeB + type.name();
    }

}
