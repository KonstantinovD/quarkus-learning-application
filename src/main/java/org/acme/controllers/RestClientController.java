package org.acme.controllers;

import org.acme.beans.restclient.HttpBinServiceDAO;
import org.eclipse.microprofile.faulttolerance.Asynchronous;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
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
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.time.temporal.ChronoUnit;
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

// ---- MicroProfile Fault Tolerance API ----
// MicroProfile’s fault tolerance strategy revolves around five
// annotations:
// - 1) @Timeout to manage the wait time before reporting failure.
// - 2) @Retry to define retry tactics for failed executions.
// - 3) @Fallback to define alternative handling in case of failure.
// - 4) @Bulkhead to control the limits of concurrent access to a method;
//      helps to prevent overwhelming the resource. It’s a solid way to
//      manage the throughput to asynchronous methods.
// - 5) @CircuitBreaker to fail fast where applicable.


    @GET
    @Path("/hello-image")
    @Cache(maxAge = 30)
    @Operation(summary = "Returns an image")
    @Produces("image/jpg")
    @Retry(delay = 2000, delayUnit = ChronoUnit.MILLIS, maxRetries = 4,
    retryOn = {SocketTimeoutException.class, ConnectException.class})
    // This method should be retried four times, with a delay of
    // 2 seconds between each attempt. The retries should be attempted
    // only in case of a SocketTimeoutException/ConnectionException.
    @Asynchronous
    // Asynchronous to pool excess requests.
    @Bulkhead(value=10, waitingTaskQueue = 30)
    // This will now allow ten concurrent requests and a backlog
    // of 30 waiting for processing.
    // Any more and a BulkHeadException will be thrown!
    @Timeout(value = 5000, unit = ChronoUnit.MILLIS)
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
