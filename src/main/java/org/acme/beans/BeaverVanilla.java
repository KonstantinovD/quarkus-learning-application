package org.acme.beans;

import lombok.Getter;
import lombok.Setter;
import org.acme.beans.qualifiers.Rare;

import javax.enterprise.context.ApplicationScoped;
import java.util.Random;

@ApplicationScoped
@Setter
@Getter
@Rare
public class BeaverVanilla implements FlavorTown{
    // the ArC CDI engine wouldn’t know which implementation of
    // FlavorTown it needs to deliver. I need to create a qualifier that
    // further distinguishes the class that I’m interested in injecting
    public Integer getRandomInt() {
        return -11;
    }
}
