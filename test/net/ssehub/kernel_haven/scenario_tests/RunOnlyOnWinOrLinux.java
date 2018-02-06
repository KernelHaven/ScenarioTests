package net.ssehub.kernel_haven.scenario_tests;

import org.junit.runners.model.InitializationError;

/**
 * May be used to specify that a certain test class runs only on <b>Windows</b> or <b>Linux</b> systems.
 * For instance, srcML-Extractor currently runs only on these two systems.
 * Usage: <tt>@RunWith(value = RunOnlyOnWinOrLinux.class)</tt>
 * @author El-Sharkawy
 *
 */
public class RunOnlyOnWinOrLinux extends AbstractOsSpecificTestRunner {
    
    /**
     * Creates a BlockJUnit4ClassRunner to run {@code clazz}.
     * @param clazz The test class.
     * @throws InitializationError if the test class is malformed.
     */
    public RunOnlyOnWinOrLinux(Class<?> clazz) throws InitializationError {
        super(clazz);
    }

    @Override
    protected boolean isSupportedOS(String os) {
        boolean isLinuxOS = os.indexOf("nux") >= 0;
        boolean isWindowsOS = os.startsWith("windows");
        
        return isLinuxOS || isWindowsOS;
    }
}
