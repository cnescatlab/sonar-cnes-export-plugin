package fr.sonar.plugins.scan.tests;

import fr.cnes.sonar.plugins.export.CnesExportPlugin;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.Plugin;
import org.sonar.api.SonarQubeSide;
import org.sonar.api.SonarRuntime;
import org.sonar.api.internal.SonarRuntimeImpl;
import org.sonar.api.utils.Version;

import static org.junit.Assert.assertEquals;

/**
 * Test for the CnesExportPlugin class
 * @author lequal
 */
public class CnesExportPluginTest {

    /**
     * Instance of the plugin to test
     */
    private CnesExportPlugin cnesExportPlugin;

    /**
     * Prepare each test by creating a new CnesExportPlugin
     */
    @Before
    public void prepare() {
        cnesExportPlugin = new CnesExportPlugin();
    }

    /**
     * Assert that the plugin subscribe correctly to SonarQube
     * by checking the good number of extensions.
     */
    @Test
    public void sonarqubePluginDefinitionTest() {
        final SonarRuntime runtime = SonarRuntimeImpl.forSonarQube(Version.create(6,3), SonarQubeSide.SERVER);
        final Plugin.Context context = new Plugin.Context(runtime);
        cnesExportPlugin.define(context);
        assertEquals(2, context.getExtensions().size());
    }

}
