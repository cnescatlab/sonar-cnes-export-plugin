/*
 * This file is part of cnesexport.
 *
 * cnesexport is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * cnesexport is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with cnesexport.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.cnes.sonar.plugins.export.ws;

import fr.cnes.sonar.plugins.export.tasks.ExportTask;
import fr.cnes.sonar.plugins.export.utils.StringManager;
import org.sonar.api.server.ws.WebService;

/**
 * Expose CNES plugin api
 * @author lequal
 */
public class CnesWs implements WebService {

    /**
     * Define the new web service
     * Define each controller and action
     * @param context Context of the WebService
     */
    @Override
    public void define(final Context context) {
        // create the new controller for the cnes web service
        final NewController controller = context.createController(
                StringManager.string(StringManager.CNES_CTRL_KEY));
        // set minimal sonarqube version required
        controller.setSince(StringManager.string(StringManager.SONAR_VERSION));
        // set description of the controller
        controller.setDescription(StringManager.string(StringManager.CNES_CTRL_DESCRIPTION));

        // create the action for URL /api/cnesexport/export
        exportAction(controller);

        // important to apply changes
        controller.done();
    }

    /**
     * Add the action corresponding to the report generation
     * @param controller controller to which add the action
     */
    private void exportAction(final NewController controller) {
        final NewAction report = controller.createAction(
                StringManager.string(StringManager.EXPORT_KEY));
        report.setDescription(StringManager.string(StringManager.EXPORT_DESC));
        report.setSince(StringManager.string(StringManager.SONAR_VERSION));
        report.setHandler(new ExportTask());
        // add the parameters of the controller
        // key parameter
        NewParam newParam = report.createParam(
                StringManager.string(StringManager.CNES_ACTION_EXPORT_PARAM_KEY_NAME));
        newParam.setDescription(StringManager.string(StringManager.CNES_ACTION_EXPORT_PARAM_KEY_DESC));
        newParam.setRequired(true);
    }

}