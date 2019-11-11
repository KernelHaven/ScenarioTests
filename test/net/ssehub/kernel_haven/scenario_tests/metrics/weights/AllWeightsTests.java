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
package net.ssehub.kernel_haven.scenario_tests.metrics.weights;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * All weight scenario tests.
 *
 * @author El-Sharkawy
 */
@RunWith(Suite.class)
@SuiteClasses({
    CTCRAtomicTests.class,
    CTCRTests.class,
    FeatureDistanceTests.class,
    VariabilityTypeMeasureTest.class,
    HierarchyWeightTest.class,
    StructureWeightTest.class,
    ScatteringWeightTest.class,
    FeatureSizeTest.class,
    })
public class AllWeightsTests {
    

}
