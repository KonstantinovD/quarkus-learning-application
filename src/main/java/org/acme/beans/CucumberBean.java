package org.acme.beans;

import lombok.Getter;
import lombok.Setter;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Named;
import javax.ws.rs.Produces;
import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.util.Set;

@Setter
@Getter
public class CucumberBean {
    private Float waterPercent;
    String countryOfOrigin;

    @Produces
    @Named("selectiveCucumber")
    @Dependent
    public CucumberBean cucumberBean(InjectionPoint injectionPoint) {
        CucumberBean cucumberBean = new CucumberBean();
        cucumberBean.setWaterPercent(0.73f);
        cucumberBean.setCountryOfOrigin("Belarus");

        String typeName = injectionPoint.getType().getTypeName();
        Member mmb = injectionPoint.getMember();
        Set<Annotation> set = injectionPoint.getQualifiers();

        return cucumberBean;
    }
}
