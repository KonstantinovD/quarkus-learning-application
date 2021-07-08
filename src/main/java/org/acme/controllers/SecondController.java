package org.acme.controllers;

import org.acme.beans.StartupConfigCheck;
import org.acme.beans.beanprofiles.ProfileClass;
import org.acme.beans.interceptors.Generator;
import org.acme.beans.spring.AnotherSpringBean;
import org.acme.beans.spring.ApplicationContextAwareReplacement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.inject.Inject;
import javax.inject.Named;
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


    @Inject
    StartupConfigCheck startupConfigCheck;

    @GET
    @Path("/configcheck")
    @Produces(MediaType.TEXT_PLAIN)
    public String configcheck() {
        return startupConfigCheck.getMessage();
    }


    @Inject
    ProfileClass profileClass;

    @GET
    @Path("/profile")
    @Produces(MediaType.TEXT_PLAIN)
    public String profile() {
        return profileClass.getProfile();
    }


    // We can use both @Named and @Qualifier
    //@Named("anotherSpringBean")
    @Autowired
    @Qualifier("anotherSpringBean")
    AnotherSpringBean asb;
    @Named("secondSpringBean")
    @Inject
    AnotherSpringBean ssb;
    @GET
    @Path("/springbean")
    @Produces(MediaType.TEXT_PLAIN)
    public String springbean() {
        return asb.getSpringValue() + " | " + ssb.getSpringValue();
    }


    @Inject
    ApplicationContextAwareReplacement acar;
    @GET
    @Path("/appcontextaware")
    @Produces(MediaType.TEXT_PLAIN)
    public String appcontextaware() {
        return acar.getBean().getValue();
    }
}
