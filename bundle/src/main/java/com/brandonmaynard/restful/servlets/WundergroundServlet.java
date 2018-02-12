package com.brandonmaynard.restful.servlets;

import com.brandonmaynard.restful.exceptions.RestClientException;
import com.brandonmaynard.restful.models.Response;
import com.brandonmaynard.restful.webservices.wunderground.WundergroundConfigurationService;
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
 * An example {@link SlingSafeMethodsServlet} extension for retrieving data from Wunderground.
 * @see <a href="https://github.com/brandonmaynard/aem-restful-api">aem-rest-api</a> GitHub repository for more information.
 * @author Brandon Maynard - https://www.linkedin.com/in/brandonmaynard/
 */
@Component(immediate = true, metatype = true)
@Service
@Properties(value = {
        @Property(name = "sling.servlet.methods", value = { "GET" }),
        @Property(name = "sling.servlet.extensions", value = { "json" }),
        @Property(name = "sling.servlet.selectors", value = { "wunderground" }),
        @Property(name = "sling.servlet.resourceTypes", value = { "restful/components/content/example-wunderground-submit" })
})
public class WundergroundServlet extends SlingSafeMethodsServlet {
    private static final Logger LOG = LoggerFactory.getLogger(WundergroundServlet.class);
    private static final long serialVersionUID = -6509232386911167725L;
    private static final int NUMBER_SELECTORS = 4;
    private static final int SELECTOR_2 = 1;
    private static final int SELECTOR_3 = 2;
    private static final int SELECTOR_4 = 3;
    private static final String CURRENT_CONDITIONS = "current";
    private static final String FORECAST = "forecast";

    @Reference
    private WundergroundConfigurationService wundergroundService;

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
                Response wundergroundResponse = new Response();

                if (CURRENT_CONDITIONS.equals(selectors[SELECTOR_2])) {
                    wundergroundResponse = wundergroundService.getCurrentConditions(selectors[SELECTOR_3], selectors[SELECTOR_4]);
                } else if (FORECAST.equals(selectors[SELECTOR_2])) {
                    wundergroundResponse = wundergroundService.getForecast(selectors[SELECTOR_3], selectors[SELECTOR_4]);
                } else {
                    wundergroundResponse.setBody(responseJson.put("error", true).toString());
                }

                response.getWriter().write(wundergroundResponse.getBody());
            } else {
                throw new RestClientException("The required selectors were not present. "
                    .concat("Selector #1 = 'wunderground'. Selector #2 = type (String). ")
                    .concat("Selector #3 = state (String). Selector #4 = city (String)."));
            }
        } catch (Exception e) {
            LOG.error("An error has occurred attempting to retrieve wunderground data.", e);

            try {
                responseJson.put("error", true);
                response.getWriter().write(responseJson.toString());
            } catch (JSONException jsonException) {
                LOG.error("An error has occurred trying to write JSON.", jsonException);
            }
        }
    }
}
