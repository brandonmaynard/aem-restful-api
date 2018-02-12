package com.brandonmaynard.restful.clients;

import com.brandonmaynard.restful.constants.Authentication;
import com.brandonmaynard.restful.constants.Headers;
import com.brandonmaynard.restful.exceptions.RestClientException;
import com.brandonmaynard.restful.models.Response;
import com.brandonmaynard.restful.utils.ResponseUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import java.net.URISyntaxException;
import java.util.List;

/**
 * OAUTH Restful Client.
 * @see <a href="https://github.com/brandonmaynard/aem-restful-api">aem-rest-api</a> GitHub repository for more information.
 * @author Brandon Maynard - https://www.linkedin.com/in/brandonmaynard/
 */
public class OauthClient extends RestClient {

    private static final Logger LOG = LoggerFactory.getLogger(OauthClient.class);

    /**
     * Method to authenticate with a webservice.
     * @param url The webservice's url
     * @param pairs Required fields for authenticating
     * @return Response
     * @throws RestClientException exception
     */
    protected Response authenticate(final String url, final List<NameValuePair> pairs) throws RestClientException {
        return authenticate(url, pairs, null);
    }

    /**
     * Method to authenticate with a webservice.
     * @param url The webservice's url
     * @param pairs Required fields for authenticating
     * @param headers Headers to be added to authenticate
     * @return Response
     * @throws RestClientException exception
     */
    protected Response authenticate(final String url, final List<NameValuePair> pairs, final List<NameValuePair> headers)
            throws RestClientException {

        HttpPost httpost = new HttpPost(url);
        httpost.setEntity(new UrlEncodedFormEntity(pairs, Consts.UTF_8));

        if (headers != null) {
            for (NameValuePair header : headers) {
                httpost.setHeader(header.getName(), header.getValue());
            }
        }

        return super.execute(httpost);
    }

    /**
     * Method to execute a GET request based on access_token.
     * @param url The URL to GET from
     * @param authJson Authentication Response body's JSON
     * @return Response
     * @throws RestClientException exception
     * @throws URISyntaxException exception
     */
    protected Response get(final String url, final JSONObject authJson) throws RestClientException, URISyntaxException {
        return executeRequest(url, authJson, null);
    }

    /**
     * Method to execute a POST request based on access_token.
     * @param url The URL to POST to
     * @param authJson Authentication Response body's JSON
     * @param pairs Required fields for posting
     * @return Response
     * @throws RestClientException exception
     * @throws URISyntaxException exception
     */
    protected Response post(final String url, final JSONObject authJson, final List<NameValuePair> pairs)
            throws RestClientException, URISyntaxException {
        return executeRequest(url, authJson, pairs);
    }

    /**
     * Execute a request from a supplied endpoint with supplied pairs.
     * GET request if pairs are null; POST request if pairs are populated.
     * @param url The URL to submit to
     * @param authJson Authentication Response body's JSON
     * @param pairs Values to be posted (null if GET request)
     * @return HttpRequestBase
     * @throws RestClientException exception
     * @throws URISyntaxException exception
     */
    private Response executeRequest(final String url, final JSONObject authJson, final List<NameValuePair> pairs)
        throws RestClientException, URISyntaxException {

        String accessToken = ResponseUtils.getValueFromJson(Authentication.ACCESS_TOKEN.getValue(), authJson);
        if (StringUtils.isBlank(accessToken)) {
            throw new RestClientException("Request was unsuccessful. The access_token is blank.");
        }

        Response response;
        if (pairs == null) {
            HttpGet httpGet = (HttpGet) buildHttpClient(new HttpGet(url), accessToken);
            response = super.execute(httpGet);
        } else {
            HttpPost httpost = (HttpPost) buildHttpClient(new HttpPost(url), accessToken);
            StringEntity messageEntity = new StringEntity(buildFieldJson(pairs), ContentType.create(MediaType.APPLICATION_JSON));
            httpost.setEntity(messageEntity);
            response = super.execute(httpost);
        }

        ResponseUtils.validate(response, url);
        return response;
    }

    /**
     * Method to build fields JSON object to be posted.
     * @param fields List<NameValuePair>
     * @return String
     */
    private String buildFieldJson(final List<NameValuePair> fields) {
        JSONObject fieldsJson = new JSONObject();

        try {
            for (NameValuePair field : fields) {
                fieldsJson.put(field.getName(), field.getValue());
            }
        } catch (JSONException e) {
            LOG.error("Unable to parse JSON.", e);
        }

        return fieldsJson.toString();
    }

    /**
     * Method to build an http response with appropriate headers.
     * @param requestBase HttpPost
     * @param accessToken String
     * @return HttpRequestBase
     */
    private HttpRequestBase buildHttpClient(final HttpRequestBase requestBase, final String accessToken) {
        requestBase.addHeader(Headers.CONTENT_TYPE.getValue(), MediaType.APPLICATION_JSON);
        requestBase.addHeader(Headers.AUTHORIZATION.getValue(), "Bearer ".concat(accessToken));
        return requestBase;
    }
}
