/*
 * Copyright 2019 University of Hildesheim, Software Systems Engineering
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

import static net.ssehub.kernel_haven.metric_haven.metric_components.config.MetricSettings.BLOCK_TYPE_SETTING;
import static net.ssehub.kernel_haven.metric_haven.metric_components.config.MetricSettings.CC_VARIABLE_TYPE_SETTING;
import static net.ssehub.kernel_haven.metric_haven.metric_components.config.MetricSettings.FAN_TYPE_SETTING;
import static net.ssehub.kernel_haven.metric_haven.metric_components.config.MetricSettings.LOC_TYPE_SETTING;
import static net.ssehub.kernel_haven.metric_haven.metric_components.config.MetricSettings.ND_TYPE_SETTING;
import static net.ssehub.kernel_haven.metric_haven.metric_components.config.MetricSettings.TD_TYPE_SETTING;
import static net.ssehub.kernel_haven.metric_haven.metric_components.config.MetricSettings.VARIABLE_TYPE_SETTING;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import net.ssehub.kernel_haven.config.Setting;
import net.ssehub.kernel_haven.metric_haven.MetricResult;
import net.ssehub.kernel_haven.metric_haven.code_metrics.AbstractFunctionMetric;
import net.ssehub.kernel_haven.metric_haven.code_metrics.BlocksPerFunctionMetric;
import net.ssehub.kernel_haven.metric_haven.code_metrics.BlocksPerFunctionMetric.BlockMeasureType;
import net.ssehub.kernel_haven.metric_haven.code_metrics.CyclomaticComplexity;
import net.ssehub.kernel_haven.metric_haven.code_metrics.CyclomaticComplexity.CCType;
import net.ssehub.kernel_haven.metric_haven.code_metrics.EigenVectorCentrality;
import net.ssehub.kernel_haven.metric_haven.code_metrics.FanInOut;
import net.ssehub.kernel_haven.metric_haven.code_metrics.FanInOut.FanType;
import net.ssehub.kernel_haven.metric_haven.code_metrics.LoCMetric;
import net.ssehub.kernel_haven.metric_haven.code_metrics.LoCMetric.LoCType;
import net.ssehub.kernel_haven.metric_haven.code_metrics.NestingDepth;
import net.ssehub.kernel_haven.metric_haven.code_metrics.NestingDepth.NDType;
import net.ssehub.kernel_haven.metric_haven.code_metrics.TanglingDegree;
import net.ssehub.kernel_haven.metric_haven.code_metrics.TanglingDegree.TDType;
import net.ssehub.kernel_haven.metric_haven.code_metrics.UndisciplinedPreprocessorUsage;
import net.ssehub.kernel_haven.metric_haven.code_metrics.VariablesPerFunction;
import net.ssehub.kernel_haven.metric_haven.code_metrics.VariablesPerFunction.VarType;
import net.ssehub.kernel_haven.metric_haven.metric_components.config.HierarchyType;
import net.ssehub.kernel_haven.metric_haven.metric_components.config.MetricSettings;
import net.ssehub.kernel_haven.metric_haven.metric_components.visitors.UsedVariabilityVarsVisitor;
import net.ssehub.kernel_haven.metric_haven.metric_components.weights.IVariableWeight;
import net.ssehub.kernel_haven.test_utils.PseudoVariabilityExtractor;
import net.ssehub.kernel_haven.variability_model.HierarchicalVariable;
import net.ssehub.kernel_haven.variability_model.VariabilityModelDescriptor.Attribute;

/**
 * Tests each metric variation and if possible the combination with feature weights.
 *
 * @author El-Sharkawy
 */
@RunWith(Parameterized.class)
public class SimpleAtomicSetTest extends AbstractParameterizedTests {
    
    private Class<? extends AbstractFunctionMetric<UsedVariabilityVarsVisitor>> metric;
    private Properties baseSetting;
    
