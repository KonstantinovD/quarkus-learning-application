package org.acme.beans.restclient;

import org.acme.beans.exceptionmapper.HttpBinExceptionMapper;
import org.acme.beans.jaxrsfilters.LoggingFilter;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.CompletionStage;

// We create a MicroProfile REST client. It starts and ends with
//an interface:
@RegisterRestClient(baseUri = "https://httpbin.org/")
// This interface is a rest client stub for MicroProfile/CDI to pick up.
// The baseURI configuration is the target REST resource’s base address.
// With this annotation, I can later @Inject this interface wherever I
// need to connect to baseUri.
// Shout out to Kenneth Reitz, the proprietor of www.httpbin.org.
// Httpbin.org is an HTTP testing web service for Requests
// (Reitz’s library), a python HTTP library. It allows devs to test
// their code against what’s essentially a mock web service. So, you can
// copy-paste this code as is and it will have a service to talk to.
@RegisterProvider(LoggingFilter.class)
// configures the supplied class as a JAX-RS provider for this interface.
@RegisterProvider(HttpBinExceptionMapper.class)
// The exception mapper will be triggered only if an HTTP status code of
//400 or greater is returned by the service call
public interface HttpBinServiceDAO {

    @GET
    // @GET defines this method for executing a GET request against the
    // defined @Path. Remember, this will be appended to the baseURI, so
    // that the final request URI in this example will read as
    // https://httpbin.org/image/jpeg.
    @Path("/image/jpeg")
    @Produces("image/jpeg")
    // The CompletionStage return type signals to the application
    //runtime that this request is a reactive one. Used it here to wrap
    //the binary data in the response from httpbin.org, for that specific
    //endpoint. Flagging this as reactive makes this request asynchronous,
    //event-driven, and CPU-efficient. The best of three worlds.
    CompletionStage<byte[]> getImageAsync();

    @PATCH
    @Path("delay/{delay}")
    @Produces(MediaType.APPLICATION_JSON)
    Response patchRandom(@PathParam("delay") int delay) throws Exception;

    @POST
    @Path("anything/{anything}")
    @Produces(MediaType.TEXT_PLAIN)
    Response postAnything(@PathParam("anything") String anything);

    @GET
    @Path("/status/{codes}")
    void getAnyStatusCode(@PathParam("codes") String codes);
}
