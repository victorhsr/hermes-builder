package com.example.PersonDSL;

import io.github.victorhsr.hermes.sample.Address;
import java.lang.String;
import java.util.function.Consumer;

public final class AddressDSL {
  public static Consumer<Address> street(String street) {
    return (address) -> address.setStreet(street);
  }

  public static Consumer<Address> number(int number) {
    return (address) -> address.setNumber(number);
  }
}
