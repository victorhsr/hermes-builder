package io.github.victorhsr.hermes.sample;

import io.github.victorhsr.hermes.core.annotations.DSLRoot;

import java.util.Objects;

@DSLRoot
public class Address {

    private String street;
    private Integer houseNumber;

    public void setStreet(String street) {
        this.street = street;
    }

    public void setHouseNumber(Integer houseNumber) {
        this.houseNumber = houseNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(street, address.street) && Objects.equals(houseNumber, address.houseNumber);
    }
}
