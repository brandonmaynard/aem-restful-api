package com.brandonmaynard.restful.webservices.salesforce.constants;

/**
 * An Enumeration for SFDC JSON keys.
 * @see <a href="https://github.com/brandonmaynard/aem-restful-api">aem-rest-api</a> GitHub repository for more information.
 * @author Brandon Maynard - https://www.linkedin.com/in/brandonmaynard/
 */
public enum SalesForceJsonKeys {

    /**
     * The records JSON key constant.
     */
    RECORDS("records"),

    /**
     * The ID JSON key constant.
     */
    ID("Id"),

    /**
     * The name JSON key constant.
     */
    NAME("Name");

    private final String value;

    /**
     * Constructor.
     * @param key String
     */
    SalesForceJsonKeys(final String key) {
        this.value = key;
    }

    /**
     * Getter for retrieving the String value.
     * @return String
     */
    public String getValue() {
        return this.value;
    }
}
