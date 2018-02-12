package com.brandonmaynard.restful.models;

/**
 * A POJO for a restful webservice response.
 * @see <a href="https://github.com/brandonmaynard/aem-restful-api">aem-rest-api</a> GitHub repository for more information.
 * @author Brandon Maynard - https://www.linkedin.com/in/brandonmaynard/
 */
public class Response {

    private int statusCode;
    private String body;

    /**
     * No argument constructor.
     */
    public Response() {
        // Do nothing.
    }

    /**
     * Get status code.
     * @return int
     */
    public int getStatusCode() {
        return this.statusCode;
    }

    /**
     * Set status code.
     * @param statusCode int
     */
    public void setStatusCode(final int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Get body.
     * @return String
     */
    public String getBody() {
        return this.body;
    }

    /**
     * Set body.
     * @param body String
     */
    public void setBody(final String body) {
        this.body = body;
    }
}
