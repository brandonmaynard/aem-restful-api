package com.brandonmaynard.restful.exceptions;

/**
 * Rest client exception.
 * @see <a href="https://github.com/brandonmaynard/aem-restful-api">aem-rest-api</a> GitHub repository for more information.
 * @author Brandon Maynard - https://www.linkedin.com/in/brandonmaynard/
 */
public class RestClientException extends Exception {

    /**
     * Constructor.
     * @param message String
     */
    public RestClientException(final String message) {
        super(message);
    }
}
