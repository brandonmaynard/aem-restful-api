package com.brandonmaynard.restful.webservices.ebay.constants;

/**
 * An Enumeration for Ebay endpoints.
 * @see <a href="https://developer.ebay.com/api-docs/buy/browse/resources/methods">Ebay API Resources</a> for more information.
 * @see <a href="https://github.com/brandonmaynard/aem-restful-api">aem-rest-api</a> GitHub repository for more information.
 * @author Brandon Maynard - https://www.linkedin.com/in/brandonmaynard/
 */
public enum EbayEndpoints {

    /**
     * The search by keyword endpoint constant.
     */
    SEARCH_BY_KEYWORD("/buy/browse/v1/item_summary/search?q=%s&limit=%s");

    private final String value;

    /**
     * Constructor.
     * @param endPoint String
     */
    EbayEndpoints(final String endPoint) {
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
