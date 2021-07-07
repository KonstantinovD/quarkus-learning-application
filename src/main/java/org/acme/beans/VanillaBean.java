package org.acme.beans;

import lombok.Getter;
import lombok.Setter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

// Give it a scope or a name – with the @Named annotation. You can
// then use this bean in another bean
@ApplicationScoped
@Named("deliciousVanilla")
@Setter
@Getter
public class VanillaBean implements FlavorTown{
    long flavorStrength;
    String countryOfOrigin;

    public Integer getRandomInt() {
        return 42;
    }
}
