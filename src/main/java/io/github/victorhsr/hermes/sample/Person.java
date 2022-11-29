package io.github.victorhsr.hermes.sample;


import io.github.victorhsr.hermes.core.annotations.DSLIgnore;
import io.github.victorhsr.hermes.core.annotations.DSLProperty;
import io.github.victorhsr.hermes.core.annotations.DSLRoot;

@DSLRoot
public class Person {
    private String name;
    @DSLProperty("addressDefinitions")
    private Address address;

    @DSLIgnore
    private Integer age;

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

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", address=" + address +
                '}';
    }
}
