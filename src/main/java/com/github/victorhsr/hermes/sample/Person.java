package com.github.victorhsr.hermes.sample;

import com.github.victorhsr.hermes.core.annotations.DSLProperty;
import com.github.victorhsr.hermes.core.annotations.DSLRoot;

import java.util.List;
import java.util.Objects;

@DSLRoot
public class Person {

    private String name;
    private Integer age;
    @DSLProperty("personAddress")
    private Address address;
    private List<String> phoneNumbers;
    private CustomGenericKeyValuePair<String, String> characteristic;

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setPhoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public void setCharacteristic(CustomGenericKeyValuePair<String, String> characteristic) {
        this.characteristic = characteristic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(name, person.name) && Objects.equals(age, person.age) && Objects.equals(address, person.address) && Objects.equals(phoneNumbers, person.phoneNumbers) && Objects.equals(characteristic, person.characteristic);
    }
}
