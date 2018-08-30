package net.ssehub.kernel_haven.scenario_tests.metrics.weights;

import static net.ssehub.kernel_haven.metric_haven.metric_components.config.StructuralType.COC;
import static net.ssehub.kernel_haven.metric_haven.metric_components.config.StructuralType.NUMBER_OF_CHILDREN;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import net.ssehub.kernel_haven.metric_haven.MetricResult;
import net.ssehub.kernel_haven.metric_haven.metric_components.VariablesPerFunctionMetric;
import net.ssehub.kernel_haven.metric_haven.metric_components.VariablesPerFunctionMetric.VarType;
import net.ssehub.kernel_haven.metric_haven.metric_components.config.MetricSettings;
import net.ssehub.kernel_haven.metric_haven.metric_components.config.StructuralType;
import net.ssehub.kernel_haven.metric_haven.metric_components.weights.StructuralWeight;
import net.ssehub.kernel_haven.scenario_tests.metrics.AbstractParameterizedTests;
import net.ssehub.kernel_haven.test_utils.PseudoVariabilityExtractor;
import net.ssehub.kernel_haven.variability_model.HierarchicalVariable;
import net.ssehub.kernel_haven.variability_model.VariabilityModelDescriptor.Attribute;
import net.ssehub.kernel_haven.variability_model.VariabilityVariable;

/**
 * Tests the {@link StructuralWeight} weight using the {@link VariablesPerFunctionMetric} (internal).
 *
 * @author Adam
 */
@RunWith(Parameterized.class)
public class StructureWeightTest extends AbstractParameterizedTests {

    private static final Properties PROPERTIES = new Properties();
    
    static {
        PROPERTIES.setProperty(VariablesPerFunctionMetric.VARIABLE_TYPE_SETTING.getKey(), VarType.INTERNAL.name());
    }
    
    private boolean aParent;
    
    private int aChildren;
    
    private boolean bParent;
    
    private int bChildren;
    
    private StructuralType type;
    
    /**
     * Retrieves values from {@link #getParameters()}, creates, and executes the test.
     * 
     * @param fileName The name of the file to be tested.
     * @param testedFunctionName The function to test
     * @param expectedLineNo The expected starting line number of the function
     * @param expectedResultValue The expected value of the metric to compute.
     * @param type The type of {@link StructuralWeight} to use.
     */
    // CHECKSTYLE:OFF // too many arguments
    public StructureWeightTest(String fileName, String testedFunctionName, int expectedLineNo,
        double expectedResultValue, boolean aParent, int aChildren, boolean bParent,
        int bChildren, StructuralType type) {
    // CHECKSTYLE:ON
        super(fileName, testedFunctionName, expectedLineNo, expectedResultValue);
        
        this.aParent = aParent;
        this.aChildren = aChildren;
        this.bParent = bParent;
        this.bChildren = bChildren;
        this.type = type;
        
        // Configuration of variability model
        List<VariabilityVariable> variables = new LinkedList<>();
        
        HierarchicalVariable varA = new HierarchicalVariable("A", "bool");
        HierarchicalVariable varB = new HierarchicalVariable("B", "bool");
        
        variables.add(varA);
        variables.add(varB);

        if (aParent) {
            HierarchicalVariable parentA = new HierarchicalVariable("PARENT_A", "bool");
            varA.setParent(parentA);
            variables.add(parentA);
        }
        if (bParent) {
            HierarchicalVariable parentB = new HierarchicalVariable("PARENT_B", "bool");
            varB.setParent(parentB);
            variables.add(parentB);
        }
        
        for (int i = 0; i < aChildren; i++) {
            HierarchicalVariable childA = new HierarchicalVariable("CHILD_A_" + i, "bool");
            childA.setParent(varA);
            variables.add(childA);
        }
        
        for (int i = 0; i < bChildren; i++) {
            HierarchicalVariable childB = new HierarchicalVariable("CHILD_B_" + i, "bool");
            childB.setParent(varB);
            variables.add(childB);
        }
        
        PseudoVariabilityExtractor.configure(new File("mocked_varModel.dimacs"),
                variables.toArray(new VariabilityVariable[0]));
        PseudoVariabilityExtractor.setAttributes(Attribute.HIERARCHICAL);
        
        PROPERTIES.setProperty(MetricSettings.STRUCTURE_MEASURING_SETTING.getKey(),
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
    @Parameters(name = "Structural ({8}) Weight: parentA={4}, childrenA={5}, parentB={6}, childrenB={7} on {1}")
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
            {"VariabilityFunctions.c", "funcVarNesting", 21, 0, false, 0, false, 0, NUMBER_OF_CHILDREN},
            {"VariabilityFunctions.c", "funcVarNesting", 21, 1, false, 1, false, 0, NUMBER_OF_CHILDREN},
            {"VariabilityFunctions.c", "funcVarNesting", 21, 1, false, 0, false, 1, NUMBER_OF_CHILDREN},
            {"VariabilityFunctions.c", "funcVarNesting", 21, 5, false, 3, false, 2, NUMBER_OF_CHILDREN},
            
            {"VariabilityFunctions.c", "funcVarNesting", 21, 5, false, 3, false, 2, COC},
            {"VariabilityFunctions.c", "funcVarNesting", 21, 6, true, 3, false, 2, COC},
            {"VariabilityFunctions.c", "funcVarNesting", 21, 6, false, 3, true, 2, COC},
            {"VariabilityFunctions.c", "funcVarNesting", 21, 7, true, 3, true, 2, COC},
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
        return this.getClass().getCanonicalName() + aParent + aChildren + bParent + bChildren + type;
    }

}
