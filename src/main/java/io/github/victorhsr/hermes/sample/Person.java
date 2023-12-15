package io.github.victorhsr.hermes.sample;

import io.github.victorhsr.hermes.core.annotations.DSLProperty;
import io.github.victorhsr.hermes.core.annotations.DSLRoot;

import java.util.List;
import java.util.Map;

@DSLRoot
public class Person {

    private String name;
    private Integer age;
    @DSLProperty("personAddress")
    private Address address;
    private List<List<String>> listOfList;
    private Map<Integer, List<String>> mapWithList;
    private CustomGeneric<Integer, Map<Integer, List<String>>, List<List<String>>, List<Integer>> customGeneric;

    public CustomGeneric<Integer, Map<Integer, List<String>>, List<List<String>>, List<Integer>> getCustomGeneric() {
        return customGeneric;
    }

    public void setCustomGeneric(CustomGeneric<Integer, Map<Integer, List<String>>, List<List<String>>, List<Integer>> customGeneric) {
        this.customGeneric = customGeneric;
    }

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

    public List<List<String>> getListOfList() {
        return listOfList;
    }

    public void setListOfList(List<List<String>> listOfList) {
        this.listOfList = listOfList;
    }

    public Map<Integer, List<String>> getMapWithList() {
        return mapWithList;
    }

    public void setMapWithList(Map<Integer, List<String>> mapWithList) {
        this.mapWithList = mapWithList;
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
