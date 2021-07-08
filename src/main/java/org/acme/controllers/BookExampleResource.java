package org.acme.controllers;

import io.quarkus.runtime.configuration.ProfileManager;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/rest")
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
}
