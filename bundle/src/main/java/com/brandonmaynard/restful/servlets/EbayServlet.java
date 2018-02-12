package com.brandonmaynard.restful.servlets;

import com.brandonmaynard.restful.exceptions.RestClientException;
import com.brandonmaynard.restful.models.Response;
import com.brandonmaynard.restful.webservices.ebay.EbayConfigurationService;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import java.io.IOException;

/**
 * An example {@link SlingSafeMethodsServlet} extension for retrieving restful data from Ebay.
 * @see <a href="https://github.com/brandonmaynard/aem-restful-api">aem-rest-api</a> GitHub repository for more information.
 * @author Brandon Maynard - https://www.linkedin.com/in/brandonmaynard/
 */
@Component(immediate = true, metatype = true)
@Service
@Properties(value = {
        @Property(name = "sling.servlet.methods", value = { "GET" }),
        @Property(name = "sling.servlet.extensions", value = { "json" }),
        @Property(name = "sling.servlet.selectors", value = { "ebay" }),
        @Property(name = "sling.servlet.resourceTypes", value = { "restful/components/content/example-ebay-search" })
})
public class EbayServlet extends SlingSafeMethodsServlet {
    private static final Logger LOG = LoggerFactory.getLogger(EbayServlet.class);
    private static final long serialVersionUID = 3262792743948841087L;
    private static final int NUMBER_SELECTORS = 3;
    private static final int SELECTOR_2 = 1;
    private static final int SELECTOR_3 = 2;

    @Reference
    private EbayConfigurationService ebayService;

    /*
     * (non-Javadoc)
     * @see org.apache.sling.api.servlets.SlingSafeMethodsServlet#doGet()
     */
    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws IOException {
        JSONObject responseJson = new JSONObject();
        response.setContentType(MediaType.APPLICATION_JSON);

        try {
            String[] selectors = request.getRequestPathInfo().getSelectors();
            if (selectors.length == NUMBER_SELECTORS) {
                Response ebayAuthResponse = this.ebayService.authenticate();
                Response searchResponse = this.ebayService.getSearchByKeywords(ebayAuthResponse, selectors[SELECTOR_2], selectors[SELECTOR_3]);
                response.getWriter().write(searchResponse.getBody());
            } else {
                throw new RestClientException("The required selectors were not present. "
                    .concat("Selector #1 = 'ebay'. Selector #2 = search term (String). Selector #3 = limit (Int)."));
            }
        } catch (Exception e) {
            LOG.error("An error has occurred attempting to retrieve ebay data.", e);

            try {
                responseJson.put("error", true);
                response.getWriter().write(responseJson.toString());
            } catch (JSONException jsonException) {
                LOG.error("An error has occurred trying to write JSON.", jsonException);
            }
        }
    }
}
