package com.example.PersonDSL;

import io.github.victorhsr.hermes.annotations.Person;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class PersonDSL {
  public static Person person(Consumer<Person>... options) {
    final Person person = new Person();
    Stream.of(options).forEach(option -> option.accept(person));
    return person;
  }
}
