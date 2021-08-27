package com.gw.safty.common.utils.collection;

@FunctionalInterface
public interface IndexEachAction<T> {
    void action(T t, int index);
}
