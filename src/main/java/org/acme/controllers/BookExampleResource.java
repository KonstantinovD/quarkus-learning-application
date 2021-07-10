package org.acme.controllers;

import io.quarkus.runtime.configuration.ProfileManager;
import org.acme.beans.jaxrsfilters.Address;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("/rest")
@Tag(name = "BookExampleResource",
  description = "config props, profile, " +
    "filters/interceptors, exception handling")
public class BookExampleResource {

    @Inject
    @ConfigProperty(
      name="quarkus.application.name",
      defaultValue ="my-app-name" )
    String applicationName;

    @GET
    @Path("/configproperty")
    @Produces(MediaType.TEXT_PLAIN)
    public String testConfigProperty() {
        return applicationName;
    }

    @GET
    @Path("/profile")
    @Produces(MediaType.TEXT_PLAIN)
    public String getProfileProgrammatically() {
        String currentProfile = ProfileManager.getActiveProfile();
        String appName = ConfigProvider.getConfig()
          .getValue("quarkus.application.name", String.class);
        return String.format("CurrentProfile: '%s', app name: '%s'",
          currentProfile, appName);
    }


    @GET
    @Path("/exception")
    @Produces(MediaType.TEXT_PLAIN)
    public String generateException() {
        throw new IllegalArgumentException("IAE to be throw");
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/testfilter")
    public Response testfilter(Address data) {
        data.setId(UUID.randomUUID().toString());
        data.setNumber("12B");
        data.setStreet("Ryzhova st.");
        return Response.ok(data).build();
    }

    @POST
    @Path("/testinterceptor")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String testinterceptor(String body) {
        return body;
    }
}
