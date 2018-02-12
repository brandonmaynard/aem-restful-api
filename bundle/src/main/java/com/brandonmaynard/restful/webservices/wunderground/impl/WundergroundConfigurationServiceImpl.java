package com.brandonmaynard.restful.webservices.wunderground.impl;

import com.brandonmaynard.restful.clients.SimpleClient;
import com.brandonmaynard.restful.exceptions.RestClientException;
import com.brandonmaynard.restful.models.Response;
import com.brandonmaynard.restful.webservices.wunderground.WundergroundConfigurationService;
import com.brandonmaynard.restful.webservices.wunderground.constants.WundergroundEndpoints;
import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.http.HttpStatus;
import org.apache.http.client.utils.URIBuilder;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Dictionary;

/**
 * Connector and configuration service for Wunderground.
 * @see <a href="https://github.com/brandonmaynard/aem-restful-api">aem-rest-api</a> GitHub repository for more information.
 * @author Brandon Maynard - https://www.linkedin.com/in/brandonmaynard/
 */
@Service
@Component(
    label = "BMM Wunderground Configuration Service",
    description = "Wunderground Service.",
    immediate = true,
    metatype = true)
public class WundergroundConfigurationServiceImpl extends SimpleClient implements WundergroundConfigurationService {

    private static final Logger LOG = LoggerFactory.getLogger(WundergroundConfigurationServiceImpl.class);

    private static final String API_DOMAIN = "wunderground.api.domain";
    @Property(
        name = API_DOMAIN,
        label = "API Domain",
        value = "",
        description = "Wunderground API domain."
    )
    private String apiDomain;

    private static final String API_KEY = "wunderground.api.key";
    @Property(
        name = API_KEY,
        label = "API Key",
        value = "",
        description = "Wunderground API key."
    )
    private String apiKey;

    /**
     * Method to run on activation.
     * @param context the component's context
     */
    protected void activate(final ComponentContext context) {
        @SuppressWarnings("rawtypes")
        final Dictionary properties = context.getProperties();

        this.apiDomain = (String) properties.get(API_DOMAIN);
        this.apiKey = (String) properties.get(API_KEY);
    }

    /**
     * @see WundergroundConfigurationService#getCurrentConditions(String, String)
     * @param state String
     * @param city String
     * @return Response
     * @throws RestClientException exception
     * @throws URISyntaxException exception
     */
    public Response getCurrentConditions(final String state, final String city) throws RestClientException, URISyntaxException {
        return getFromEndpoint(WundergroundEndpoints.CURRENT_CITY_BY_STATE.getValue(), state, city);
    }

    /**
     * @see WundergroundConfigurationService#getForecast(String, String)
     * @param state String
     * @param city String
     * @return Response
     * @throws RestClientException exception
     * @throws URISyntaxException exception
     */
    public Response getForecast(final String state, final String city) throws RestClientException, URISyntaxException {
        return getFromEndpoint(WundergroundEndpoints.FORECAST_CITY_BY_STATE.getValue(), state, city);
    }

    /**
     * Get the response from a provided endpoint.
     * @param endpoint String
     * @param state String
     * @param city String
     * @return Response
     * @throws RestClientException exception
     * @throws URISyntaxException exception
     */
    private Response getFromEndpoint(final String endpoint, final String state, final String city) throws RestClientException, URISyntaxException {
        if (StringUtils.isBlank(this.apiDomain) || StringUtils.isBlank(this.apiKey)) {
            throw new RestClientException("Wunderground API Domain or Key are not filled out. Please ensure this configuration is accurate.");
        }

        try {
            String encodedState = URLEncoder.encode(state, "UTF-8");
            String encodedCity = URLEncoder.encode(city, "UTF-8");
            String uri = String.format(endpoint, this.apiKey, encodedState, encodedCity);
            return super.get(getFormattedUrl(this.apiDomain, uri));
        } catch (UnsupportedEncodingException e) {
            LOG.error("An UnsupportedEncodingException has occurred. Please verify the parameters.");
            Response response = new Response();
            response.setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            response.setBody("An error has occurred.");
            return response;
        }
    }

    /**
     * Get the formatted URL with base URL and endpoint concatenated.
     * @param baseUrl base URL
     * @param endpoint endpoint URI
     * @return String
     * @throws URISyntaxException exception
     */
    private String getFormattedUrl(final String baseUrl, final String endpoint) throws URISyntaxException {
        return new URIBuilder(baseUrl.concat(endpoint)).build().toString();
    }
}
