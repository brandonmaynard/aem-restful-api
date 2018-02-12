package com.brandonmaynard.restful.clients;

import com.brandonmaynard.restful.exceptions.RestClientException;
import com.brandonmaynard.restful.models.Response;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import java.util.List;

/**
 * Simple Client.
 * @see <a href="https://github.com/brandonmaynard/aem-restful-api">aem-rest-api</a> GitHub repository for more information.
 * @author Brandon Maynard - https://www.linkedin.com/in/brandonmaynard/
 */
public class SimpleClient extends RestClient {

    /**
     * Method to submit a GET request.
     * @param url The URL to GET from
     * @return Response
     * @throws RestClientException exception
     */
    protected Response get(final String url) throws RestClientException {
        HttpGet httpGet = new HttpGet(url);
        return super.execute(httpGet);
    }

    /**
     * Method to submit a POST request.
     * @param url The URL to POST to
     * @param pairs Required fields for posting
     * @return Response
     * @throws RestClientException exception
     */
    protected Response post(final String url, final List<NameValuePair> pairs) throws RestClientException {
        HttpPost httpost = new HttpPost(url);
        httpost.setEntity(new UrlEncodedFormEntity(pairs, Consts.UTF_8));
        return super.execute(httpost);
    }
}
