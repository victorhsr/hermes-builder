package com.example.PersonDSL;

import io.github.victorhsr.hermes.sample.Address;
import io.github.victorhsr.hermes.sample.Person;
import java.lang.String;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class PersonDSL {
  public static Consumer<Person> name(String name) {
    return (person) -> person.setName(name);
  }

  public static Consumer<Person> address(Consumer<Address>... options) {
    final io.github.victorhsr.hermes.sample.Address address = new io.github.victorhsr.hermes.sample.Address();
    Stream.of(options).forEach(option -> option.accept(address));
    return (person) -> person.setAddress(address);
  }
}
