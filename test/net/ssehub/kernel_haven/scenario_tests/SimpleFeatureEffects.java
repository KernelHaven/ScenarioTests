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
package net.ssehub.kernel_haven.scenario_tests;

import static net.ssehub.kernel_haven.util.logic.FormulaBuilder.and;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileInputStream;

import net.ssehub.kernel_haven.AbstractScenarioTest;
import net.ssehub.kernel_haven.logic_utils.FormulaEqualityChecker;
import net.ssehub.kernel_haven.util.io.csv.CsvReader;
import net.ssehub.kernel_haven.util.logic.Formula;
import net.ssehub.kernel_haven.util.logic.parser.CStyleBooleanGrammar;
import net.ssehub.kernel_haven.util.logic.parser.Parser;
import net.ssehub.kernel_haven.util.logic.parser.VariableCache;
import net.ssehub.kernel_haven.util.null_checks.NonNull;

/**
 * A scenario test that computes feature effects using the CodeBlockExtractor.
 *
 * @author Adam
 */
public class SimpleFeatureEffects extends AbstractScenarioTest {

    @Override
    protected void checkOutputDirResult(File dir) throws Exception {
        File[] files = dir.listFiles();
        assertThat(files.length, is(1));
        
        assertThat(files[0].getName(), startsWith("Analysis_"));
        assertThat(files[0].getName(), endsWith("_Feature Effects.csv"));
        
        try (CsvReader in = new CsvReader(new FileInputStream(files[0]))) {
            String[][] full = in.readFull();
            
            /*
             * PCs:
             * FileA:
             *     VAR_A
             *     VAR_A && !VAR_B
             *     VAR_A && (VAR_C || !VAR_D)
             * 
             * FileB:
             *     VAR_A
             *     VAR_D
             * 
             * => Feature Effects:
             * 
             * VAR_A: 1
             * VAR_B: VAR_A
             * VAR_C: VAR_A && VAR_D
             * VAR_D: 1
             */
            
            Parser<@NonNull Formula> parser = new Parser<>(new CStyleBooleanGrammar(new VariableCache()));
            
            assertThat(full[0], is(new String[] {"Variable", "Feature Effect"}));
            assertThat(full[1], is(new String[] {"VAR_A", "1"}));
            assertThat(full[2], is(new String[] {"VAR_B", "VAR_A"}));
            assertThat(full[3][0], is("VAR_C"));
            assertThat(new FormulaEqualityChecker().isLogicallyEqual(and("VAR_A", "VAR_D"), parser.parse(full[3][1])),
                    is(true));
            assertThat(full[4], is(new String[] {"VAR_D", "1"}));
            
            
            assertThat(full.length, is(5));
        }
        
    }

}
