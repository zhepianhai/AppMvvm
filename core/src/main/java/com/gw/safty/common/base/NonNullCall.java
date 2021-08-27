package com.gw.safty.common.base;

@FunctionalInterface
public interface NonNullCall<T>{
    void change(T t);
}