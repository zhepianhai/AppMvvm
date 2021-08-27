package com.gw.safty.common.utils.collection;

/**
 * 显现此接口用于实现 {@link java.util.List}类型的循环
 * @param <T> {@link java.util.List} 保存的类型
 */
@FunctionalInterface
public interface ListEachAction<T> {
    /**
     * 每次循环一个{@link java.util.List}中的对象都会调用这个方法
     * <p> 可在此方法内做处理
     * @param t 当前循环的对象
     */
    void action(T t);
}
