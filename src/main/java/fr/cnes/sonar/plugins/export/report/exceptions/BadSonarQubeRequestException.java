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

package fr.cnes.sonar.plugins.export.report.exceptions;

/**
 * Thrown when a request is not recognize by SonarQube
 * @author lequal
 */
public class BadSonarQubeRequestException extends Exception {

    /**
     * Constructor
     * @param message the text to print (exception's details)
     */
    public BadSonarQubeRequestException(final String message) {
        super(message);
    }
}
