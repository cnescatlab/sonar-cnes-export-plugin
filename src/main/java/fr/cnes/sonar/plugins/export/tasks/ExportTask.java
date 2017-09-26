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
package fr.cnes.sonar.plugins.export.tasks;

import fr.cnes.sonar.plugins.export.report.exceptions.BadSonarQubeRequestException;
import fr.cnes.sonar.plugins.export.report.factory.ProfileFactory;
import fr.cnes.sonar.plugins.export.report.model.QualityProfile;
import fr.cnes.sonar.plugins.export.utils.StringManager;
import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.Response;
import org.sonar.api.utils.text.JsonWriter;

import java.io.IOException;

/**
 * Execute element to produce the report
 * @author lequal
 */
public class ExportTask extends AbstractTask {

    /**
     * Product the report
     * @param profileId Key of the profile to export
     * @return logs of the task
     * @throws IOException when contacting the server
     * @throws BadSonarQubeRequestException malformed request from the client
     */
    public QualityProfile export(final String profileId)
            throws IOException, BadSonarQubeRequestException {

        // create a factory
        ProfileFactory factory = new ProfileFactory("http://localhost:9000");

        return factory.create(profileId);
    }

    /**
     * Use the user's request to start the report generation
     * @param request request coming from the user
     * @param response response to send to the user
     * @throws IOException when contacting the server
     * @throws BadSonarQubeRequestException on bad requests
     */
    @Override
    public void handle(final Request request, final Response response)
            throws IOException, BadSonarQubeRequestException {
        // reset logs to not stack them
        setLogs("");

        // Key of the project provided by the user through parameters
        final String profileKey = request.mandatoryParam(
                StringManager.string(StringManager.CNES_ACTION_EXPORT_PARAM_KEY_NAME));

        // read request parameters and generates response output
        // generate the reports and save output
        final QualityProfile result = export(profileKey);

        // set the response
        final JsonWriter jsonWriter = response.newJsonWriter();
        jsonWriter.beginObject();
        // add logs to response
        jsonWriter.prop(StringManager.string(StringManager.EXPORT_RESPONSE_LOG), result.toString());
        jsonWriter.endObject();
        jsonWriter.close();
    }
}
