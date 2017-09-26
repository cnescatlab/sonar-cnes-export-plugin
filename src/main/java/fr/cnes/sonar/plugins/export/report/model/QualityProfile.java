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

package fr.cnes.sonar.plugins.export.report.model;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Contain all Quality Profile's details
 * @author lequal
 */
public class QualityProfile {
    /**
     * Header for the resulting file
     */
    private static final String CSV_HEADER = "Key;Name;Language;Repository;Severity;Type\n";
    /**
     * Column separator in csv
     */
    private static final String SEPARATOR = ";";
    /**
     * Line separator in CSV
     */
    private static final String NEW_LINE = "\n";
    /**
     * Contains resources like rules and configuration files
     */
    private Rule[] rules;
    /**
     * The key of the current quality profile
     */
    private String key;

    /**
     * Complete constructor
     * @param pRules verified rules by this profile
     */
    public QualityProfile(Rule[] pRules) {
        this.rules = pRules;
    }

    /**
     * Getter for rules
     * @return rules
     */
    public Rule[] getRules() {
        return rules.clone();
    }

    /**
     * Setter for rules
     * @param pRules value
     */
    public void setRules(Rule[] pRules) {
        this.rules = pRules.clone();
    }

    /**
     * Getter for key
     * @return key
     */
    public String getKey() {
        return this.key;
    }

    /**
     * Setter for key
     * @param pKey key to set
     */
    public void setKey(String pKey) {
        this.key = pKey;
    }

    /**
     * Find a rule with its key
     * @param key key of the rule
     * @return the rule or null if not found
     */
    public Rule find(String key) {
        // initialization of the result
        Rule rule = null;

        // iterator on profile's rules
        final Iterator<Rule> iterator = Arrays.asList(getRules()).iterator();

        // search for the rule with the asking key
        while(iterator.hasNext() && rule==null) {
            // get current rule
            final Rule r = iterator.next();
            // check the current rule's key equals to wanted key
            if(r.getKey().equals(key)) {
                rule = r;
            }
        }

        return rule;
    }

    /**
     * Override toString to retrieve information
     * @return a csv format
     */
    @Override
    public String toString() {
        // header added to csv
        StringBuilder builder = new StringBuilder(CSV_HEADER);

        // concat important information about rules
        for(Rule rule : rules) {
            builder.append(rule.getKey());
            builder.append(SEPARATOR);
            builder.append(rule.getName());
            builder.append(SEPARATOR);
            builder.append(rule.getLangName());
            builder.append(SEPARATOR);
            builder.append(rule.getRepo());
            builder.append(SEPARATOR);
            builder.append(rule.getActiveSeverity());
            builder.append(SEPARATOR);
            builder.append(rule.getType());
            builder.append(NEW_LINE);
        }

        return builder.toString();
    }
}
