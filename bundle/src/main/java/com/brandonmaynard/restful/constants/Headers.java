package com.brandonmaynard.restful.constants;

/**
 * An Enumeration for http headers.
 * @see <a href="https://github.com/brandonmaynard/aem-restful-api">aem-rest-api</a> GitHub repository for more information.
 * @author Brandon Maynard - https://www.linkedin.com/in/brandonmaynard/
 */
public enum Headers {

    /**
     * Authorization Constant.
     */
    AUTHORIZATION("Authorization"),

    /**
     * Content Type Constant.
     */
    CONTENT_TYPE("Content-type");

    private final String value;

    /**
     * Constructor.
     * @param name String
     */
    Headers(final String name) {
        this.value = name;
    }

    /**
     * Getter for retrieving the String value.
     * @return String
     */
    public String getValue() {
        return this.value;
    }
}
