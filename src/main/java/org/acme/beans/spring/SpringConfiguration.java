package org.acme.beans.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfiguration {
    @Bean(name = "anotherSpringBean")
    public AnotherSpringBean anotherSpringBean() {
        AnotherSpringBean asb = new AnotherSpringBean();
        asb.setSpringValue("SPRING_VAL");
        return asb;
    }

    @Bean(name = "secondSpringBean")
    public AnotherSpringBean secondSpringBean() {
        AnotherSpringBean asb = new AnotherSpringBean();
        asb.setSpringValue("SECOND_SPRING_VAL");
        return asb;
    }
}
