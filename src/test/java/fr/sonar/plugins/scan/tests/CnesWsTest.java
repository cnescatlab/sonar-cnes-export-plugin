package fr.sonar.plugins.scan.tests;

import fr.cnes.sonar.plugins.export.ws.CnesWs;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.server.ws.WebService;
import org.sonar.api.server.ws.WsTester;

import static org.junit.Assert.*;

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
        assertNotNull(controller);
        assertEquals("api/cnesexport", controller.path());
        assertFalse(controller.description().isEmpty());
        assertEquals(1, controller.actions().size());
    }

    /**
     * Check export web service
     * Assert that the key and parameters' number is correct
     */
    @Test
    public void exportWebServiceTest() {
        final WebService.Action getTree = controller.action("export");
        assertNotNull(getTree);
        assertEquals("export", getTree.key());
        assertEquals(1, getTree.params().size());
    }
}
