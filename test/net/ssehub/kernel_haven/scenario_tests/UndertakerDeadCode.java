package net.ssehub.kernel_haven.scenario_tests;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import net.ssehub.kernel_haven.util.io.csv.CsvReader;

/**
 * Tests a dead code analysis on a pseudo Linux with Undertaker, KbuildMiner and KconfigReader extractors. 
 *
 * @author Adam
 */
public class UndertakerDeadCode extends AbstractScenarioTest {

    @Override
    protected File getPropertiesFile() {
        return new File("testdata/UndertakerDeadCode/config.properties");
    }
    
    @Override
    protected File getSourceTree() {
        return new File("testdata/UndertakerDeadCode/dead_code_linux");
    }
    
    @Override
    protected void checkOutputDirResult(File dir) throws IOException {
        File[] files = dir.listFiles();
        assertThat(files.length, is(1));
        
        assertThat(files[0].getName(), startsWith("Analysis_"));
        assertThat(files[0].getName(), endsWith("_Dead Code Blocks.csv"));
        
        try (CsvReader in = new CsvReader(new FileInputStream(files[0]))) {
            String[][] full = in.readFull();
            
            /*
             * We expect the following output:
             * [Source File, File PC, Line Start, Line End, Presence Condition]
             * [src/main.c, CONFIG_MAIN || CONFIG_MAIN_MODULE, 4, 6, !(CONFIG_MAIN || CONFIG_MAIN_MODULE)]
             * [src/main.c, CONFIG_MAIN || CONFIG_MAIN_MODULE, 13, 15, CONFIG_MAIN && CONFIG_MAIN_MODULE]
             * [src/main.c, CONFIG_MAIN || CONFIG_MAIN_MODULE, 18, 20, CONFIG_A]
             * [src/main.c, CONFIG_MAIN || CONFIG_MAIN_MODULE, 23, 25, CONFIG_C && !CONFIG_B]
             */
            
            assertThat(full[0], is(new String[] {
                "Source File", "File PC", "Line Start", "Line End", "Presence Condition" }));
            assertThat(full[1], is(new String[] {
                "src/main.c", "CONFIG_MAIN || CONFIG_MAIN_MODULE", "4", "6",
                "!(CONFIG_MAIN || CONFIG_MAIN_MODULE)" }));
            assertThat(full[2], is(new String[] {
                "src/main.c", "CONFIG_MAIN || CONFIG_MAIN_MODULE", "13", "15",
                "CONFIG_MAIN && CONFIG_MAIN_MODULE" }));
            assertThat(full[3], is(new String[] {
                "src/main.c", "CONFIG_MAIN || CONFIG_MAIN_MODULE", "18", "20", "CONFIG_A" }));
            assertThat(full[4], is(new String[] {
                "src/main.c", "CONFIG_MAIN || CONFIG_MAIN_MODULE", "23", "25", "CONFIG_C && !CONFIG_B" }));
            assertThat(full.length, is(5));
        }
        
    }

}
