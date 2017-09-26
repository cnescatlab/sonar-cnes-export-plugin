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

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.cnes.sonar.plugins.export.report.exceptions.BadSonarQubeRequestException;
import fr.cnes.sonar.plugins.export.utils.StringManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Generic interface for resources providers
 * @author lequal
 */
public abstract class AbstractDataProvider {

    /**
     * Name for properties' file about requests
     */
    public static final String REQUESTS_PROPERTIES = "requests.properties";
    /**
     *  Name of the property for the maximum number of results per page
     */
    public static final String MAX_PER_PAGE_SONARQUBE = "MAX_PER_PAGE_SONARQUBE";
    /**
     * Field to search in json to get the total page's number
     */
    public static final String TOTAL = "total";
    /**
     * Field to search in json to get rules
     */
    public static final String RULES = "rules";
    /**
     * Field to search in json to get active rules
     */
    public static final String ACTIVES = "actives";

    /**
     * Logger for the class
     */
    protected static final Logger LOGGER =
            Logger.getLogger(AbstractDataProvider.class.getCanonicalName());

    /**
     * Contain all the properties related to requests
     */
    private static Properties requests;

    /**
     * Tool for parsing json
     */
    private Gson gson;

    /**
     * Url of the sonarqube server
     */
    private String url;

    // Static initialization block for reading .properties
    static {
        // store properties
        requests = new Properties();
        // read the file
        InputStream input = null;

        final ClassLoader classLoader = AbstractDataProvider.class.getClassLoader();

        try {
            // load properties file as a stream
            input = classLoader.getResourceAsStream(REQUESTS_PROPERTIES);
            if(input!=null) {
                // load properties from the stream in an adapted structure
                requests.load(input);
            }
        } catch (IOException e) {
            // it logs all the stack trace
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            if(input!=null) {
                try {
                    // close the stream if necessary (not null)
                    input.close();
                } catch (IOException e) {
                    // it logs all the stack trace
                    LOGGER.log(Level.SEVERE, e.getMessage(), e);
                }
            }
        }
    }

    /**
     * Singleton which execute concrete http requests
     */
    private RequestManager requestManager;

    /**
     * Constructor
     * @param pUrl Represents the url of the current SonarQube instance
     */
    public AbstractDataProvider(final String pUrl) {
        // json tool
        this.gson = new Gson();
        // get sonar url
        this.url = pUrl;
        // set network tool to execute request
        this.requestManager = RequestManager.getInstance();
    }

    /**
     * Give the value of the property corresponding to the key passed as parameter.
     * It gives only properties related to requests.
     * @param property Key of the property you want.
     * @return The value of the property you want as a String.
     */
    public static String getRequest(final String property) {
        return requests.getProperty(property);
    }

    /**
     * Check if the server has sent an error
     * @param jsonObject The response from the server
     * @throws BadSonarQubeRequestException thrown if the server do not understand our request
     */
    private void isErrorFree(final JsonObject jsonObject) throws BadSonarQubeRequestException {
        // we retrieve the exception
        final JsonElement error = jsonObject.get("errors");
        // if there is an error we search the message and throw an exception
        if (error != null) {
            // Json object of the error
            final JsonObject errorJO = error.getAsJsonArray().get(0).getAsJsonObject();
            // get the error message
            final JsonElement errorElement = errorJO.get("msg");
            final String errorMessage = (getGson().fromJson(errorElement, String.class));
            // throw exception if there was a problem when dealing with the server
            throw new BadSonarQubeRequestException(errorMessage);
        }
    }

    /**
     * Execute a given request
     * @param request Url for the request, for example http://sonarqube:1234/api/toto/list
     * @return Server's response as a JsonObject
     * @throws IOException if there were an error contacting the server
     * @throws BadSonarQubeRequestException if SonarQube Server sent an error
     */
    public JsonObject request(final String request)
            throws IOException, BadSonarQubeRequestException {
        // do the request to the server and return a string answer
        final String raw = stringRequest(request);

        // prepare json
        final JsonElement json;

        // verify that the server response was correct
        try {
            json = getGson().fromJson(raw, JsonElement.class);
        } catch (Exception e) {
            // log exception's message
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new BadSonarQubeRequestException("Server answered: " + raw +
                    StringManager.SPACE + e.getMessage());
        }

        // get the json object version
        final JsonObject jsonObject = json.getAsJsonObject();

        // verify if an error occurred
        isErrorFree(jsonObject);

        return jsonObject;
    }

    /**
     * Get the raw string response
     * @param request the raw url of the request
     * @return the server's response as a string
     * @throws IOException when not able to contact the server
     */
    protected String stringRequest(final String request) throws IOException {
        // prepare the request by replacing some relevant special characters
        // replace spaces
        String preparedRequest = request.replaceAll(" ", "%20");
        // replace + characters
        preparedRequest = preparedRequest.replaceAll("\\+", "%2B");

        // launch the request on sonarqube server and retrieve resources into a string
        return RequestManager.getInstance().get(preparedRequest);
    }

    /**
     * Json parsing tool
     * @return the gson tool
     */
    public Gson getGson() {
        return gson;
    }

    /**
     * Setter of gson
     * @param pGson value
     */
    public void setGson(final Gson pGson) {
        this.gson = pGson;
    }

    /**
     * Name of the project to report
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Setter of url
     * @param pUrl value
     */
    public void setUrl(final String pUrl) {
        this.url = pUrl;
    }
}
