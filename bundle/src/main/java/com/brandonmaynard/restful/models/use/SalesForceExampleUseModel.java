package com.brandonmaynard.restful.models.use;

import com.adobe.cq.sightly.WCMUse;
import com.brandonmaynard.restful.exceptions.RestClientException;
import com.brandonmaynard.restful.models.Response;
import com.brandonmaynard.restful.webservices.salesforce.SalesForceConfigurationService;
import com.brandonmaynard.restful.webservices.salesforce.constants.SalesForceFactoryLabels;
import com.brandonmaynard.restful.webservices.salesforce.constants.SalesForceJsonKeys;
import com.brandonmaynard.restful.webservices.salesforce.constants.SalesForceQueries;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link WCMUse} Model for SalesForce example component.
 */
public class SalesForceExampleUseModel extends WCMUse {

    private static final Logger LOG = LoggerFactory.getLogger(SalesForceExampleUseModel.class);

    private String allEndPointsJson;
    private List<NameValuePair> campaigns;

    /*
     * (non-Javadoc)
     * @see com.adobe.cq.sightly.WCMUse#activate()
     */
    @Override
    public void activate() throws Exception {
        try {
            SalesForceConfigurationService[] connectors = getSlingScriptHelper()
                    .getServices(SalesForceConfigurationService.class, SalesForceFactoryLabels.INSTANCE_1);

            if (connectors == null || connectors.length == 0) {
                throw new RestClientException("Unable to get SFDC connector instance. Please ensure the "
                    .concat("specified configuration exists and that the 'sfdc.unique.label' is properly configured.")
                );
            } else {
                SalesForceConfigurationService sfdcConnector = connectors[0];
                Response sfdcAuthResponse = sfdcConnector.authenticate();
                Response sfdcEndpointsResponse = sfdcConnector.getAllAvailableEndpoints(sfdcAuthResponse);
                Response sfdcCampaignsResponse = sfdcConnector.getQueryResponse(sfdcAuthResponse, SalesForceQueries.CAMPAIGNS.getValue());

                this.allEndPointsJson = sfdcEndpointsResponse.getBody();
                this.campaigns = getCampaignList(sfdcCampaignsResponse);
            }
        } catch (Exception e) {
            LOG.error("An exception has occurred.", e);
        }
    }

    /**
     * Get a list of campaigns from SFDC.
     * @param sfdcCampaignsResponse Response
     * @return List<NameValuePair>
     * @throws JSONException exception
     */
    private List<NameValuePair> getCampaignList(final Response sfdcCampaignsResponse) throws JSONException {
        List<NameValuePair> campaigns = new ArrayList<>();

        JSONObject campaignJson = new JSONObject(sfdcCampaignsResponse.getBody());
        if (campaignJson.length() == 0) {
            if (LOG.isErrorEnabled()) {
                LOG.error("The response body's JSON is empty.");
            }
            return campaigns;
        }

        JSONArray records = campaignJson.getJSONArray(SalesForceJsonKeys.RECORDS.getValue());
        if (records == null || records.length() == 0) {
            if (LOG.isErrorEnabled()) {
                LOG.error("No records were found in the response body's JSON.");
            }
            return campaigns;
        }

        for (int i = 0; i < records.length(); i++) {
            JSONObject record = records.getJSONObject(i);
            String recordName = record.getString(SalesForceJsonKeys.NAME.getValue());
            String recordId = record.getString(SalesForceJsonKeys.ID.getValue());
            campaigns.add(new BasicNameValuePair(recordName, recordId));
        }

        return campaigns;
    }

    /**
     * Get the raw JSON for the all endpoints-endpoint.
     * @return String.
     */
    public String getAllEndPointsJson() {
        return this.allEndPointsJson;
    }

    /**
     * Get a list of campaigns.
     * @return List<NameValuePair>.
     */
    public List<NameValuePair> getCampaigns() {
        return this.campaigns;
    }
}
