package org.acme;

import io.quarkus.runtime.annotations.CommandLineArguments;
import org.acme.beans.*;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.stream.Collectors;

@Path("/app")
public class SampleResource {

    @Inject
    VanillaCake vanillaCake;

    @GET
    @Path("/test")
    @Produces(MediaType.TEXT_PLAIN)
    public String test() {
        String n1 = String.valueOf(vanillaCake.gimmeSomeDeliciousVanilla());
        String n2 = String.valueOf(vanillaCake.gimmeSomeFlavor(new FlavorTown() {
            @Override
            public Integer getRandomInt() {
                return FlavorTown.super.getRandomInt();
            }
        }));
        return n1 + " " + n2;
    }


    // Можно переопределить имя bean-компонента в точке инъекции, а
    // также установить желаемый scope перед инъекцией:
    @Inject
    @Named("boldPotato") @ApplicationScoped
    PotatoBean potatoBean;

    @GET
    @Path("/potato")
    @Produces(MediaType.TEXT_PLAIN)
    public String potato() {
        return String.valueOf(potatoBean.getCountry());
    }
// but cannot for already defined as bean
/*  @Inject
    @Named("boldVanilla") @ApplicationScoped
    VanillaBean vanillaBean; */

    @Inject
    @CommandLineArguments
    String[] commandLineArgs;

    @GET
    @Path("/cliargs")
    @Produces(MediaType.TEXT_PLAIN)
    public String cliargs() {
        return String.join(" ", commandLineArgs);
    }

    @Inject //2 @Dependent classes - first is for SampleResource
    @Named("selectiveCucumber")
    CucumberBean cucumberBean;
    @Inject // second for CucumberBoxBean
    CucumberBoxBean cucumberBoxBean;

    @Inject //Produces method can be in any class (BeansProvider)
    @Named("secondSelectiveCucumber")
    CucumberBean secondCucumberBean;

    @GET
    @Path("/cucumber")
    @Produces(MediaType.TEXT_PLAIN)
    public String cucumber() {
        System.out.println(
          cucumberBoxBean.getCucumberBean().getWaterPercent());
        System.out.println(secondCucumberBean.getWaterPercent());
        return String.valueOf(cucumberBean.getWaterPercent());
    }

    @GET
    @Path("/rare")
    @Produces(MediaType.TEXT_PLAIN)
    public String rare() {
        return vanillaCake.getSecondVanillaBeanInt()
          + " " + vanillaCake.getRareBeaverVanillaInt();
    }
}
