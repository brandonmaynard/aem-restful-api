package com.brandonmaynard.restful.utils;

import com.brandonmaynard.restful.exceptions.RestClientException;
import com.brandonmaynard.restful.models.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link Response} utility methods.
 * @see <a href="https://github.com/brandonmaynard/aem-restful-api">aem-rest-api</a> GitHub repository for more information.
 * @author Brandon Maynard - https://www.linkedin.com/in/brandonmaynard/
 */
public final class ResponseUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ResponseUtils.class);

    /**
     * Private constructor.
     */
    private ResponseUtils() {
        // Do Nothing.
    }

    /**
     * Get the supplied Response's body JSON.
     * @param response Response
     * @return JSONObject
     */
    public static JSONObject getResponseBodyJson(final Response response) {
        try {
            return new JSONObject(response.getBody());
        } catch (JSONException e) {
            LOG.error("Unable to get body JSON. Raw response:\n{}", response.getBody(), e);
        }

        return new JSONObject();
    }

    /**
     * Get the access token from the authentication response body's JSON.
     * @param valueName value's name
     * @param authenticationJson Authentication response body's JSON
     * @return String
     */
    public static String getValueFromJson(final String valueName, final JSONObject authenticationJson) {
        try {
            return authenticationJson.getString(valueName);
        } catch (JSONException e) {
            LOG.error("Unable to grab value from supplied JSON. Full JSON output:\n{}", authenticationJson.toString(), e);
        }

        return StringUtils.EMPTY;
    }

    /**
     * Validate that response is ok and log if response is problematic.
     * @param response Response
     * @param url String
     * @throws RestClientException exception
     */
    public static void validate(final Response response, final String url) throws RestClientException {
        validate(response, url, getResponseBodyJson(response));
    }

    /**
     * Validate that response is ok and log if response is problematic.
     * @param response Response
     * @param url String
     * @param responseJson JSON of authentication response body
     * @throws RestClientException exception
     */
    private static void validate(final Response response, final String url, final JSONObject responseJson) throws RestClientException {
        try {
            boolean hasError = responseJson.has("error") && responseJson.getString("error").equalsIgnoreCase("true");
            if ((response.getStatusCode() != HttpStatus.SC_OK && response.getStatusCode() != HttpStatus.SC_CREATED) || hasError) {
                final String message = "Unable to authenticating with";
                throw new RestClientException(buildResponseMessage(response.getStatusCode(), message, url, response.getBody()));
            }
        } catch (JSONException e) {
            final String message = "Unable to validate response JSON";
            LOG.error(buildResponseMessage(response.getStatusCode(), message, url, response.getBody()), e);
        }
    }

    /**
     * Build the response message for the exception that is thrown.
     * @param statusCode response status code
     * @param message custom message to display in logs
     * @param url url used for webservice call
     * @param body response body
     * @return String
     */
    private static String buildResponseMessage(final int statusCode, final String message, final String url, final String body) {
        return "Status Code "
            .concat(String.valueOf(statusCode))
            .concat(": ")
            .concat(message)
            .concat(" ")
            .concat(StringUtils.isBlank(url) ? StringUtils.EMPTY : url)
            .concat("\nRaw response:\n")
            .concat(body);
    }
}
