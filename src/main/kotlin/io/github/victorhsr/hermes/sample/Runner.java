package io.github.victorhsr.hermes.sample;

import static com.example.PersonDSL.AddressDSL.number;
import static com.example.PersonDSL.AddressDSL.street;
import static com.example.PersonDSL.PersonDSL.*;

public class Runner {

    public static void main(String[] args) {
        Person person = buildPerson();
        System.out.println("person = " + person);
    }

    public static Person buildPerson() {
        return person(
                name("victor"),
                address(
                        street("street x"),
                        number(42)
                )
        );
    }

}

