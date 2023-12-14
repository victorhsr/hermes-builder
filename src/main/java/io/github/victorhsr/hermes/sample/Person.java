package io.github.victorhsr.hermes.sample;

import io.github.victorhsr.hermes.core.annotations.DSLProperty;
import io.github.victorhsr.hermes.core.annotations.DSLRoot;

@DSLRoot
public class Person {

    private String name;
    private Integer age;
    @DSLProperty("personAddress")
    private Address address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", address=" + address +
                '}';
    }
}
