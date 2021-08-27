package com.gw.safty.common.utils.collection;

/**
 * 用于对
 * @param <R>
 * @param <T>
 */
@FunctionalInterface
public interface Transformer<R, T> {
    R transform(T t);
}
