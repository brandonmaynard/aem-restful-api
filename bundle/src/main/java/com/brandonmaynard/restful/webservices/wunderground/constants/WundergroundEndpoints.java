package com.brandonmaynard.restful.webservices.wunderground.constants;

/**
 * An Enumeration for Wunderground endpoints.
 * @see <a href="https://www.wunderground.com/weather/api/d/docs?d=data/index">Wunderground API Features</a> for more information.
 * @see <a href="https://github.com/brandonmaynard/aem-restful-api">aem-rest-api</a> GitHub repository for more information.
 * @author Brandon Maynard - https://www.linkedin.com/in/brandonmaynard/
 */
public enum WundergroundEndpoints {

    /**
     * The current conditions for city by state endpoint constant.
     */
    CURRENT_CITY_BY_STATE("/%s/conditions/q/%s/%s.json"),

    /**
     * The current conditions for city by state endpoint constant.
     */
    FORECAST_CITY_BY_STATE("/%s/forecast/q/%s/%s.json");

    private final String value;

    /**
     * Constructor.
     * @param endPoint String
     */
    WundergroundEndpoints(final String endPoint) {
        this.value = endPoint;
    }

    /**
     * Getter for retrieving the String value.
     * @return String
     */
    public String getValue() {
        return this.value;
    }
}
