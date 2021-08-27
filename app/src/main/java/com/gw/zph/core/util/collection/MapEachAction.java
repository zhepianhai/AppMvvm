package com.gw.zph.core.util.collection;

/**
 * 用于循环Map类型的数据的接口
 * <p> 将每一个对象Key，Value通过action(k,v)传递到调用的地方
 * @param <K> 当前循环对象的key
 * @param <V>当前循环对象的Vulue
 */
@FunctionalInterface
public interface MapEachAction<K, V> {
    void action(K k, V v);
}
