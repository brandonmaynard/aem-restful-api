package com.brandonmaynard.restful.constants;

/**
 * An Enumeration for restful authentication.
 * @see <a href="https://github.com/brandonmaynard/aem-restful-api">aem-rest-api</a> GitHub repository for more information.
 * @author Brandon Maynard - https://www.linkedin.com/in/brandonmaynard/
 */
public enum Authentication {

    /**
     * Grant Type Constant.
     */
    GRANT_TYPE("grant_type"),

    /**
     * Client ID Constant.
     */
    CLIENT_ID("client_id"),

    /**
     * Client Secret Constant.
     */
    CLIENT_SECRET("client_secret"),

    /**
     * Redirect URI Constant.
     */
    REDIRECT_URI("redirect_uri"),

    /**
     * Scope Constant.
     */
    SCOPE("scope"),

    /**
     * Username Constant.
     */
    USERNAME("username"),

    /**
     * Password Constant.
     */
    PASSWORD("password"),

    /**
     * Access Token Constant.
     */
    ACCESS_TOKEN("access_token"),

    /**
     * Instance URL Constant.
     */
    INSTANCE_URL("instance_url");

    private final String value;

    /**
     * Constructor.
     * @param name String
     */
    Authentication(final String name) {
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
