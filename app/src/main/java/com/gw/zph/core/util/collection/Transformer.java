package com.gw.zph.core.util.collection;

/**
 * 用于对
 * @param <R>
 * @param <T>
 */
@FunctionalInterface
public interface Transformer<R, T> {
    R transform(T t);
}
