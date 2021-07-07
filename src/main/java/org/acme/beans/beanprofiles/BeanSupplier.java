package org.acme.beans.beanprofiles;

import io.quarkus.arc.DefaultBean;
import io.quarkus.arc.profile.IfBuildProfile;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Produces;

@Slf4j
public class BeanSupplier {
    @Produces
    @RequestScoped
    @IfBuildProfile("test")
    public ProfileClass profileClassTest(){
        log.info("profile: TEST");
        ProfileClass profileClass = new ProfileClass();
        profileClass.setProfile("TEST");
        return profileClass;
    }

    @Produces
    @RequestScoped
    @IfBuildProfile("dev")
    public ProfileClass profileClassDev(){
        log.info("profile: DEV");
        ProfileClass profileClass = new ProfileClass();
        profileClass.setProfile("DEV");
        return profileClass;
    }

    @Produces
    @RequestScoped
    @IfBuildProfile("prod")
  //@UnlessBuildProfile("prod") - if any profile except 'prod'
    public ProfileClass profileClassProd(){
        log.info("profile: PROD");
        ProfileClass profileClass = new ProfileClass();
        profileClass.setProfile("PROD");
        return profileClass;
    }

    // @DefaultBean - it is an annotation that lets you set a bean to be a
    // default option, in case of injections. When there isn’t any
    // available source of ProfileClass anywhere else in the Quarkus app,
    // DefaultBean will step in and supply the default. If another
    // implementation exists, that implementation will be applied.
    @Produces
    @ApplicationScoped
    @DefaultBean
    public ProfileClass profileClassDefault(){
        log.info("profile: NOT DEFINED");
        ProfileClass profileClass = new ProfileClass();
        profileClass.setProfile("DEFAULT");
        return profileClass;
    }
}
