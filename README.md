# Hermes Builder
[![Maven Build](https://github.com/victorhsr/hermes-builder/workflows/Maven%20Build/badge.svg)](https://github.com/victorhsr/hermes-builder/actions?query=workflow%3A"Maven+Build")
[![Coverage Status](https://coveralls.io/repos/github/victorhsr/hermes-builder/badge.svg)](https://coveralls.io/github/victorhsr/hermes-builder)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/mit)
[![](https://jitpack.io/v/victorhsr/hermes-builder.svg)](https://jitpack.io/#victorhsr/hermes-builder)

Hermes is a library designed to streamline the generation of fluent object builders for your Java classes. Employing a declarative approach, it produces a straightforward Domain Specific Language (DSL) to facilitate the construction of object instances.

## Adding Hermes to your pom file

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.victorhsr</groupId>
        <artifactId>hermes-builder</artifactId>
        <version>1.0.0-beta</version>
    </dependency>
</dependencies>
```

## Usage
Suppose we have classes `Person`, `Address` and `MetaDataWrapper` annotated with `@DSLRoot` and `@DSLProperty`:

```Java
@DSLRoot
public class Person {

    private String name;
    private Integer age;
    private Address address;
    @DSLProperty("metaData")
    private MetaDataWrapper<String, String> metaDataWrapper;

    // getters and setters...
}

@DSLRoot
public class Address {

    private String street;
    private Integer houseNumber;

    // getters and setters...
}

@DSLRoot
public class MetaDataWrapper<K, V> {

    private K key;
    private V value;

    // getters and setters...
}
```

The plugin enables creating a `Person` instance succinctly:
```Java
Person personFromDsl = 
    person(
        name("Victor"),
        age(99),
        address(
            street("Foo street"),
            houseNumber(99)
        ),
        metaData(
            key("LAST_LOGIN"),
            value("01-01-2024")
        )
    );
````

Note that there's no need to instantiate `Address` or `MetaDataWrapper`, and more descriptive names for Person's fields can be used.

## Annotations

- **@DSLRoot** - Indicates the intention to create a builder for the annotated class;
- **@DSLProperty** - Customizes the method name generated for a specific field. The default is the original field name;
- **@DSLIgnore** - Marks a field to be excluded when generating DSL code;

# Comparison with Traditional Java Classes

The equivalent implementation in traditional Java code involves a considerable amount of boilerplate:

```Java

private Person buildPerson() {
    Person person = new Person();
    person.setAge(99);
    person.setName("Victor");
    person.setAddress(buildAddress());
    person.setMetaDataWrapper(buildMetaDataWrapper());

    return person;
}

private MetaDataWrapper<String, String> buildMetaDataWrapper() {
    MetaDataWrapper<String, String> metaData = new MetaDataWrapper<>();
    metaData.setKey("LAST_LOGIN");
    metaData.setValue("01-01-2024");

    return characteristics;

}

private Address buildAddress() {
    Address address = new Address();
    address.setHouseNumber(99);
    address.setStreet("Foo street");

    return address;
}
```
That's a lot of code, almost twice the lines of our previous implementation. The Hermes Builder significantly reduces this boilerplate, providing a more concise and readable alternative.

# Using Lombok for comparison

An alternative approach using Lombok involves a cleaner syntax but may lack readability and customization

```Java
public Person buildPerson() {
    return Person.builder()
            .name("Victor")
            .age(99)
            .address(buildAddress())
            .setMetaDataWrapper(buildMetaDataWrapper())
            .build();
}

private MetaDataWrapper<String, String> buildMetaDataWrapper() {
    return MetaDataWrapper
            .builder()
            .key("LAST_LOGIN")
            .value("01-01-2024")
            .build()

}

private Address buildAddress() {
    return Address.builder()
            .houseNumber(99)
            .street("Foo street")
            .build()
}
```
In summary, the Hermes Builder strikes a balance between conciseness and customization, offering a powerful solution for building objects in a declarative manner.
