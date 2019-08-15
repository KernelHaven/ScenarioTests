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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import net.ssehub.kernel_haven.metric_haven.MetricResult;
import net.ssehub.kernel_haven.metric_haven.code_metrics.TanglingDegree;
import net.ssehub.kernel_haven.metric_haven.code_metrics.TanglingDegree.TDType;
import net.ssehub.kernel_haven.metric_haven.metric_components.config.HierarchyType;
import net.ssehub.kernel_haven.metric_haven.metric_components.config.MetricSettings;
import net.ssehub.kernel_haven.metric_haven.metric_components.weights.IVariableWeight;
import net.ssehub.kernel_haven.test_utils.PseudoVariabilityExtractor;
import net.ssehub.kernel_haven.variability_model.HierarchicalVariable;
import net.ssehub.kernel_haven.variability_model.VariabilityModelDescriptor.Attribute;

/**
 * Tests metrics execution of Tangling degree metrics (all variations) with srcML-Extractor.
 * @author El-Sharkawy
 *
 */
@RunWith(Parameterized.class)
public class TDTests extends AbstractParameterizedTests {
    
    private static final Map<TDType, Properties> SETTINGS_WITHOUTWEIGHTS;
    private static final Map<TDType, Properties> SETTINGS_WITHWEIGHTS;
    
    static {
        Map<TDType, Properties> settingsWithout = new HashMap<>();
        Map<TDType, Properties> settingsWith = new HashMap<>();
        
        Properties prop = new Properties();
        prop.setProperty(MetricSettings.TD_TYPE_SETTING.getKey(), TDType.TD_ALL.name());
        settingsWithout.put(TDType.TD_ALL, prop);
        
        prop = new Properties();
        prop.setProperty(MetricSettings.TD_TYPE_SETTING.getKey(), TDType.TD_ALL.name());
        prop.setProperty(MetricSettings.HIERARCHY_TYPE_MEASURING_SETTING.getKey(),
            HierarchyType.HIERARCHY_WEIGHTS_BY_FILE.name());
        prop.setProperty(MetricSettings.HIERARCHY_WEIGHTS_SETTING.getKey(),
            "top:42, leaf:21, intermediate:3");
        settingsWith.put(TDType.TD_ALL, prop);
        
        prop = new Properties();
        prop.setProperty(MetricSettings.TD_TYPE_SETTING.getKey(), TDType.TD_VISIBLE.name());
        settingsWithout.put(TDType.TD_VISIBLE, prop);
        
        prop = new Properties();
        prop.setProperty(MetricSettings.TD_TYPE_SETTING.getKey(), TDType.TD_VISIBLE.name());
        prop.setProperty(MetricSettings.HIERARCHY_TYPE_MEASURING_SETTING.getKey(),
            HierarchyType.HIERARCHY_WEIGHTS_BY_FILE.name());
        prop.setProperty(MetricSettings.HIERARCHY_WEIGHTS_SETTING.getKey(),
            "top:42, leaf:21, intermediate:3");
        settingsWith.put(TDType.TD_VISIBLE, prop);
        
        SETTINGS_WITHOUTWEIGHTS = Collections.unmodifiableMap(settingsWithout);
        SETTINGS_WITHWEIGHTS = Collections.unmodifiableMap(settingsWith);
    }
    
    private Properties baseSetting;

    /**
     * Retrieves values from {@link #getParameters()}, creates, and executes the test.
     * @param testedFunctionName The function to test
     * @param expectedLineNo The expected starting line number of the function
     * @param expectedResultValue The expected value of the metric to compute.
     * @param type The tangling degree type to be used for the test
     * @param useWeight If true a weight is used with (A = 42, B = 21, all other = 3) to test
     *     combination of metric with {@link IVariableWeight}s.
     */
    public TDTests(String testedFunctionName, int expectedLineNo, double expectedResultValue,
        TDType type, boolean useWeight) {
        
        super("VariabilityFunctions4TD.c", testedFunctionName, expectedLineNo, expectedResultValue);
        
        // Avoid creation of new properties to support caching of results
        baseSetting = useWeight ? SETTINGS_WITHWEIGHTS.get(type) : SETTINGS_WITHOUTWEIGHTS.get(type);
    }
    
    /**
     * Initializes the properties and sets the variability model for {@link PseudoVariabilityExtractor}.
     */
    @BeforeClass
    public static void setup() {
        // Configuration of variability model
        HierarchicalVariable varA = new HierarchicalVariable("A", "bool");
        HierarchicalVariable varB = new HierarchicalVariable("B", "bool");
        HierarchicalVariable varC = new HierarchicalVariable("C", "bool");
        HierarchicalVariable varD = new HierarchicalVariable("D", "bool");
        
        // A is parent, B is leaf, all other a intermediate
        varC.setParent(varA);
        varD.setParent(varC);
        varB.setParent(varD);
        
        PseudoVariabilityExtractor.configure(new File("mocked_varModel.dimacs"), varA, varB, varC, varD);
        PseudoVariabilityExtractor.setAttributes(Attribute.HIERARCHICAL);
        
    }
    
    /**
     * Creates the parameters for this test.
     * 
     * @return The parameters of this test.
     */
    // CHECKSTYLE:OFF
    @Parameters(name = "{3}: {0} with weight = {4}")
    public static Collection<Object[]> getParameters() {
    // CHECKSTYLE:ON
        return Arrays.asList(new Object[][] {
            // Full TD (no weights)
            {"funcStub"       ,  1, 0, TDType.TD_ALL, false},
            {"funcEmpty"      ,  3, 0, TDType.TD_ALL, false},
            {"funcOneIfDef"   ,  7, 1, TDType.TD_ALL, false},
            {"funcNested1"    , 13, 2, TDType.TD_ALL, false},
            {"funcNested2"    , 22, 3, TDType.TD_ALL, false},
            {"funcNested3"    , 34, 3, TDType.TD_ALL, false},
            {"funcNested4"    , 45, 4, TDType.TD_ALL, false},
            {"funcElif"       , 58, 5, TDType.TD_ALL, false},
            {"funcElif2"      , 68, 9, TDType.TD_ALL, false},
            {"funcElifNested1", 80, 4, TDType.TD_ALL, false},
            {"funcElifNested2", 92, 8, TDType.TD_ALL, false},
            
            // Full TD (with weights)
            {"funcStub"       ,  1,   0, TDType.TD_ALL, true},
            {"funcEmpty"      ,  3,   0, TDType.TD_ALL, true},
            {"funcOneIfDef"   ,  7,  42, TDType.TD_ALL, true},
            {"funcNested1"    , 13,  63, TDType.TD_ALL, true},
            {"funcNested2"    , 22, 105, TDType.TD_ALL, true},
            {"funcNested3"    , 34,  84, TDType.TD_ALL, true},
            {"funcNested4"    , 45, 126, TDType.TD_ALL, true},
            {"funcElif"       , 58, 168, TDType.TD_ALL, true},
            {"funcElif2"      , 68, 237, TDType.TD_ALL, true},
            {"funcElifNested1", 80,  87, TDType.TD_ALL, true},
            {"funcElifNested2", 92, 177, TDType.TD_ALL, true},
        });
    }
    
    @Override
    protected String getMetric() {
        return TanglingDegree.class.getName();
    }
    
    @Override
    protected Map<String, MetricResult> runMetricAsMap(File file, Properties properties) {
        return super.runMetricAsMap(file, properties, false, true, true);
    }
    
    /**
     * Executes the LoF tests.
     */
    @Test
    public void test() {
        super.test(baseSetting);
    }
    
}
