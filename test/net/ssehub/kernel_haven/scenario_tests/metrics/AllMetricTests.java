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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import net.ssehub.kernel_haven.scenario_tests.metrics.weights.AllWeightsTests;

/**
 * All metric scenario tests.
 *
 * @author El-Sharkawy
 */
@RunWith(Suite.class)
@SuiteClasses({
    // Lines of Code variations
    LoCTests.class,
    
    // Tangling Degree variations (including weight usage)
    
    // Nesting depth tests
    NestingDepthTests.class,
    
    // McCabe Tests
    McCabeTests.class,
    McCabeOnVpTests.class,
    
    // Variables per Function Tests
    VariablesPerFunctionAllTests.class,
    VariablesPerFunctionExternalTests.class,
    VariablesPerFunctionInternalTests.class,
    
    // Fan-In/Out
    FanInOutTests.class,
    DCFanInOutTests.class,
    DCFanInOutScenarioTests.class,
    
    // EigenVector Centrality
    EigenVectorCentralityTests.class,
    
    // UndisciplinedCPP
    UndisciplinedPreprocessorUsageTests.class,
    
    // Testing all metric variations on factorial use case
    SimpleAtomicSetTest.class,
    
    RobostnessTest.class,
    
    AllWeightsTests.class,
    })
public class AllMetricTests {
    

}
