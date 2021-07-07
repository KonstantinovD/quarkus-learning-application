package org.acme.beans;

import java.util.Random;

public interface FlavorTown {
    default Integer getRandomInt() {
        Random random = new Random();
        return random.nextInt();
    }

    default Integer getRandomInt2() {
        Random random = new Random();
        return random.nextInt();
    }
}