    /**
     * Retrieves values from {@link #getParameters()}, creates, and executes the test.
     * @param metric The metric to test.
     * @param key Optional: A metric-specific setting to use for the test.
     * @param value Optional: The value for the setting to use for the test.
     * @param useWeight If true a weight is used with (FACTORIAL = 42, ITERATIVE = 21) to test
     *     combination of metric with {@link IVariableWeight}s.
     * @param expectedResultValue The expected value of the metric to compute.
     */
    public SimpleAtomicSetTest(Class<? extends AbstractFunctionMetric<UsedVariabilityVarsVisitor>> metric,
        Setting<?> key, Enum<?> value, boolean useWeight, double expectedResultValue) {
        
        super("Factorial.c", "factorial", 2, expectedResultValue);
        this.metric = metric;
        
        baseSetting = new Properties();
        
        if (null != key && null != value) {
            baseSetting.put(key.getKey(), value.name());
        }
        
        if (useWeight) {
            baseSetting.setProperty(MetricSettings.HIERARCHY_TYPE_MEASURING_SETTING.getKey(),
                HierarchyType.HIERARCHY_WEIGHTS_BY_FILE.name());
            baseSetting.setProperty(MetricSettings.HIERARCHY_WEIGHTS_SETTING.getKey(),
                "top:42, leaf:21, intermediate:0");
        }
    }
    
    /**
     * Creates the parameters for this test.
     * 
     * @return The parameters of this test.
     */
    // CHECKSTYLE:OFF
    @Parameters(name = "Factorial: {2} with weight = {3}")
    // CHECKSTYLE:ON
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
            // LoC variations
            {LoCMetric.class, LOC_TYPE_SETTING, LoCType.SCOC,  false, 6},
            {LoCMetric.class, LOC_TYPE_SETTING, LoCType.SCOF,  false, 4},
            {LoCMetric.class, LOC_TYPE_SETTING, LoCType.SCOC_COMMENT_RATIO,  false, 2d / (6 + 2)},
            {LoCMetric.class, LOC_TYPE_SETTING, LoCType.SCOF_COMMENT_RATIO,  false, 2d / (4 + 2)},
            {LoCMetric.class, LOC_TYPE_SETTING, LoCType.PSCOF, false, 4d / 6},
            {LoCMetric.class, LOC_TYPE_SETTING, LoCType.LOC,   false, 18},
            {LoCMetric.class, LOC_TYPE_SETTING, LoCType.LOF,   false, 12},
            {LoCMetric.class, LOC_TYPE_SETTING, LoCType.LOC_COMMENT_RATIO,  false, 2d / 18},
            {LoCMetric.class, LOC_TYPE_SETTING, LoCType.LOF_COMMENT_RATIO,  false, 2d / 12},
            {LoCMetric.class, LOC_TYPE_SETTING, LoCType.PLOF,  false, 12d / 18},
            
            // Vars per Function
            {VariablesPerFunction.class, VARIABLE_TYPE_SETTING, VarType.INTERNAL, false, 1},
            {VariablesPerFunction.class, VARIABLE_TYPE_SETTING, VarType.INTERNAL, true, 21},
            {VariablesPerFunction.class, VARIABLE_TYPE_SETTING, VarType.EXTERNAL, false, 2},
            {VariablesPerFunction.class, VARIABLE_TYPE_SETTING, VarType.EXTERNAL, true, 63},
            
            // Blocks per Function
            {BlocksPerFunctionMetric.class, BLOCK_TYPE_SETTING, BlockMeasureType.BLOCK_AS_ONE, false, 1},
            {BlocksPerFunctionMetric.class, BLOCK_TYPE_SETTING, BlockMeasureType.SEPARATE_PARTIAL_BLOCKS, false, 2},
            
            // Tangling Degree
            {TanglingDegree.class, TD_TYPE_SETTING, TDType.TD_ALL, false, 2},
            {TanglingDegree.class, TD_TYPE_SETTING, TDType.TD_ALL, true, 42},
            {TanglingDegree.class, TD_TYPE_SETTING, TDType.TD_VISIBLE, false, 1},
            {TanglingDegree.class, TD_TYPE_SETTING, TDType.TD_VISIBLE, true, 21},
            
