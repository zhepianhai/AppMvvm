package com.gw.zph.core;

@FunctionalInterface
public interface SupListTransformValueFunc<IN, OUT> {
    OUT call(IN var1);
}
