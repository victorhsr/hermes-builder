package io.github.victorhsr.hermes.sample;

import static io.github.victorhsr.hermes.sample.PersonDSL.*;
import static io.github.victorhsr.hermes.sample.AddressDSL.*;

public class Loader {

    public static void main(String[] args) {
        final var person = person(
                name("victor"),
                addressDefinitions(
                        street("Rua Joaquim Mangueira"),
                        houseNumber(42)
                )
        );


        System.out.println("person = " + person);
    }

}
