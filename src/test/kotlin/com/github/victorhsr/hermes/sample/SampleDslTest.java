package com.github.victorhsr.hermes.sample;

import com.github.victorhsr.hermes.sample.lombok.DataAnnotationPojo;
import com.github.victorhsr.hermes.sample.lombok.SetterAnnotationOnFieldPojo;
import com.github.victorhsr.hermes.sample.lombok.SetterAnnotationPojo;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.victorhsr.hermes.sample.AddressDSL.houseNumber;
import static com.github.victorhsr.hermes.sample.AddressDSL.street;
import static com.github.victorhsr.hermes.sample.ExpectedCustomGenericKeyValuePairDSL.key;
import static com.github.victorhsr.hermes.sample.ExpectedCustomGenericKeyValuePairDSL.value;
import static com.github.victorhsr.hermes.sample.PersonDSL.*;
import static com.github.victorhsr.hermes.sample.lombok.DataAnnotationPojoDSL.*;
import static com.github.victorhsr.hermes.sample.lombok.SetterAnnotationOnFieldPojoDSL.*;
import static com.github.victorhsr.hermes.sample.lombok.SetterAnnotationPojoDSL.fieldWithoutAnnotation;
import static com.github.victorhsr.hermes.sample.lombok.SetterAnnotationPojoDSL.setterAnnotationPojo;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SampleDslTest {

    @Nested
    class NativeJavaTests {

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
                    characteristic(
                            key(CHARACTERISTIC_KEY),
                            value(CHARACTERISTIC_VALUE)
                    )
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

    @Nested
    class LombokTests {

        @Nested
        class ClassLevelAnnotationTests {

            @Test
            void DataAnnotationTest() {
                // given
                DataAnnotationPojo expected = new DataAnnotationPojo();
                expected.setFoo("foo");
                expected.setBar(true);

                // when
                DataAnnotationPojo actual = dataAnnotationPojo(
                        foo("foo"),
                        bar(true)
                );

                // then
                assertThat(actual).isEqualTo(expected);
            }

            @Test
            void SetterAnnotationTest() {
                // given
                SetterAnnotationPojo expected = new SetterAnnotationPojo();
                expected.setFieldWithoutAnnotation(true);

                // when
                SetterAnnotationPojo actual = setterAnnotationPojo(
                        fieldWithoutAnnotation(true)
                );

                // then
                assertThat(actual).isEqualTo(expected);
            }
        }

        @Nested
        class FieldLevelAnnotationTest {

            @Test
            void setterAnnotationOnFieldTest() {
                // given
                SetterAnnotationOnFieldPojo expected = new SetterAnnotationOnFieldPojo();
                expected.setSomeBoolean(true);
                expected.setSomeString("someString");

                // when
                SetterAnnotationOnFieldPojo actual = setterAnnotationOnFieldPojo(
                        someBoolean(true),
                        someString("someString")
                );

                // then
                assertThat(actual).isEqualTo(expected);
            }
        }
    }
}