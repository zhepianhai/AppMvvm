package com.gw.zph.core.util.collection;

@FunctionalInterface
public interface IndexEachAction<T> {
    void action(T t, int index);
}
