package com.brandonmaynard.restful.webservices.salesforce.constants;

/**
 * An Enumeration for SFDC endpoints.
 * @see <a href="https://github.com/brandonmaynard/aem-restful-api">aem-rest-api</a> GitHub repository for more information.
 * @author Brandon Maynard - https://www.linkedin.com/in/brandonmaynard/
 */
public enum SalesForceEndpoints {

    /**
     * The authorize endpoint constant.
     */
    AUTHORIZE_ENDPOINT("/services/oauth2/token"),

    /**
     * The all available endpoints-endpoint constant.
     */
    ALL_ENDPOINTS("/services/data/v%s/"),

    /**
     * The all sobject endpoint constant.
     */
    SOBJECT_ENDPOINT("/services/data/v%s/sobjects/%s/"),

    /**
     * The query endpoint constant.
     */
    QUERY_ENDPOINT("/services/data/v%s/query/");

    private final String value;

    /**
     * Constructor.
     * @param endPoint String
     */
    SalesForceEndpoints(final String endPoint) {
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