            // Cyclomatic Complexity
            {CyclomaticComplexity.class, CC_VARIABLE_TYPE_SETTING, CCType.MCCABE, false, 3},
            {CyclomaticComplexity.class, CC_VARIABLE_TYPE_SETTING, CCType.VARIATION_POINTS, false, 2},
            {CyclomaticComplexity.class, CC_VARIABLE_TYPE_SETTING, CCType.VARIATION_POINTS, true, 22},
            {CyclomaticComplexity.class, CC_VARIABLE_TYPE_SETTING, CCType.ALL, false, 4},
            {CyclomaticComplexity.class, CC_VARIABLE_TYPE_SETTING, CCType.ALL, true, 24},
            
            // Nesting Depth
            {NestingDepth.class, ND_TYPE_SETTING, NDType.CLASSIC_ND_MAX, false, 2},
            {NestingDepth.class, ND_TYPE_SETTING, NDType.CLASSIC_ND_AVG, false, 8d / 6},
            {NestingDepth.class, ND_TYPE_SETTING, NDType.VP_ND_MAX, false, 1},
            {NestingDepth.class, ND_TYPE_SETTING, NDType.VP_ND_MAX, true, 21},
            {NestingDepth.class, ND_TYPE_SETTING, NDType.VP_ND_AVG, false, 4d / 6},
            {NestingDepth.class, ND_TYPE_SETTING, NDType.VP_ND_AVG, true, 84d / 6},
            {NestingDepth.class, ND_TYPE_SETTING, NDType.COMBINED_ND_MAX, false, 3},
            {NestingDepth.class, ND_TYPE_SETTING, NDType.COMBINED_ND_MAX, true, 23},
            {NestingDepth.class, ND_TYPE_SETTING, NDType.COMBINED_ND_AVG, false, 12d / 6},
            {NestingDepth.class, ND_TYPE_SETTING, NDType.COMBINED_ND_AVG, true, 92d / 6},
            
            // Fan in/Out
            {FanInOut.class, FAN_TYPE_SETTING, FanType.CLASSICAL_FAN_IN_GLOBALLY,              false, 1},
            {FanInOut.class, FAN_TYPE_SETTING, FanType.CLASSICAL_FAN_OUT_GLOBALLY,             false, 1},
            {FanInOut.class, FAN_TYPE_SETTING, FanType.CLASSICAL_FAN_IN_LOCALLY,               false, 1},
            {FanInOut.class, FAN_TYPE_SETTING, FanType.CLASSICAL_FAN_OUT_LOCALLY,              false, 1},
            {FanInOut.class, FAN_TYPE_SETTING, FanType.VP_FAN_IN_GLOBALLY,                     false, 0},
            {FanInOut.class, FAN_TYPE_SETTING, FanType.VP_FAN_OUT_GLOBALLY,                    false, 0},
            {FanInOut.class, FAN_TYPE_SETTING, FanType.VP_FAN_IN_LOCALLY,                      false, 0},
            {FanInOut.class, FAN_TYPE_SETTING, FanType.VP_FAN_OUT_LOCALLY,                     false, 0},
            {FanInOut.class, FAN_TYPE_SETTING, FanType.DEGREE_CENTRALITY_IN_GLOBALLY,          false, 3},
            {FanInOut.class, FAN_TYPE_SETTING, FanType.DEGREE_CENTRALITY_IN_GLOBALLY,          true,  42 + 21 + 1},
            {FanInOut.class, FAN_TYPE_SETTING, FanType.DEGREE_CENTRALITY_IN_LOCALLY,           false, 3},
            {FanInOut.class, FAN_TYPE_SETTING, FanType.DEGREE_CENTRALITY_IN_LOCALLY,           true,  42 + 21 + 1},
            {FanInOut.class, FAN_TYPE_SETTING, FanType.DEGREE_CENTRALITY_OUT_GLOBALLY,         false, 3},
            {FanInOut.class, FAN_TYPE_SETTING, FanType.DEGREE_CENTRALITY_OUT_GLOBALLY,         true,  42 + 21 + 1},
            {FanInOut.class, FAN_TYPE_SETTING, FanType.DEGREE_CENTRALITY_OUT_LOCALLY,          false, 3},
            {FanInOut.class, FAN_TYPE_SETTING, FanType.DEGREE_CENTRALITY_OUT_LOCALLY,          true,  42 + 21 + 1},
            {FanInOut.class, FAN_TYPE_SETTING, FanType.DEGREE_CENTRALITY_OUT_NO_STUB_GLOBALLY, false, 3},
            {FanInOut.class, FAN_TYPE_SETTING, FanType.DEGREE_CENTRALITY_OUT_NO_STUB_GLOBALLY, true,  42 + 21 + 1},
            {FanInOut.class, FAN_TYPE_SETTING, FanType.DEGREE_CENTRALITY_OUT_NO_STUB_LOCALLY,  false, 3},
            {FanInOut.class, FAN_TYPE_SETTING, FanType.DEGREE_CENTRALITY_OUT_NO_STUB_LOCALLY,  true,  42 + 21 + 1},
            {FanInOut.class, FAN_TYPE_SETTING, FanType.DEGREE_CENTRALITY_OUT_NO_EXTERNAL_VPS_GLOBALLY, false, 2},
            {FanInOut.class, FAN_TYPE_SETTING, FanType.DEGREE_CENTRALITY_OUT_NO_EXTERNAL_VPS_GLOBALLY, true,  21 + 1},
            {FanInOut.class, FAN_TYPE_SETTING, FanType.DEGREE_CENTRALITY_OUT_NO_EXTERNAL_VPS_LOCALLY,  false, 2},
            {FanInOut.class, FAN_TYPE_SETTING, FanType.DEGREE_CENTRALITY_OUT_NO_EXTERNAL_VPS_LOCALLY,  true,  21 + 1},
            {FanInOut.class, FAN_TYPE_SETTING, FanType.DEGREE_CENTRALITY_OUT_NO_STUB_NO_EXTERNAL_VPS_GLOBALLY,
                false, 2},
            {FanInOut.class, FAN_TYPE_SETTING, FanType.DEGREE_CENTRALITY_OUT_NO_STUB_NO_EXTERNAL_VPS_GLOBALLY,
                true,  21 + 1},
            {FanInOut.class, FAN_TYPE_SETTING, FanType.DEGREE_CENTRALITY_OUT_NO_STUB_NO_EXTERNAL_VPS_LOCALLY,
                false, 2},
            {FanInOut.class, FAN_TYPE_SETTING, FanType.DEGREE_CENTRALITY_OUT_NO_STUB_NO_EXTERNAL_VPS_LOCALLY,
                true,  21 + 1},
            
