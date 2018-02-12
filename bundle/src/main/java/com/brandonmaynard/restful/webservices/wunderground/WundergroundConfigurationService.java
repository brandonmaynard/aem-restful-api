package com.brandonmaynard.restful.webservices.wunderground;

import com.brandonmaynard.restful.exceptions.RestClientException;
import com.brandonmaynard.restful.models.Response;

import java.net.URISyntaxException;

/**
 * An interface for the Wunderground connector and configurations.
 * @see <a href="https://github.com/brandonmaynard/aem-restful-api">aem-rest-api</a> GitHub repository for more information.
 * @author Brandon Maynard - https://www.linkedin.com/in/brandonmaynard/
 */
public interface WundergroundConfigurationService {

    /**
     * Method to get current conditions from Wunderground.
     * @see <a href="https://www.wunderground.com/weather/api/d/docs?MR=1">here</a> for more information.
     * @param state String
     * @param city String
     * @return Response with populated Status Code and Body
     * @throws RestClientException exception
     * @throws URISyntaxException exception
     */
    Response getCurrentConditions(String state, String city) throws RestClientException, URISyntaxException;

    /**
     * Method to get forecast from Wunderground.
     * @see <a href="https://www.wunderground.com/weather/api/d/docs?d=data/index">here</a> for more information.
     * @param state String
     * @param city String
     * @return Response with populated Status Code and Body
     * @throws RestClientException exception
     * @throws URISyntaxException exception
     */
    Response getForecast(String state, String city) throws RestClientException, URISyntaxException;
}
