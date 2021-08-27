package com.gw.safty.common.utils.collection;

/**
 * 过滤器接口
 * <p>实现过滤方法即可
 * @param <T>
 */
@FunctionalInterface
public interface Filter<T> {

    /**
     * 过滤一个对象
     * @param t 循环中的item
     * @return true 表示当前 t 通过过滤规则，false相反
     */
    boolean filter(T t);
}
