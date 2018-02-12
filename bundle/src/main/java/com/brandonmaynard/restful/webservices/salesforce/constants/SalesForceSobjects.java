package com.brandonmaynard.restful.webservices.salesforce.constants;

/**
 * An Enumeration for SFDC sobject names.
 * @see <a href="https://github.com/brandonmaynard/aem-restful-api">aem-rest-api</a> GitHub repository for more information.
 * @author Brandon Maynard - https://www.linkedin.com/in/brandonmaynard/
 */
public enum SalesForceSobjects {

    /**
     * The Lead sobject constant.
     */
    LEAD("Lead");

    private final String value;

    /**
     * Constructor.
     * @param sobjectName String
     */
    SalesForceSobjects(final String sobjectName) {
        this.value = sobjectName;
    }

    /**
     * Getter for retrieving the String value.
     * @return String
     */
    public String getValue() {
        return this.value;
    }
}
