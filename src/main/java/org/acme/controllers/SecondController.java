package org.acme.controllers;

import org.acme.beans.interceptors.Generator;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/app")
public class SecondController {

    @Inject
    Generator generatorForInterceptor;

    @GET
    @Path("/intercept/{str}")
    @Produces(MediaType.TEXT_PLAIN)
    public void intercept(@PathParam("str") String str) {
        generatorForInterceptor.generate(str);
    }

}
