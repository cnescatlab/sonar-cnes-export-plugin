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

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

/**
 * Provides issue items
 * @author lequal
 */
public final class RequestManager {

    /**
     * Encoding for http content
     */
    private static final String UTF_8 = "UTF-8";
    /**
     * Header http for setting the content type of a request
     */
    private static final String CONTENT_TYPE = "content-type";
    /**
     * Json type for a content
     */
    private static final String APPLICATION_JSON = "application/json";

    /**
     * Instance of the singleton
     */
    private static RequestManager ourInstance = null;

    /**
     * Use of private constructor to singletonize this class
     */
    private RequestManager() {
    }

    /**
     * Return the unique instance
     * @return the singleton
     */
    public static synchronized RequestManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new RequestManager();
        }
        return ourInstance;
    }

    /**
     * Execute a get http request
     * @param url url to request
     * @return response as string
     * @throws IOException error on response
     */
    public String get(String url) throws IOException {
        // returned string containing the response as raw string
        final String toReturn;
        // create a client
        final CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // set the request
        final HttpGet request = new HttpGet(url);
        // set content type to json
        request.addHeader(CONTENT_TYPE, APPLICATION_JSON);
        // future result of the request
        final HttpResponse result;
        try {
            // execute the request
            result = httpClient.execute(request);
            // convert to string
            toReturn = EntityUtils.toString(result.getEntity(), UTF_8);
        } finally {
            // always close the connexion
            request.reset();
        }
        // return string result
        return toReturn;
    }

    /**
     * Execute a get http request
     * @param url url to request
     * @param data list of pairs containing resources to post
     * @return response as string
     * @throws IOException error on response
     */
    public String post(String url, List<NameValuePair> data) throws IOException {
        // create a client
        final CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // set the request
        final HttpPost request = new HttpPost(url);
        request.addHeader(CONTENT_TYPE, APPLICATION_JSON);
        request.setEntity(new UrlEncodedFormEntity(data));
        // future result of the request
        final HttpResponse result;
        try {
            // execute the request
            result = httpClient.execute(request);
        } finally {
            // always close the connexion
            request.reset();
        }

        // return string result
        return EntityUtils.toString(result.getEntity(), UTF_8);
    }
}
