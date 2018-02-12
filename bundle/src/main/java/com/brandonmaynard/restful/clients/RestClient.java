package com.brandonmaynard.restful.clients;

import com.brandonmaynard.restful.exceptions.RestClientException;
import com.brandonmaynard.restful.models.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Restful Client.
 * @see <a href="https://github.com/brandonmaynard/aem-restful-api">aem-rest-api</a> GitHub repository for more information.
 * @author Brandon Maynard - https://www.linkedin.com/in/brandonmaynard/
 */
class RestClient {

    private static final Logger LOG = LoggerFactory.getLogger(RestClient.class);

    /**
     * Method to execute a request.
     * @param requestBase HttpPost
     * @return Response
     * @throws RestClientException exception
     */
    Response execute(final HttpRequestBase requestBase) throws RestClientException {
        Response response = new Response();
        CloseableHttpClient httpclient = HttpClients.createDefault();

        try {
            try (CloseableHttpResponse closeableresponse = httpclient.execute(requestBase)) {
                response = buildResponse(closeableresponse);
            }
        } catch (IOException e) {
            LOG.error("An error has occurred. Unable to execute http object.", e);
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                LOG.error("An error has occurred. Unable to close httpclient object.", e);
            }
        }

        return response;
    }

    /**
     * Build the Response object.
     * @param closeableresponse CloseableHttpResponse
     * @return Response
     * @throws RestClientException exception
     */
    private Response buildResponse(final CloseableHttpResponse closeableresponse) throws RestClientException {
        Response response = new Response();
        response.setStatusCode(closeableresponse.getStatusLine().getStatusCode());

        try {
            HttpEntity entity = closeableresponse.getEntity();
            InputStream rstream = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(rstream));
            String body = StringUtils.EMPTY;

            String line;
            while ((line = reader.readLine()) != null) {
                body += line;
            }

            response.setBody(body);
            reader.close();

            if (response.getStatusCode() != HttpStatus.SC_OK && response.getStatusCode() != HttpStatus.SC_CREATED) {
                throw new RestClientException("Call did not come back with a 200 or 201. Raw error response:\n"
                    .concat(response.getBody())
                );
            }
        } catch (IOException e) {
            LOG.error("An error has occurred. Unable to manipulate Closeable objects (InputStream, BufferedReader).", e);
        }

        return response;
    }
}
