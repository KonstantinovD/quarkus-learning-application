package org.acme.controllers;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

@Path("/secured")
@Tag(name = "SecuredController", description ="")
public class SecuredController {

    @GET
    @Path("/{name:[a-zA-Z]*}/namelenght")
    @RolesAllowed("VIP")
    // Having added @RolesAllowed("VIP") to that resource method,
    // we allowing only JWT claims with “VIP” in the role claim
    // to access that resource; therefore, it’s crucial that
    // the role be set as a claim on the JWT.
    public void getNameAsync(@Suspended final AsyncResponse async,
                             @PathParam("name") String name) {

    }

}
