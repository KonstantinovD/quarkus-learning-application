package org.acme.controllers;

import org.acme.beans.FlavorTown;
import org.acme.beans.VanillaCake;
import org.acme.beans.spring.AnotherSpringBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;


@RestController
@RequestMapping("/spring")
public class SpringController {

    // set package private or get INFO warning
    // 'Found unrecommended usage of private members (use package-private instead)'
    @Autowired
    @Named("anotherSpringBean")
    AnotherSpringBean anotherSpringBean;

    @GET
    @GetMapping(path = "/asb", produces = "text/plain")
    public String asb() {
        return anotherSpringBean.getSpringValue();
    }

    @Inject
    VanillaCake vanillaCake;

    @GetMapping(path = "/cake", produces = "text/plain")
    public int cucumber() {
        return vanillaCake.gimmeSomeFlavor(new FlavorTown() {
            @Override
            public Integer getRandomInt() {
                return 1234567;
            }
        });
    }
}
