package io.github.victorhsr.hermes.sample;

import org.junit.jupiter.api.Test;

import java.util.List;

import static io.github.victorhsr.hermes.sample.AddressDSL.houseNumber;
import static io.github.victorhsr.hermes.sample.AddressDSL.street;
import static io.github.victorhsr.hermes.sample.PersonDSL.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SampleDslTest {

    public static final int AGE = 20;
    public static final String NAME = "Victor";
    public static final List<String> PHONE_NUMBERS = List.of("1234");
    public static final String CHARACTERISTIC_KEY = "hair";
    public static final String CHARACTERISTIC_VALUE = "brown";
    public static final int HOUSE_NUMBER = 99;
    public static final String STREET = "my-street";

    @Test
    public void testPerson() {
        // given
        Person expectedResult = buildExpectedPerson();

        // when
        Person actual = person(
                name(NAME),
                age(AGE),
                phoneNumbers(PHONE_NUMBERS),
                personAddress(
                        street(STREET),
                        houseNumber(HOUSE_NUMBER)
                ),
                characteristic(buildCharacteristic())
        );

        // then
        assertThat(actual).isEqualTo(expectedResult);
    }

    private Person buildExpectedPerson() {
        Person person = new Person();
        person.setAge(AGE);
        person.setName(NAME);
        person.setPhoneNumbers(PHONE_NUMBERS);
        person.setAddress(buildAddress());
        person.setCharacteristic(buildCharacteristic());

        return person;
    }

    private CustomGenericKeyValuePair<String, String> buildCharacteristic() {
        CustomGenericKeyValuePair<String, String> characteristics = new CustomGenericKeyValuePair<>();
        characteristics.setKey(CHARACTERISTIC_KEY);
        characteristics.setValue(CHARACTERISTIC_VALUE);

        return characteristics;

    }

    private Address buildAddress() {
        Address address = new Address();
        address.setHouseNumber(HOUSE_NUMBER);
        address.setStreet(STREET);

        return address;
    }

}