package org.acme.beans;


import javax.enterprise.context.Dependent;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Named;
import javax.ws.rs.Produces;
import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.util.Set;

public class BeansProvider {

    //Produces method can be in any class (BeansProvider)
    @Produces
    @Named("secondSelectiveCucumber")
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
