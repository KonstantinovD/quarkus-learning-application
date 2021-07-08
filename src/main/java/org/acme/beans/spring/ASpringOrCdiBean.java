package org.acme.beans.spring;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.inject.Named;

@Component
@Named("aSpringOrCdiBean")
@Getter
@Setter
public class ASpringOrCdiBean {
    String value;
}
