package io.github.victorhsr.hermes.sample;

import io.github.victorhsr.hermes.core.annotations.DSLIgnore;
import io.github.victorhsr.hermes.core.annotations.DSLProperty;
import io.github.victorhsr.hermes.core.annotations.DSLRoot;
import io.github.victorhsr.hermes.sample.Address;

import java.util.List;

//
//
//import io.github.victorhsr.hermes.core.annotations.DSLIgnore;
//import io.github.victorhsr.hermes.core.annotations.DSLProperty;
//import io.github.victorhsr.hermes.core.annotations.DSLRoot;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.function.Consumer;
//import java.util.function.Supplier;
//import java.util.stream.Stream;
//
//
//class PersonD {
//
//    public static Person person(Consumer<Person>... options) {
//        Person person = new Person();
//        Stream.of(options).forEach(opt -> opt.accept(person));
//        return person;
//    }
//
//    public static Consumer<Person> phones(String... phones) {
//        return (person) -> person.setPhones(List.of(phones));
//    }
//
//    public static Consumer<Person> phonesObj(Phone... phones) {
//        List<Phone> phonesList = new ArrayList<>();
//        Stream.of(phones).forEach(s -> phonesList.add(s));
//
//        return (person) -> person.setPhonesObj(phonesList);
//    }
//
//    public static Phone phoneObj_item(Consumer<Phone>... options) {
//        Phone phone = new Phone();
//        Stream.of(options).forEach(opt -> opt.accept(phone));
//
//        return phone;
//    }
//
//    public static void main(String[] args) {
//        Person person = person(
//                phones(
//                        "2",
//                        "1"
//                ),
//                phonesObj(
//                        phoneObj_item(),
//                        phoneObj_item()
//                )
//        );
//    }
//
//}
//
//class Phone {
//    private String phoneNumber;
//
//    public String getPhoneNumber() {
//        return phoneNumber;
//    }
//
//    public void setPhoneNumber(String phoneNumber) {
//        this.phoneNumber = phoneNumber;
//    }
//}
//
@DSLRoot
public class Person {
    private String name;
    @DSLProperty("addressDefinitions")
    private Address address;

    private List<String> phones;

//    private List<Phone> phonesObj;
//
//    public List<Phone> getPhonesObj() {
//        return phonesObj;
//    }
//
//    public void setPhonesObj(List<Phone> phonesObj) {
//        this.phonesObj = phonesObj;
//    }

    @DSLIgnore
    private Integer age;

    private boolean admin;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", address=" + address +
                '}';
    }
}
