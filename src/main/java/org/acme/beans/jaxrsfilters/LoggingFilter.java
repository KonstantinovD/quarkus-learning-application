package org.acme.beans.jaxrsfilters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

// Аннотация @Provider отмечает этот класс как поставщик функций
// JAX-RS. Quarkus (by contract) регистрирует этот класс и выполняет
// любую функцию JAX-RS, которую этот класс поставляет
@Provider
// The @Priority annotation sets the priority of this filter relative
// to any other filters available. @Priority is provided by CDI.
@Priority(1)
public class LoggingFilter implements
  ContainerRequestFilter, ContainerResponseFilter {
    private final Logger logger =
      LoggerFactory.getLogger(LoggingFilter.class.getName());

    // The ContainerRequestFilter and ContainerResponseFilter
    // interfaces define the contract for implementing inbound and
    // outbound message filters

    // before request executed
    @Override
    public void filter(
      ContainerRequestContext requestContext) throws IOException {
        if (requestContext.getUriInfo().getPath().contains("/testfilter")) {
            //get a hold of the inbound data -> we can do anything with it
            BufferedInputStream iStream = new BufferedInputStream(
              requestContext.getEntityStream());

            byte[] inputContent = new byte[iStream.available()];
            iStream.read(inputContent);
            String body = new String(inputContent, "utf-8");
            logger.info("Inside request filter. Message size: "
              + inputContent.length + "; Message on the way in: " + body);
            // put the inbound data back where we found it. This is
            // important: you must reset the inbound message stream;
            // otherwise, the destination resource method will not see
            // anything when processing gets to that point.
            requestContext.setEntityStream(new ByteArrayInputStream(inputContent));
        }
    }

    // after request executed
    @Override
    public void filter(
      ContainerRequestContext requestContext,
      ContainerResponseContext responseContext) throws IOException {
        if (requestContext.getUriInfo().getPath().contains("/testfilter")) {
            logger.info("Message on the way out: "
              + responseContext.getEntity());
        }
    }
}
