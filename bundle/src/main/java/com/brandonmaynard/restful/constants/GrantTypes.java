package com.brandonmaynard.restful.constants;

/**
 * An Enumeration for http grant types.
 * @see <a href="https://github.com/brandonmaynard/aem-restful-api">aem-rest-api</a> GitHub repository for more information.
 * @author Brandon Maynard - https://www.linkedin.com/in/brandonmaynard/
 */
public enum GrantTypes {

    /**
     * Implicit Constant.
     */
    IMPLICIT("implicit"),

    /**
     * Authorization Code Constant.
     */
    AUTHORIZATION_CODE("authorization_code"),

    /**
     * Client Credentials Constant.
     */
    CLIENT_CREDENTIALS("client_credentials"),

    /**
     * Refresh Token Constant.
     */
    REFRESH_TOKEN("refresh_token"),

    /**
     * Password Constant.
     */
    PASSWORD("password"),

    /**
     * Password Realm Constant.
     */
    PASSWORD_REALM("http://auth0.com/oauth/grant-type/password-realm"),

    /**
     * Multifactor Authentication OOB Constant.
     */
    MULTIFACTOR_AUTH_OOB("http://auth0.com/oauth/grant-type/mfa-oob"),

    /**
     * Multifactor Authentication OTP Constant.
     */
    MULTIFACTOR_AUTH_OTP("http://auth0.com/oauth/grant-type/mfa-otp"),

    /**
     * Multifactor Authentication Recovery Constant.
     */
    MULTIFACTOR_AUTH_RECOVERY("http://auth0.com/oauth/grant-type/mfa-recovery-code");

    private final String value;

    /**
     * Constructor.
     * @param type String
     */
    GrantTypes(final String type) {
        this.value = type;
    }

    /**
     * Getter for retrieving the String value.
     * @return String
     */
    public String getValue() {
        return this.value;
    }
}
