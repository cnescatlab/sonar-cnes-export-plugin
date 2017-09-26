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
package fr.cnes.sonar.plugins.export.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Centralized the management of strings
 *
 * @author lequal
 */
public final class StringManager {

    /**
     * Property for controller description
     */
    public static final String CNES_CTRL_DESCRIPTION = "cnes.ctrl.description";
    /**
     * Property for controller key
     */
    public static final String CNES_CTRL_KEY = "cnes.ctrl.key";
    /**
     * Property for action 2 (reporting) param 1 name
     */
    public static final String CNES_ACTION_EXPORT_PARAM_KEY_NAME =
            "cnes.action.export.param.key.name";
    /**
     * Define the name of the author parameter
     */
    public static final String CNES_ACTION_REPORT_PARAM_AUTHOR_NAME =
            "cnes.action.report.param.author.name";
    /**
     * Property for action 2 (reporting) response's field 1
     */
    public static final String EXPORT_RESPONSE_LOG = "cnes.action.export.response.log";
    /**
     * Property for action 2 (reporting) param 1 description
     */
    public static final String CNES_ACTION_EXPORT_PARAM_KEY_DESC =
            "cnes.action.export.param.key.desc";
    /**
     * Property for action 2 (reporting) param 3 description
     */
    public static final String REPORT_NAME_DESC = "cnes.action.report.param.name.desc";
    /**
     * Property for action 2 (reporting) param 4 description
     */
    public static final String REPORT_AUTHOR_DESC = "cnes.action.report.param.author.desc";
    /**
     * Property for action 2 (reporting) key
     */
    public static final String EXPORT_KEY = "cnes.action.export.key";
    /**
     * Property for action 2 (reporting) description
     */
    public static final String EXPORT_DESC = "cnes.action.export.desc";
    /**
     * Property name of the command pattern to report an export
     */
    public static final String CNES_COMMAND_REPORT = "cnes.command.report";
    /**
     * Property name of SonarQube server url
     */
    public static final String SONAR_URL = "sonar.url";
    /**
     * Property name of output
     */
    public static final String CNES_REPORT_PATH = "cnes.reporter.path";
    /**
     * Property name of results' folder
     */
    public static final String CNES_REPORTS_FOLDER = "cnes.reports.folder";
    /**
     * Property for error message when it was impossible to create a directory
     */
    public static final String CNES_MKDIR_ERROR = "cnes.mkdir.error";
    /**
     * Property name of the date pattern
     */
    public static final String DATE_PATTERN = "date.pattern";
    /**
     * Property name of export page key
     */
    public static final String CNES_PAGE_EXPORT_KEY = "cnes.page.export.key";
    /**
     * Property name of export page name
     */
    public static final String CNES_PAGE_EXPORT_NAME = "cnes.page.export.name";
    /**
     * Define the minimal version of sonarqube
     */
    public static final String SONAR_VERSION = "sonar.version";
    /**
     * Path where the report must be exported
     */
    public static final String CNES_REPORTER_OUTPUT = "cnes.reporter.output";
    /**
     * Template to use
     */
    public static final String CNES_REPORTER_TEMPLATE = "cnes.reporter.template";
    /**
     * Template to use for xlsx
     */
    public static final String CNES_ISSUES_TEMPLATE = "cnes.issues.template";
    /**
     * Separator between two log entries
     */
    public static final String CNES_LOG_SEPARATOR = "cnes.log.separator";
    /**
     * Default string to return when a key is unknown
     */
    public static final String DEFAULT_STRING = "unknown string";
    /**
     * Just a new line
     */
    public static final String NEW_LINE = "\n";
    /**
     * Logger of this class
     */
    private static final Logger LOGGER = Logger.getLogger(StringManager.class.getName());
    /**
     * Properties file for the current plugin
     */
    private static final String PLUGIN_PROPERTIES = "strings.properties";
    /**
     * Just a space character
     */
    public static final String SPACE = " ";
    /**
     * Unique instance of this class (singleton)
     */
    private static StringManager ourInstance = null;
    /**
     * Gather all the properties concerning the plugin
     */
    private Properties properties = new Properties();

    /**
     * Private constructor to make a singleton of this class
     */
    private StringManager() {
        try {
            load();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * Get the singleton
     *
     * @return unique instance of StringManager
     */
    public static synchronized StringManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new StringManager();
        }
        return ourInstance;
    }

    /**
     * Get the value of a property through its key
     *
     * @param key Key of the string to string
     * @return the property as String or the DEFAULT_STRING
     */
    public static String string(final String key) {
        return getInstance().getProperty(key, DEFAULT_STRING);
    }

    /**
     * Get the value of a property through its key
     *
     * @param key Key of the string to string
     * @param defaultString Default value to return
     * @return the property as String or the DEFAULT_STRING
     */
    private String getProperty(final String key, final String defaultString) {
        return this.properties.getProperty(key, defaultString);
    }

    /**
     * load properties from a specific file
     *
     * @throws IOException when an error occurred during file reading
     */
    private void load() throws IOException {
        // store properties
        this.properties = new Properties();

        final ClassLoader classLoader = StringManager.class.getClassLoader();

        // read the file
        // load properties file as a stream
        try (InputStream input = classLoader.getResourceAsStream(PLUGIN_PROPERTIES)) {
            if (input != null) {
                // load properties from the stream in an adapted structure
                this.properties.load(input);
            }
        }
    }
}
