package io.github.victorhsr.hermes.sample;

import io.github.victorhsr.hermes.core.annotations.DSLRoot;

@DSLRoot
public class Person {
    private String name;
    private Address address;

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
}
