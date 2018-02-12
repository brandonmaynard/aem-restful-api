package com.brandonmaynard.restful.webservices.salesforce.impl;

import com.brandonmaynard.restful.clients.OauthClient;
import com.brandonmaynard.restful.constants.Authentication;
import com.brandonmaynard.restful.constants.GrantTypes;
import com.brandonmaynard.restful.exceptions.RestClientException;
import com.brandonmaynard.restful.models.Response;
import com.brandonmaynard.restful.utils.ResponseUtils;
import com.brandonmaynard.restful.webservices.salesforce.SalesForceConfigurationService;
import com.brandonmaynard.restful.webservices.salesforce.constants.SalesForceEndpoints;
import com.brandonmaynard.restful.webservices.salesforce.constants.SalesForceQueries;
import com.brandonmaynard.restful.webservices.salesforce.constants.SalesForceSobjects;
import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

/**
 * Connector and configuration service for SFDC (factory implementation).
 * @see <a href="https://github.com/brandonmaynard/aem-restful-api">aem-rest-api</a> GitHub repository for more information.
 * @author Brandon Maynard - https://www.linkedin.com/in/brandonmaynard/
 */
@Service
@Component(
    label = "BMM SalesForce Configuration Service",
    description = "SalesForce Configuration Service.",
    immediate = true,
    metatype = true,
    configurationFactory = true)
public class SalesForceConfigurationServiceImpl extends OauthClient implements SalesForceConfigurationService {

    private static final Logger LOG = LoggerFactory.getLogger(SalesForceConfigurationServiceImpl.class);

    private static final String USERNAME = "sfdc.usernam";
    @Property(
        name = USERNAME,
        label = "SalesForce Username",
        value = "",
        description = "The SalesForce username to SFDC's integration account for AEM."
    )
    private String username;

    private static final String PASSWORD_TOKEN = "sfdc.passwordtoken";
    @Property(
        name = PASSWORD_TOKEN,
        label = "SalesForce Password+Token",
        value = "",
        description = "The SalesForce password+token (combined together) to SFDC's integration account for AEM."
    )
    private String password;

    private static final String DOMAIN = "sfdc.domain";
    @Property(
        name = DOMAIN,
        label = "SalesForce Instance Domain",
        value = "",
        description = "The SalesForce instance's domain."
    )
    private String domain;

    private static final String VERSION = "sfdc.version";
    @Property(
        name = VERSION,
        label = "SalesForce Version",
        value = "",
        description = "The SalesForce version being used."
    )
    private String version;

    private static final String CLIENT_ID = "sfdc.client.id";
    @Property(
        name = CLIENT_ID,
        label = "SalesForce Client ID",
        value = "",
        description = "The SalesForce instance's client ID."
    )
    private String clientId;

    private static final String CLIENT_SECRET = "sfdc.client.secret";
    @Property(
        name = CLIENT_SECRET,
        label = "SalesForce Client Secret Key",
        value = "",
        description = "The SalesForce instance's client secret key."
    )
    private String clientSecret;

    private static final String LABEL = "sfdc.unique.label";
    @Property(
        name = LABEL,
        label = "Instance Label",
        value = "",
        description = "Unique configuration instance label."
    )

    private JSONObject authJson;

    /**
     * Method to run on activation.
     * @param context the component's context
     */
    protected void activate(final ComponentContext context) {
        @SuppressWarnings("rawtypes")
        final Dictionary properties = context.getProperties();

        this.username = (String) properties.get(USERNAME);
        this.password = (String) properties.get(PASSWORD_TOKEN);
        this.domain = (String) properties.get(DOMAIN);
        this.version = (String) properties.get(VERSION);
        this.clientId = (String) properties.get(CLIENT_ID);
        this.clientSecret = (String) properties.get(CLIENT_SECRET);
    }

