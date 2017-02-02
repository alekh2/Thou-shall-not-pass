package com.flipkart.depcheck.Utils;

import com.flipkart.depcheck.exceptions.BadRequestException;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.JerseyWebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by prasanth.narra on 18/01/17.
 */
public class HttpUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);

    public static String hit(String url, String httpMethod, String payload) throws BadRequestException {
        JerseyClient client = JerseyClientBuilder.createClient(new ClientConfig());
        JerseyWebTarget webResource = client.target(url);
        Response response;
        if(httpMethod.equals(HttpMethod.GET))
            response = webResource.request(MediaType.APPLICATION_JSON).get(Response.class);
        else if(httpMethod.equals(HttpMethod.POST))
            response = webResource.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(Entity.json(payload), Response.class);
        else
            throw new BadRequestException("Only GET and POST are supported currently");
        if (response.getStatus() != 200 && response.getStatus() != 201) {
            LOGGER.error(response.readEntity(String.class));
            return null;
        }
        return response.readEntity(String.class);
    }
}
