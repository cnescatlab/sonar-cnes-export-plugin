package fr.sonar.plugins.scan.tests;

import fr.cnes.sonar.plugins.export.ws.CnesWs;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.server.ws.WebService;
import org.sonar.api.server.ws.WsTester;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Test for the CnesWs class
 * @author lequal
 */
public class CnesWsTest {

	/**
	 * Just a controller to test web service with stubbed data
	 */
    private WebService.Controller controller;

    /**
     * Executed each time before running a single test
     */
    @Before
    public void prepare() {
        final WebService ws = new CnesWs();

        // WsTester is available in the Maven artifact
        // org.codehaus.sonar:sonar-plugin-api
        // with type "test-jar"
        final WsTester tester = new WsTester(ws);
        controller = tester.controller("api/cnesexport");
    }

    /**
     * Check that the controller has correct parameters
     */
    @Test
    public void controllerTest() {
        assertThat(controller).isNotNull();
        assertThat(controller.path()).isEqualTo("api/cnesexport");
        assertThat(controller.description()).isNotEmpty();
        assertThat(controller.actions().size()).isEqualTo(1);
    }

    /**
     * Check export web service
     * Assert that the key and parameters' number is correct
     */
    @Test
    public void exportWebServiceTest() {
        final WebService.Action getTree = controller.action("export");
        assertThat(getTree).isNotNull();
        assertThat(getTree.key()).isEqualTo("export");
        assertThat(getTree.params().size()).isEqualTo(1);
    }
}