    /**
     * @see SalesForceConfigurationService#authenticate()
     * @return Response
     * @throws RestClientException exception
     */
    public Response authenticate() throws RestClientException {
        if (StringUtils.isBlank(this.domain)) {
            throw new RestClientException("Supplied Domain is not filled out. Please ensure this configuration is accurate.");
        }

        List<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair(Authentication.GRANT_TYPE.getValue(), GrantTypes.PASSWORD.getValue()));
        pairs.add(new BasicNameValuePair(Authentication.CLIENT_ID.getValue(), this.clientId));
        pairs.add(new BasicNameValuePair(Authentication.CLIENT_SECRET.getValue(), this.clientSecret));
        pairs.add(new BasicNameValuePair(Authentication.USERNAME.getValue(), this.username));
        pairs.add(new BasicNameValuePair(Authentication.PASSWORD.getValue(), this.password));

        String url = this.domain.concat(SalesForceEndpoints.AUTHORIZE_ENDPOINT.getValue());
        Response response = super.authenticate(url, pairs);
        ResponseUtils.validate(response, url);

        return response;
    }

    /**
     * @see SalesForceConfigurationService#getAllAvailableEndpoints(Response)
     * @param authResponse Response
     * @return Response
     * @throws RestClientException exception
     * @throws URISyntaxException exception
     */
    public Response getAllAvailableEndpoints(final Response authResponse) throws RestClientException, URISyntaxException {
        String endPoint = String.format(SalesForceEndpoints.ALL_ENDPOINTS.getValue(), this.version);
        return super.get(getFormattedUrl(authResponse, endPoint, null), this.authJson);
    }

    /**
     * @see SalesForceConfigurationService#getQueryResponse(Response, String)
     * @param authResponse Response
     * @param query String
     * @return Response
     * @throws RestClientException exception
     * @throws URISyntaxException exception
     */
    public Response getQueryResponse(final Response authResponse, final String query) throws RestClientException, URISyntaxException {
        String endPoint = String.format(SalesForceEndpoints.QUERY_ENDPOINT.getValue(), this.version);
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair(SalesForceQueries.QUERY_PARAM_NAME.getValue(), query));
        return super.get(getFormattedUrl(authResponse, endPoint, params), this.authJson);
    }

    /**
     * @see SalesForceConfigurationService#upsertLead(Response, List)
     * @param authResponse Response
     * @param fields List<NameValuePair>
     * @return Response
     * @throws RestClientException exception
     * @throws URISyntaxException exception
     */
    public Response upsertLead(final Response authResponse, final List<NameValuePair> fields) throws RestClientException, URISyntaxException {
        return super.post(getSobjectEndpoint(authResponse, SalesForceSobjects.LEAD.getValue()), this.authJson, fields);
    }

    /**
     * Get the formatted SFDC URL for the supplied sobject name.
     * @param authResponse Response
     * @param sbojectName String
     * @return String
     * @throws RestClientException exception
     * @throws URISyntaxException exception
     */
    private String getSobjectEndpoint(final Response authResponse, final String sbojectName) throws RestClientException, URISyntaxException {
        String endPoint = String.format(SalesForceEndpoints.SOBJECT_ENDPOINT.getValue(), this.version, sbojectName);
        return getFormattedUrl(authResponse, endPoint, null);
    }

    /**
     * Get the formatted SFDC URL with instance domain and endpoint concatenated.
     * @param authResponse authentication Response
     * @param endpoint endpoint URI
     * @param params optional parameters
     * @return Response
     * @throws RestClientException exception
     * @throws URISyntaxException exception
     */
    private String getFormattedUrl(final Response authResponse, final String endpoint, final List<NameValuePair> params)
            throws RestClientException, URISyntaxException {

        String formattedUrl = StringUtils.EMPTY;

        try {
            JSONObject authJson = getAuthJson(authResponse);
            String instanceUrl = ResponseUtils.getValueFromJson(Authentication.INSTANCE_URL.getValue(), authJson);
            if (StringUtils.isBlank(instanceUrl)) {
                throw new RestClientException("SFDC Instance URL came back blank or null. Please check OSGi configurations.");
            }

            formattedUrl = instanceUrl.concat(endpoint);
        } catch (Exception e) {
            LOG.error("An exception has occurred.", e);
        }

        URIBuilder uriBuilder = new URIBuilder(formattedUrl);

        if (params != null) {
            for (NameValuePair param : params) {
                uriBuilder.setParameter(param.getName(), param.getValue());
            }
        }

        return uriBuilder.build().toString();
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
