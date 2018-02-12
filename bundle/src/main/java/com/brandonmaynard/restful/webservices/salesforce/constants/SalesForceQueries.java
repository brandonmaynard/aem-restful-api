package com.brandonmaynard.restful.webservices.salesforce.constants;

/**
 * An Enumeration for SFDC queries.
 * @see <a href="https://github.com/brandonmaynard/aem-restful-api">aem-rest-api</a> GitHub repository for more information.
 * @author Brandon Maynard - https://www.linkedin.com/in/brandonmaynard/
 */
public enum SalesForceQueries {

    /**
     * The query's parameter name constant.
     */
    QUERY_PARAM_NAME("q"),

    /**
     * The campaign query constant.
     */
    CAMPAIGNS("SELECT Id, Name FROM Campaign");

    private final String value;

    /**
     * Constructor.
     * @param query String
     */
    SalesForceQueries(final String query) {
        this.value = query;
    }

    /**
     * Getter for retrieving the String value.
     * @return String
     */
    public String getValue() {
        return this.value;
    }
}
