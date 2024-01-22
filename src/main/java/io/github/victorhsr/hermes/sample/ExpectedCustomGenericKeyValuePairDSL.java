package io.github.victorhsr.hermes.sample;

import java.util.function.Consumer;
import java.util.stream.Stream;

public class ExpectedCustomGenericKeyValuePairDSL {

    public static Consumer<CustomGenericKeyValuePair> key(Object key) {
        return (customGenericKeyValuePair) -> customGenericKeyValuePair.setKey(key);
    }

    public static Consumer<CustomGenericKeyValuePair> value(Object value) {
        return (customGenericKeyValuePair) -> customGenericKeyValuePair.setValue(value);
    }

    @SafeVarargs
    public static CustomGenericKeyValuePair customGenericKeyValuePair(Consumer<CustomGenericKeyValuePair>... options) {
        final CustomGenericKeyValuePair customGenericKeyValuePair = new CustomGenericKeyValuePair();
        Stream.of(options).forEach(option -> option.accept(customGenericKeyValuePair));
        return customGenericKeyValuePair;
    }

//    public static Consumer<Person> characteristic(Consumer<CustomGenericKeyValuePair>... options) {
//        final CustomGenericKeyValuePair customGenericKeyValuePair = new CustomGenericKeyValuePair();
//        Stream.of(options).forEach(option -> option.accept(customGenericKeyValuePair));
//        return (person) -> person.setCharacteristic(customGenericKeyValuePair);
//    }
}
