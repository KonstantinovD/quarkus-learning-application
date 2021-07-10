package org.acme.controllers;

import org.acme.beans.restclient.HttpBinServiceDAO;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.annotations.cache.Cache;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

@RestController
@Path("/restclient")
@Tag(name = "RestClientController",
  description = "controller to send requests to another rest service")
public class RestClientController {

    // inject the REST client interface and mark it with the @RestClient
    // annotation. Again, MicroProfile REST clients are available in any
    // part of the Quarkus application; it doesn’t have to be inside of
    // a JAX-RS resource class.
    @Inject
    @RestClient
    HttpBinServiceDAO httpBinService;

    @GET
    @Path("/hello-image")
    @Cache(maxAge = 30)
    @Operation(summary = "Returns an image")
    @Produces("image/jpg")
    public Response helloImage()
      throws InterruptedException, ExecutionException {
        CompletionStage<byte[]> futureImage =
          httpBinService.getImageAsync();
        byte[] imageBytes = futureImage.toCompletableFuture().get();
        // use the generic Response object to construct a HTTP response
        // fit to carry an image. This is supported purely with the
        // JAX-RS standard. Simply pass an instance of StreamingOutput
        // to the entity method of Response.
        return Response.ok().entity((StreamingOutput) outputStream -> {
          outputStream.write(imageBytes);
          outputStream.flush();
        }).build();
    }

    @PATCH
    @Path("/delay/{delay}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response patchRandom(@PathParam("delay") int delay)
      throws Exception {
        return httpBinService.patchRandom(delay);
    }

    @POST
    @Path("/anything/{anything}")
    public Response postAnything(@PathParam("anything") String anything) {
        return httpBinService.postAnything(anything);
    }

    @GET
    @Path("/status/{codes}")
    public void getAnyStatusCode(@PathParam("codes") String codes) {
        httpBinService.getAnyStatusCode(codes);
    }



}