            // Eigenvector
            {EigenVectorCentrality.class, FAN_TYPE_SETTING, FanType.CLASSICAL_FAN_IN_GLOBALLY,              false, 0},
            {EigenVectorCentrality.class, FAN_TYPE_SETTING, FanType.CLASSICAL_FAN_OUT_GLOBALLY,             false, 0},
            {EigenVectorCentrality.class, FAN_TYPE_SETTING, FanType.CLASSICAL_FAN_IN_LOCALLY,               false, 0},
            {EigenVectorCentrality.class, FAN_TYPE_SETTING, FanType.CLASSICAL_FAN_OUT_LOCALLY,              false, 0},
            {EigenVectorCentrality.class, FAN_TYPE_SETTING, FanType.VP_FAN_IN_GLOBALLY,                     false, 0},
            {EigenVectorCentrality.class, FAN_TYPE_SETTING, FanType.VP_FAN_OUT_GLOBALLY,                    false, 0},
            {EigenVectorCentrality.class, FAN_TYPE_SETTING, FanType.VP_FAN_IN_LOCALLY,                      false, 0},
            {EigenVectorCentrality.class, FAN_TYPE_SETTING, FanType.VP_FAN_OUT_LOCALLY,                     false, 0},
            {EigenVectorCentrality.class, FAN_TYPE_SETTING, FanType.DEGREE_CENTRALITY_IN_GLOBALLY,          false, 0},
            {EigenVectorCentrality.class, FAN_TYPE_SETTING, FanType.DEGREE_CENTRALITY_IN_GLOBALLY,          true,  0},
            {EigenVectorCentrality.class, FAN_TYPE_SETTING, FanType.DEGREE_CENTRALITY_IN_LOCALLY,           false, 0},
            {EigenVectorCentrality.class, FAN_TYPE_SETTING, FanType.DEGREE_CENTRALITY_IN_LOCALLY,           true,  0},
            {EigenVectorCentrality.class, FAN_TYPE_SETTING, FanType.DEGREE_CENTRALITY_OUT_GLOBALLY,         false, 0},
            {EigenVectorCentrality.class, FAN_TYPE_SETTING, FanType.DEGREE_CENTRALITY_OUT_GLOBALLY,         true,  0},
            {EigenVectorCentrality.class, FAN_TYPE_SETTING, FanType.DEGREE_CENTRALITY_OUT_LOCALLY,          false, 0},
            {EigenVectorCentrality.class, FAN_TYPE_SETTING, FanType.DEGREE_CENTRALITY_OUT_LOCALLY,          true,  0},
            {EigenVectorCentrality.class, FAN_TYPE_SETTING, FanType.DEGREE_CENTRALITY_OUT_NO_STUB_GLOBALLY, false, 0},
            {EigenVectorCentrality.class, FAN_TYPE_SETTING, FanType.DEGREE_CENTRALITY_OUT_NO_STUB_GLOBALLY, true,  0},
            {EigenVectorCentrality.class, FAN_TYPE_SETTING, FanType.DEGREE_CENTRALITY_OUT_NO_STUB_LOCALLY,  false, 0},
            {EigenVectorCentrality.class, FAN_TYPE_SETTING, FanType.DEGREE_CENTRALITY_OUT_NO_STUB_LOCALLY,  true,  0},
            {EigenVectorCentrality.class, FAN_TYPE_SETTING, 
                FanType.DEGREE_CENTRALITY_OUT_NO_EXTERNAL_VPS_GLOBALLY, false, 0},
            {EigenVectorCentrality.class, FAN_TYPE_SETTING, 
                FanType.DEGREE_CENTRALITY_OUT_NO_EXTERNAL_VPS_GLOBALLY, true,  0},
            {EigenVectorCentrality.class, FAN_TYPE_SETTING,
                FanType.DEGREE_CENTRALITY_OUT_NO_EXTERNAL_VPS_LOCALLY, false, 0},
            {EigenVectorCentrality.class, FAN_TYPE_SETTING,
                FanType.DEGREE_CENTRALITY_OUT_NO_EXTERNAL_VPS_LOCALLY, true,  0},
            {EigenVectorCentrality.class, FAN_TYPE_SETTING,
                FanType.DEGREE_CENTRALITY_OUT_NO_STUB_NO_EXTERNAL_VPS_GLOBALLY, false, 0},
            {EigenVectorCentrality.class, FAN_TYPE_SETTING,
                FanType.DEGREE_CENTRALITY_OUT_NO_STUB_NO_EXTERNAL_VPS_GLOBALLY, true,  0},
            {EigenVectorCentrality.class, FAN_TYPE_SETTING,
                FanType.DEGREE_CENTRALITY_OUT_NO_STUB_NO_EXTERNAL_VPS_LOCALLY, false, 0},
            {EigenVectorCentrality.class, FAN_TYPE_SETTING,
                FanType.DEGREE_CENTRALITY_OUT_NO_STUB_NO_EXTERNAL_VPS_LOCALLY, true,  0},
            
            // Undisciplined CPP
            {UndisciplinedPreprocessorUsage.class, null, null, false, 0},
        });
    }
    
    /**
     * Initializes the properties and sets the variability model for {@link PseudoVariabilityExtractor}.
     */
    @BeforeClass
    public static void setup() {
        // Configuration of variability model
        HierarchicalVariable var1 = new HierarchicalVariable("FACTORIAL", "bool");
        HierarchicalVariable var2 = new HierarchicalVariable("ITERATIVE", "bool");
        
        var2.setParent(var1);
        
        PseudoVariabilityExtractor.configure(new File("mocked_varModel.dimacs"), var1, var2);
        PseudoVariabilityExtractor.setAttributes(Attribute.HIERARCHICAL);
        
    }

    @Override
    protected String getMetric() {
        return metric.getName();
    }
    
    @Override
    protected Map<String, MetricResult> runMetricAsMap(File file, Properties properties) {
        return super.runMetricAsMap(file, properties, false, true, true);
    }

    /**
     * Executes the specified tests.
     */
    @Test
    public void test() {
        super.test(baseSetting);
    }
}
