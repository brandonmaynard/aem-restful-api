package com.brandonmaynard.restful.webservices.ebay.impl;

import com.brandonmaynard.restful.clients.OauthClient;
import com.brandonmaynard.restful.constants.Authentication;
import com.brandonmaynard.restful.constants.GrantTypes;
import com.brandonmaynard.restful.constants.Headers;
import com.brandonmaynard.restful.exceptions.RestClientException;
import com.brandonmaynard.restful.models.Response;
import com.brandonmaynard.restful.utils.ResponseUtils;
import com.brandonmaynard.restful.webservices.ebay.EbayConfigurationService;
import com.brandonmaynard.restful.webservices.ebay.constants.EbayEndpoints;
import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.service.component.ComponentContext;

import javax.ws.rs.core.MediaType;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Dictionary;
import java.util.List;

/**
 * Connector and configuration service for Ebay.
 * @see <a href="https://github.com/brandonmaynard/aem-restful-api">aem-rest-api</a> GitHub repository for more information.
 * @author Brandon Maynard - https://www.linkedin.com/in/brandonmaynard/
 */
@Service
@Component(
    label = "BMM Ebay Configuration Service",
    description = "Ebay Configuration Service.",
    immediate = true,
    metatype = true)
public class EbayConfigurationServiceImpl extends OauthClient implements EbayConfigurationService {

    private static final String URL = "ebay.url";
    @Property(
        name = URL,
        label = "Authentication URL",
        value = "",
        description = "Ebay Authentication URL."
    )
    private String url;

    private static final String API_URL = "ebay.api.url";
    @Property(
        name = API_URL,
        label = "API URL",
        value = "",
        description = "Ebay API URL."
    )
    private String apiUrl;

    private static final String SCOPE = "ebay.scope";
    @Property(
        name = SCOPE,
        label = "Scope",
        value = "",
        description = "Ebay scope."
    )
    private String scope;

    private static final String RU_NAME = "ebay.ru.name";
    @Property(
        name = RU_NAME,
        label = "Redirect RuName",
        value = "",
        description = "Ebay RuName."
    )
    private String ruName;

    private static final String CLIENT_ID = "ebay.client.id";
    @Property(
        name = CLIENT_ID,
        label = "Client ID",
        value = "",
        description = "Ebay client ID."
    )
    private String clientId;

    private static final String CLIENT_SECRET = "ebay.client.secret";
    @Property(
        name = CLIENT_SECRET,
        label = "Client Secret",
        value = "",
        description = "Ebay client secret."
    )
    private String clientSecret;

    private JSONObject authJson;

    /**
     * Method to run on activation.
     * @param context the component's context
     */
    protected void activate(final ComponentContext context) {
        @SuppressWarnings("rawtypes")
        final Dictionary properties = context.getProperties();

        this.url = (String) properties.get(URL);
        this.apiUrl = (String) properties.get(API_URL);
        this.scope = (String) properties.get(SCOPE);
        this.ruName = (String) properties.get(RU_NAME);
        this.clientId = (String) properties.get(CLIENT_ID);
        this.clientSecret = (String) properties.get(CLIENT_SECRET);
    }

    /**
     * @see EbayConfigurationService#authenticate()
     * @return Response
     * @throws RestClientException exception
     */
    public Response authenticate() throws RestClientException {
        if (StringUtils.isBlank(this.url)) {
            throw new RestClientException("Supplied Authentication URL is not filled out. Please ensure this configuration is accurate.");
        }

        List<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair(Authentication.GRANT_TYPE.getValue(), GrantTypes.CLIENT_CREDENTIALS.getValue()));
        pairs.add(new BasicNameValuePair(Authentication.REDIRECT_URI.getValue(), this.ruName));
        pairs.add(new BasicNameValuePair(Authentication.SCOPE.getValue(), this.scope));

        List<NameValuePair> headers = new ArrayList<>();
        String encoding = Base64.getEncoder().encodeToString((clientId.concat(":").concat(clientSecret)).getBytes());
        headers.add(new BasicNameValuePair(Headers.AUTHORIZATION.getValue(), "Basic ".concat(encoding)));
        headers.add(new BasicNameValuePair(Headers.CONTENT_TYPE.getValue(), MediaType.APPLICATION_FORM_URLENCODED));

        Response response = super.authenticate(this.url, pairs, headers);
        ResponseUtils.validate(response, this.url);

        return response;
    }

    /**
     * @see EbayConfigurationService#getSearchByKeywords(Response, String, String)
     * @param authResponse Response
     * @param searchTerm String
     * @param limit String
     * @return Response
     * @throws RestClientException exception
     * @throws UnsupportedEncodingException exception
     * @throws URISyntaxException exception
     */
    public Response getSearchByKeywords(final Response authResponse, final String searchTerm, final String limit)
            throws RestClientException, UnsupportedEncodingException, URISyntaxException {

        if (StringUtils.isBlank(this.apiUrl)) {
            throw new RestClientException("Ebay API URL is not filled out. Please ensure this configuration is accurate.");
        }

        if (!StringUtils.isNumeric(limit)) {
            throw new RestClientException("Supplied limit parameter is not a numeric value.");
        }

        String encodedSearchTerm = URLEncoder.encode(searchTerm, "UTF-8");
        String searchByKeywordUri = String.format(EbayEndpoints.SEARCH_BY_KEYWORD.getValue(), encodedSearchTerm, limit);
        return super.get(getFormattedUrl(this.apiUrl, searchByKeywordUri), getAuthJson(authResponse));
    }

    /**
     * Get the formatted URL with base URL and endpoint concatenated.
     * @param baseUrl base URL
     * @param endpoint endpoint URI
     * @return String
     * @throws URISyntaxException exception
     */
    private String getFormattedUrl(final String baseUrl, final String endpoint) throws URISyntaxException {
        return new URIBuilder(baseUrl.concat(endpoint)).build().toString();
    }

    /**
     * Get the authentication's Response's JSON if it is not already populated.
     * @param authResponse authentication Response
     * @return String
     * @throws URISyntaxException exception
     */
    private JSONObject getAuthJson(final Response authResponse) throws URISyntaxException {
        if (this.authJson == null) {
            this.authJson = ResponseUtils.getResponseBodyJson(authResponse);
        }
        return this.authJson;
    }
}
