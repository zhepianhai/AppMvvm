package com.gw.zph.core.util.collection;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 集合数据类型的工具类
 * <p> 拟用于实现更直观简洁的方式对结合进行操作
 */
public class CollectionsOpt {
    /**
     * 对{@link List<T>}过滤进行过滤
     * <p>并返回新的 {@link List<T>}
     * @param list 需要过滤的数据
     * @param filter 过滤器 {@link Filter}
     * @param <T> 具体对象的类型
     * @return 过滤得到的结果
     */
    public static<T> List<T> filter(@NonNull List<T> list, @NonNull Filter<T> filter){
        ArrayList<T> ts = new ArrayList<>();
        if (isEmpty(list)) return ts;
        for (T t : list) {
            if (filter.filter(t)){
                ts.add(t);
            }
        }
        return ts;
    }

    /**
     * 对 {@link List<T>}进行变换
     * <p>并返回新的 {@link List<T>}
     * @param list 需要过滤的数据
     * @param transformer 过滤器 {@link Filter}
     * @param <T> 具体对象的类型
     * @return 转换后的数据，{@link List<T>}将重新创建，但是{@link List<T>}保存的数据
     * 将直接使用 transform 函数的返回值。
     */
    public static <T, R> List<R> transform(@NonNull List<T> list, @NonNull Transformer<R, T> transformer){
        ArrayList<R> result = new ArrayList<>();
        if (isEmpty(list)) return result;
        for (T t : list) {
            result.add(transformer.transform(t));
        }
        return result;
    }

    public static <T> T first(List<T> list, Filter<T> filter){
        if (isEmpty(list)) return null;
        for (T t : list) {
            if (filter.filter(t)) {
                return t;
            }
        }
        return null;
    }

    /**
     * 循环工具函数
     * @param list 需要循环的数据
     * @param action 循环执行体, 每次循环执行一次
     * @param <T> 具体的数据类型
     */
    public static <T> void forEach(@NonNull List<T> list, @NonNull ListEachAction<T> action){
        for (T t : list) {
            action.action(t);
        }
    }

    /**
     * 循环工具函数
     * @param list 需要循环的数据
     * @param action 循环执行体, 每次循环执行一次，并携带循环对象即索引
     * @param <T> 具体的数据类型
     */
    public static <T> void forEachWithIndex(@NonNull List<T> list, @NonNull IndexEachAction<T> action){
        for (int i=0; i<list.size(); i++){
            action.action(list.get(i), i);
        }
    }

    public static <T> boolean isEmpty(List<T> list){
        return list == null || list.isEmpty();
    }
    public static <T> boolean isEmpty(T[] array){
        return array == null || array.length <= 0;
    }
    public static <T> boolean isNotEmpty(T[] array){
        return !isEmpty(array);
    }

    public static <T> int sizeOrZero(List<T> list){
        if (isEmpty(list)) return 0;
        return list.size();
    }

    /**
     * 判断某参数是否在可变参数列表中
     * @param source 比较对象
     * @param index 取可变参数的位置
     * @param objs 可变参数列表
     * @param <T> 参数实际类型
     * @return true 表示 source 在可变参数objs的第index个位置
     */
    public static <T> boolean instanceIn(T source, int index, T ... objs){
        if (objs == null || objs.length <= index) return false;
        return objs[index] == source;
    }
    public static <T> boolean isNotEmpty(List<T> list){
        return !isEmpty(list);
    }

    public static <T> T getLast(List<T> list){
        if (isEmpty(list)) return null;
        return (T)list.get(list.size() - 1);
    }
    public static <T> T getFrist(List<T> list){
        if (isEmpty(list)) return null;
        return (T)list.get(0);
    }
    public static <K, V> void forEach(@NonNull Map<K, V> map, @NonNull MapEachAction<K, V> a){
        for (Map.Entry<K, V> kvEntry : map.entrySet()) {
            a.action(kvEntry.getKey(), kvEntry.getValue());
        }
    }
}


