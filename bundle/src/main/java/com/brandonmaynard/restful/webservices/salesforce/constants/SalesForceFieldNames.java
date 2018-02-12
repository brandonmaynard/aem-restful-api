package com.brandonmaynard.restful.webservices.salesforce.constants;

/**
 * An Enumeration for SFDC field names.
 * @see <a href="https://developer.salesforce.com/docs/atlas.en-us.sfFieldRef.meta/sfFieldRef/salesforce_field_reference_Lead.htm">
 * Lead API Field Names</a> for more information.
 * @see <a href="https://github.com/brandonmaynard/aem-restful-api">aem-rest-api</a> GitHub repository for more information.
 * @author Brandon Maynard - https://www.linkedin.com/in/brandonmaynard/
 */
public enum SalesForceFieldNames {

    /**
     * The first name field constant.
     */
    FIRST_NAME("FirstName"),

    /**
     * The last name field constant.
     */
    LAST_NAME("LastName"),

    /**
     * The company field constant.
     */
    COMPANY("Company"),

    /**
     * The phone field constant.
     */
    PHONE("Phone"),

    /**
     * The email field constant.
     */
    EMAIL("Email"),

    /**
     * The campaign field constant.
     */
    CAMPAIGN("Campaign");

    private final String value;

    /**
     * Constructor.
     * @param fieldName String
     */
    SalesForceFieldNames(final String fieldName) {
        this.value = fieldName;
    }

    /**
     * Getter for retrieving the String value.
     * @return String
     */
    public String getValue() {
        return this.value;
    }
}
