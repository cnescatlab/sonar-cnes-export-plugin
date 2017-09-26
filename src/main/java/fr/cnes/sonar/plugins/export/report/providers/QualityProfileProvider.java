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

package fr.cnes.sonar.plugins.export.report.providers;

import com.google.gson.JsonObject;
import fr.cnes.sonar.plugins.export.report.exceptions.BadSonarQubeRequestException;
import fr.cnes.sonar.plugins.export.report.model.QualityProfile;
import fr.cnes.sonar.plugins.export.report.model.Rule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Provides quality gates
 * @author lequal
 */
public class QualityProfileProvider extends AbstractDataProvider {

    /**
     * Property for the request to get a specific quality profile
     */
    private static final String GET_QUALITY_PROFILE_REQUEST = "request.qualityprofile.get";
    /**
     * Property for the request to get a specific rule
     */
    private static final String GET_RULE_REQUEST = "request.rule.get";

    /**
     * Complete constructor
     * @param pUrl Represents the url of the current SonarQube instance
     */
    public QualityProfileProvider(String pUrl) {
        super(pUrl);
    }

    /**
     * Intern class to gather data from web service
     */
    private static class ActiveRule {
        public String qProfile;
        public String severity;

        /**
         * Find a rule with its profile's key
         * @param profileKey key of the profile
         * @return the rule or null if not found
         */
         public static ActiveRule find(ActiveRule[] rules, String profileKey) {
            // initialization of the result
             ActiveRule rule = null;

            // iterator on profile's rules
            final Iterator<ActiveRule> iterator = Arrays.asList(rules).iterator();

            // search for the rule with the asking key
            while(iterator.hasNext() && rule==null) {
                // get current rule
                final ActiveRule r = iterator.next();
                // check the current rule's profile's key equals to wanted key
                if(r.qProfile.equals(profileKey)) {
                    rule = r;
                }
            }

            return rule;
        }
    }

    /**
     * Get a specific quality profile
     * @param profileKey The key corresponding to the wanted quality profile
     * @return The chosen quality profile
     * @throws IOException when connecting the server
     * @throws BadSonarQubeRequestException A request is not recognized by the server
     */
    public QualityProfile getQualityProfile(String profileKey)
            throws IOException, BadSonarQubeRequestException {
        // declaration of the variable to return
        final QualityProfile res = new QualityProfile(new Rule[0]);

        // Will contain the requests
        String request;
        // Will contain the json response of requests
        JsonObject jsonObject;

        // get the rules of the profile
        // stop condition
        boolean goon = true;
        // page result index
        int page = 1;
        // contain the resulted rules
        final List<Rule> rules = new ArrayList<>();
        // continue until there are no more results
        while(goon) {
            // Get all quality profiles (metadata)
            request = String.format(getRequest(GET_QUALITY_PROFILE_REQUEST),
                    getUrl(), profileKey,
                    Integer.valueOf(getRequest(MAX_PER_PAGE_SONARQUBE)), page);
            // perform the previous request to sonarqube server
            jsonObject = request(request);
            // convert json to Rule objects
            final Rule [] tmp = (getGson().fromJson(jsonObject.get(RULES), Rule[].class));
            // add rules to the result list
            rules.addAll(Arrays.asList(tmp));

            // check if there are other pages
            final int number = (jsonObject.get(TOTAL).getAsInt());
            goon = page* Integer.valueOf(getRequest(MAX_PER_PAGE_SONARQUBE)) < number;
            page++;
        }
        // set rules in the result
        Rule[] tmpRules = new Rule[rules.size()];
        rules.toArray(tmpRules);
        res.setRules(tmpRules);
        // set the profile's key
        res.setKey(profileKey);

        // get all active severities of rules in current profile
        for (Rule rule : rules) {
            // get active severity
            request = String.format(getRequest(GET_RULE_REQUEST),
                    getUrl(), rule.getKey());
            // perform a request
            jsonObject = request(request);
            // convert json to Project objects
            final ActiveRule[] activeRules = (getGson().fromJson(jsonObject.get(ACTIVES), ActiveRule[].class));

            // retrieve corresponding rule
            ActiveRule activeRule = ActiveRule.find(activeRules, profileKey);
            // set active severity for the rule
            if(activeRule!=null) {
                rule.setActiveSeverity(activeRule.severity);
            }
        }

        return res;
    }
}
