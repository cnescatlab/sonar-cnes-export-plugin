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

package fr.cnes.sonar.plugins.export.report.factory;

import fr.cnes.sonar.plugins.export.report.exceptions.BadSonarQubeRequestException;
import fr.cnes.sonar.plugins.export.report.model.QualityProfile;
import fr.cnes.sonar.plugins.export.report.providers.QualityProfileProvider;

import java.io.IOException;

/**
 * Construct  the report from resources providers
 * @author lequal
 */
public class ProfileFactory {

    private final String url;

    /**
     * Complete constructor
     * @param pUrl url of the server to request
     */
    public ProfileFactory(String pUrl) {
        this.url = pUrl;
    }

    /**
     * Create a report from program resources
     * @param profileKey key of the profile to export
     * @return A complete report resources model
     * @throws IOException on json problem
     * @throws BadSonarQubeRequestException when a request to the server is not well-formed
     */
    public QualityProfile create(String profileKey)
            throws IOException, BadSonarQubeRequestException {

        // instantiation of providers
        final QualityProfileProvider qualityProfileProvider =
                new QualityProfileProvider(this.url);

        return qualityProfileProvider.getQualityProfile(profileKey);
    }

}
