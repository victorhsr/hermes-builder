package com.github.victorhsr.hermes.sample;

import com.github.victorhsr.hermes.core.annotations.DSLRoot;

import java.util.Objects;

@DSLRoot
public class CustomGenericKeyValuePair<K, V> {

    private K key;
    private V value;

    public void setKey(K key) {
        this.key = key;
    }

    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomGenericKeyValuePair<?, ?> that = (CustomGenericKeyValuePair<?, ?>) o;
        return Objects.equals(key, that.key) && Objects.equals(value, that.value);
    }
}
