package io.github.victorhsr.hermes.sample;

import java.util.HashMap;
import java.util.Map;

import static io.github.victorhsr.hermes.sample.AddressDSL.houseNumber;
import static io.github.victorhsr.hermes.sample.AddressDSL.street;
import static io.github.victorhsr.hermes.sample.PersonDSL.*;

public class Loader {

    public static void main(String[] args) {
        Person person = buildPerson();
        System.out.println("person = " + person);
        Map<Object, Object> objectObjectMap = new HashMap<>();
        Person personDSL = buildPersonDSL();
        System.out.println("personDSL = " + personDSL);
    }

    private static Person buildPersonDSL() {
        return person(
                name("victor"),
                age(29),
                personAddress(
                        street("Joaquim Mangueira"),
                        houseNumber(42)
                )
        );
    }

    private static Person buildPerson() {
        Person person = new Person();
        person.setName("Victor");
        person.setAge(29);
        person.setAddress(buildAddress());

        return person;
    }

    private static Address buildAddress() {
        Address address = new Address();
        address.setStreet("Joaquim Mangueira");
        address.setHouseNumber(42);

        return address;
    }


}
