package org.acme.beans;

import lombok.Getter;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@RequestScoped
@Getter
public class CucumberBoxBean {

    @Inject //2 @Dependent classes - second is for CucumberBoxBean
    @Named("selectiveCucumber")
    CucumberBean cucumberBean;
}
