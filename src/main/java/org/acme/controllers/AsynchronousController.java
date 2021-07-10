package org.acme.controllers;

import lombok.extern.slf4j.Slf4j;
import org.acme.beans.jaxrsfilters.UserDTO;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;

@Path("/async")
@Slf4j
@Tag(name = "AsynchronousController",
  description = "Controller for testing async")
public class AsynchronousController {

    @Inject
    ManagedExecutor managedExecutor;

    @GET
    @Path("/{name:[a-zA-Z]*}/userAsync")
    @Produces(MediaType.APPLICATION_JSON)
    public void getUserAsync(
      // The @Suspended and AsyncResponse class are the special sauce
      // here. When a method contains these two, it signals to the app
      // container that “Hey, just hand off the payload to the target
      // method and return immediately. Don’t wait for that method to
      // return!”.
      @Suspended final AsyncResponse async,
      final @PathParam("name") String name) {
        final Future<?> toComplete = managedExecutor.submit(
          () -> {
            log.info("Executing in a different thread");
            final UserDTO user = new UserDTO();
            user.setLogin(name);
            user.setPassword("password123");
            user.setUserId(UUID.randomUUID().toString());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException iex) {
                log.error("Unable to sleep into thread: " + iex);
            }
            // then send the result of the operation into the resume
            // method of the AsyncResponse class, which then sends the
            // response object back to the user on another thread.
           final Response response = Response.ok(user).build();
            async.resume(response);
          });
        // Note that the return type of the suspended method is void.
        // The response is going to go out on a different thread.
    }



    @GET
    @Path("/{name:[a-zA-Z]*}/userAsyncRx")
    // An alternative approach to signaling to the runtime that this
    // endpoint is an async endpoint is to return a CompletionStage.
    // A bonus is that with CompletionStage and CompletableFuture, you
    // get a wholly reactive programming approach that allows you to
    // specify exception handling scenarios among other things.
    // Technically, this is a reactive approach to handling the requests.
    @Produces(MediaType.APPLICATION_JSON)
    public CompletionStage<UserDTO> getUserRx(
      final @PathParam("name") String name) {
        final CompletionStage<UserDTO> completionStageResponse =
          new CompletableFuture<>();
        managedExecutor.submit(() -> {
            log.info("Executing in a different thread");
            final UserDTO user = new UserDTO();
            user.setLogin(name);
            user.setPassword("password123");
            user.setUserId(UUID.randomUUID().toString());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException iex) {
                log.error("Unable to sleep into thread: " + iex);
            }
            // For the reactive approach, we needed to downcast the
            // CompletionStage to a CompletableFuture and then call the
            // complete method; it has the same effect as
            // AsyncResponse#resume.
            ((CompletableFuture)completionStageResponse).complete(user);
        });
        return completionStageResponse;
    }

}
