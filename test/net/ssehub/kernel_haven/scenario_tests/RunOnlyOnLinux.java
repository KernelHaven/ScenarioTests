package net.ssehub.kernel_haven.scenario_tests;

import java.util.Locale;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

/**
 * May be used to specify that a certain test class runs only on <b>Linux</b> systems.
 * Usage: <tt>@RunWith(value = RunOnlyOnLinux.class)</tt>
 * @author El-Sharkawy
 *
 */
public class RunOnlyOnLinux extends BlockJUnit4ClassRunner {
    
    /**
     * Creates a BlockJUnit4ClassRunner to run {@code clazz}.
     * @param clazz The test class.
     * @throws InitializationError if the test class is malformed.
     */
    public RunOnlyOnLinux(Class<?> clazz) throws InitializationError {
        super(clazz);
    }

    @Override
    public void run(RunNotifier notifier) {
        String os = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
        if (os.indexOf("nux") >= 0) {
            super.run(notifier);            
        } else {
            System.err.println(this.getTestClass().getName() + " skipped because of wrong OS used.");
        }
    }
}
