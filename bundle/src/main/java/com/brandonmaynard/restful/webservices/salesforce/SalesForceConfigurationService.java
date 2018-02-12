package com.brandonmaynard.restful.webservices.salesforce;

import com.brandonmaynard.restful.exceptions.RestClientException;
import com.brandonmaynard.restful.models.Response;
import org.apache.http.NameValuePair;

import java.net.URISyntaxException;
import java.util.List;

/**
 * An interface for the SFDC connector and configurations.
 * @see <a href="https://github.com/brandonmaynard/aem-restful-api">aem-rest-api</a> GitHub repository for more information.
 * @author Brandon Maynard - https://www.linkedin.com/in/brandonmaynard/
 */
public interface SalesForceConfigurationService {

    /**
     * Method to authenticate with SFDC API.
     * @see <a href="https://developer.salesforce.com/docs/atlas.en-us.api_rest.meta/api_rest/quickstart_oauth.htm">here</a>
     * for more information (Session ID Authorization section).
     * @return Response with populated Status Code and Body
     * @throws RestClientException exception
     */
    Response authenticate() throws RestClientException;

    /**
     * Method to get all SFDC available endpoints based on your configured version.
     * @see <a href="https://developer.salesforce.com/docs/atlas.en-us.api_rest.meta/api_rest/dome_discoveryresource.htm">here</a>
     * for more information.
     * @param authResponse Response
     * @return Response with populated Status Code and Body
     * @throws RestClientException exception
     * @throws URISyntaxException exception
     */
    Response getAllAvailableEndpoints(Response authResponse) throws RestClientException, URISyntaxException;

    /**
     * Method to get a supplied query's response based on your configured version.
     * @see <a href="https://developer.salesforce.com/docs/atlas.en-us.api_rest.meta/api_rest/resources_query.htm">here</a>
     * for more information.
     * @param authResponse Response
     * @param query String
     * @return Response with populated Status Code and Body
     * @throws RestClientException exception
     * @throws URISyntaxException exception
     */
    Response getQueryResponse(Response authResponse, String query) throws RestClientException, URISyntaxException;

    /**
     * Method to post form data to the Lead sobject endpoint based on your configured version.
     * SalesForce will either update an existing record, if one exists, or create a new record (Upsert).
     * @see <a href="https://developer.salesforce.com/docs/atlas.en-us.api_rest.meta/api_rest/resources_sobject_basic_info.htm">here</a>
     * for more information.
     * @param authResponse Response
     * @param fields List<NameValuePair>
     * @return Response with populated Status Code and Body
     * @throws RestClientException exception
     * @throws URISyntaxException exception
     */
    Response upsertLead(Response authResponse, List<NameValuePair> fields) throws RestClientException, URISyntaxException;
}
