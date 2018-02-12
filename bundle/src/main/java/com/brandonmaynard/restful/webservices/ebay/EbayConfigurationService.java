package com.brandonmaynard.restful.webservices.ebay;

import com.brandonmaynard.restful.exceptions.RestClientException;
import com.brandonmaynard.restful.models.Response;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

/**
 * An interface for the Ebay connector and configurations.
 * @see <a href="https://github.com/brandonmaynard/aem-restful-api">aem-rest-api</a> GitHub repository for more information.
 * @author Brandon Maynard - https://www.linkedin.com/in/brandonmaynard/
 */
public interface EbayConfigurationService {

    /**
     * Method to authenticate with Ebay API.
     * @see <a href="https://developer.ebay.com/api-docs/static/oauth-client-credentials-grant.html">here</a> for more information.
     * @return Response with populated Status Code and Body
     * @throws RestClientException exception
     */
    Response authenticate() throws RestClientException;

    /**
     * Method to execute a search by keywords.
     * @see <a href="https://developer.ebay.com/api-docs/buy/browse/resources/item_summary/methods/search#_samples">here</a> for more information.
     * @param authResponse Response
     * @param searchTerm String
     * @param limit String
     * @return Response with populated Status Code and Body
     * @throws RestClientException exception
     * @throws UnsupportedEncodingException exception
     * @throws URISyntaxException exception
     */
    Response getSearchByKeywords(Response authResponse, String searchTerm, String limit)
            throws RestClientException, UnsupportedEncodingException, URISyntaxException;
}
