package com.brandonmaynard.restful.servlets;

import com.brandonmaynard.restful.models.Response;
import com.brandonmaynard.restful.webservices.salesforce.SalesForceConfigurationService;
import com.brandonmaynard.restful.webservices.salesforce.constants.SalesForceFieldNames;
import com.brandonmaynard.restful.webservices.salesforce.constants.SalesForceFactoryLabels;
import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * An example {@link SlingSafeMethodsServlet} extension for submitting restful data SFDC.
 * This is purposely using SlingSafeMethodsServlet#doGet() instead of SlingAllMethodsServlet#doPost()
 * to return the raw JSON for demonstration purposes.
 * @see <a href="https://github.com/brandonmaynard/aem-restful-api">aem-rest-api</a> GitHub repository for more information.
 * @author Brandon Maynard - https://www.linkedin.com/in/brandonmaynard/
 */
@Component(immediate = true, metatype = true)
@Service
@Properties(value = {
        @Property(name = "sling.servlet.methods", value = { "GET" }),
        @Property(name = "sling.servlet.extensions", value = { "json" }),
        @Property(name = "sling.servlet.selectors", value = { "sfdc" }),
        @Property(name = "sling.servlet.resourceTypes", value = { "restful/components/content/example-salesforce-submit" })
})
public class SalesForceServlet extends SlingSafeMethodsServlet {
    private static final Logger LOG = LoggerFactory.getLogger(SalesForceServlet.class);
    private static final long serialVersionUID = 9209410125277166431L;

    @Reference(target = SalesForceFactoryLabels.INSTANCE_1)
    private SalesForceConfigurationService salesforceService;

    /*
     * (non-Javadoc)
     * @see org.apache.sling.api.servlets.SlingSafeMethodsServlet#doGet()
     */
    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws IOException {
        JSONObject responseJson = new JSONObject();
        response.setContentType(MediaType.APPLICATION_JSON);

        try {
            Response sfdcAuthResponse = this.salesforceService.authenticate();
            Response sfdcLeadResponse = this.salesforceService.upsertLead(sfdcAuthResponse, getFieldsList(request));
            response.getWriter().write(sfdcLeadResponse.getBody());
        } catch (Exception e) {
            LOG.error("An error has occurred attempting to retrieve response body.", e);

            try {
                responseJson.put("error", true);
                response.getWriter().write(responseJson.toString());
            } catch (JSONException jsonException) {
                LOG.error("An error has occurred trying to write JSON.", jsonException);
            }
        }
    }

    /**
     * Get a populated list of field names to be posted.
     * @param request SlingHttpServletRequest
     * @return List<NameValuePair>
     */
    private List<NameValuePair> getFieldsList(final SlingHttpServletRequest request) {
        List<NameValuePair> fields = new ArrayList<>();

        String firstName = request.getParameter(SalesForceFieldNames.FIRST_NAME.getValue());
        if (StringUtils.isNotBlank(firstName)) {
            fields.add(new BasicNameValuePair(SalesForceFieldNames.FIRST_NAME.getValue(), firstName));
        }

        String lastName = request.getParameter(SalesForceFieldNames.LAST_NAME.getValue());
        if (StringUtils.isNotBlank(lastName)) {
            fields.add(new BasicNameValuePair(SalesForceFieldNames.LAST_NAME.getValue(), lastName));
        }

        String company = request.getParameter(SalesForceFieldNames.COMPANY.getValue());
        if (StringUtils.isNotBlank(company)) {
            fields.add(new BasicNameValuePair(SalesForceFieldNames.COMPANY.getValue(), company));
        }

        String phone = request.getParameter(SalesForceFieldNames.PHONE.getValue());
        if (StringUtils.isNotBlank(phone)) {
            fields.add(new BasicNameValuePair(SalesForceFieldNames.PHONE.getValue(), phone));
        }

        String email = request.getParameter(SalesForceFieldNames.EMAIL.getValue());
        if (StringUtils.isNotBlank(email)) {
            fields.add(new BasicNameValuePair(SalesForceFieldNames.EMAIL.getValue(), email));
        }

        String campaign = request.getParameter(SalesForceFieldNames.CAMPAIGN.getValue());
        if (StringUtils.isNotBlank(campaign)) {
            fields.add(new BasicNameValuePair(SalesForceFieldNames.CAMPAIGN.getValue(), campaign));
        }

        return fields;
    }
}
