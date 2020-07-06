//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.unity.common.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public final class SuperviseCollections {
    public SuperviseCollections() {
    }

    public static <C extends Collection<T>, T> C excludeNull(Collection<T> coll, Supplier<C> supplier) {
        return filter(coll, (item) -> {
            return item != null;
        }, supplier);
    }

    public static <C extends Collection<T>, T> C excludeNull(Collection<T> coll, Collector<T, ?, C> collector) {
        return filter(coll, (item) -> {
            return item != null;
        }, collector);
    }

    public static <C extends Collection<T>, T> C filter(Collection<T> coll, Predicate<T> predicate, Supplier<C> supplier) {
        return CollectionUtils.isEmpty(coll) ? (C) coll : (C) coll.stream().filter(predicate).collect(Collectors.toCollection(supplier));
    }

    public static <C extends Collection<T>, T> C filter(Collection<T> coll, Predicate<T> predicate, Collector<T, ?, C> collector) {
        return CollectionUtils.isEmpty(coll) ? (C) coll : (C) coll.stream().filter(predicate).collect(collector);
    }

    public static <T, R> Set<R> collectSet(Collection<T> coll, Function<T, R> func) {
        return CollectionUtils.isEmpty(coll) ? null : (Set) coll.parallelStream().map(func).collect(Collectors.toSet());
    }

    public static <T, K, V> Map<K, V> reduce(Collection<T> coll, V defaultValue, Function<T, K> keyProvider, BiFunction<V, T, V> reduceProc) {
        if (CollectionUtils.isEmpty(coll)) {
            return null;
        } else {
            Map<K, V> maps = Maps.newLinkedHashMap();
            Iterator var5 = coll.iterator();

            while (var5.hasNext()) {
                T t = (T) var5.next();
                K key = keyProvider.apply(t);
                V val = reduceProc.apply(maps.getOrDefault(key, defaultValue), t);
                maps.put(key, val);
            }

            return maps;
        }
    }

    public static <T, R> List<R> collect(List<T> coll, Function<T, R> func) {
        return (List) (CollectionUtils.isEmpty(coll) ? Lists.newArrayList() : (List) coll.stream().map(func).collect(Collectors.toList()));
    }

    public static <T, R> List<R> collect(List<T> coll, Function<T, R> func, Predicate<R> filter) {
        if (CollectionUtils.isEmpty(coll)) {
            return Lists.newArrayList();
        } else {
            Stream<R> stream = coll.stream().map(func);
            if (filter != null) {
                stream = stream.filter(filter);
            }

            return (List) stream.collect(Collectors.toList());
        }
    }

    public static <T> T get(Collection<T> coll, int index) {
        if (CollectionUtils.isEmpty(coll)) {
            return null;
        } else {
            int size = CollectionUtils.size(coll);
            if (index < 0) {
                index += size;
            }

            if (index >= size) {
                return null;
            } else {
                if (!SuperviseNumbers.between(index, 0, size)) {
                    index = SuperviseNumbers.fixed(index, size);
                }

                return !SuperviseNumbers.between(index, 0, size) ? null : IterableUtils.get(coll, index);
            }
        }
    }

    public static <T, K, V> Map<K, V> map(Collection<T> coll, Function<T, K> keyFunc, Function<T, V> itemFunc) {
        return coll == null ? null : coll.parallelStream().collect(Collectors.toMap(keyFunc, itemFunc, (a, b) -> b));
    }

    public static <T, K, V> Map<K, V> map(Collection<T> coll, Function<T, K> keyFunc, Function<T, V> itemFunc, BinaryOperator<V> mergeFunc) {
        return coll == null ? null : coll.parallelStream().collect(Collectors.toMap(keyFunc, itemFunc, mergeFunc));
    }

    public static <K, V> Map<K, V> map(Collection<V> coll, Function<V, K> func) {
        return map(coll, func, (item) -> item);
    }

    public static final <T> T getWithin(Collection<T> list, int index) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        } else {
            int size = CollectionUtils.size(list);
            return !SuperviseNumbers.between(index, 0, size) ? null : IterableUtils.get(list, index);
        }
    }

    public static boolean allEmpty(Iterable<?> iters) {
        if (iters == null) {
            return true;
        } else {
            Iterator var1 = iters.iterator();

            Object obj;
            do {
                if (!var1.hasNext()) {
                    return true;
                }

                obj = var1.next();
            } while (obj == null);

            return false;
        }
    }

    public static <K, V, T> Map<K, V> accumulate(Collection<T> coll, Map<K, V> total, Function<T, K> keyFunc, BiFunction<T, V, V> accumulateFunc, V initVal) {
        Map<K, V> results = total == null ? Maps.newHashMap() : total;
        Iterator var8 = coll.iterator();

        while (var8.hasNext()) {
            T record = (T) var8.next();
            K hint = keyFunc.apply(record);
            V result = (V) ((Map) results).getOrDefault(hint, initVal);
            ((Map) results).put(hint, accumulateFunc.apply(record, result));
        }

        return (Map) results;
    }

    public static <T> Set<T> fillTo(Set<T> coll, Set<T> another, int size) {
        if (coll.size() < size) {
            Iterator iter = another.iterator();

            while (iter.hasNext()) {
                coll.add((T) iter.next());
                if (coll.size() == size) {
                    break;
                }
            }
        }

        return coll;
    }

    public static <T> T random(Collection<T> coll) {
        return get(random(coll, 1), 0);
    }

    public static <T> Collection<T> random(Collection<T> coll, Integer num) {
        if (coll.isEmpty()) {
            return null;
        } else {
            new Random(System.currentTimeMillis());
            int size = CollectionUtils.size(coll);
            if (num == null) {
                num = 1;
            }

            List<T> toSort = Lists.newArrayList(coll);
            Collections.shuffle(toSort);
            return sub(toSort, 0, num);
        }
    }

    public static <T> List<T> sub(List<T> list, int offset) {
        return sub(list, offset, (Integer) null);
    }

    public static <T> List<T> sub(List<T> list, int offset, Integer fetchSize) {
        List<T> sub = Lists.newArrayList();
        int size = CollectionUtils.size(list);
        if (size < 1) {
            return sub;
        } else {
            if (offset < 0) {
                offset += size;
            }

            offset = SuperviseNumbers.fixed(offset, 0, size - 1);
            int toIndex = fetchSize == null ? size - 1 : Math.min(offset + fetchSize, size);
            return list.subList(offset, toIndex);
        }
    }

    public static <T> T peek(Collection<T> col) {
        return col.stream().findFirst().orElse((T) null);
    }

    public static <V> List<V> flatten(List<Collection<V>> items) {
        List<V> result = Lists.newArrayList();
        Iterator var2 = items.iterator();

        while (var2.hasNext()) {
            Collection<V> is = (Collection) var2.next();
            result.addAll(is);
        }

        return result;
    }

    public static <C extends Collection<T>, T> C fill(C coll, int n, T... items) {
        for (int i = 0; i < n; ++i) {
            Object[] var4 = items;
            int var5 = items.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                T item = (T) var4[var6];
                coll.add(item);
            }
        }

        return coll;
    }

    public static <T> T copyMap2Beans(Map<String, Object> map, Class<T> clazz) {

        try {
            T t = clazz.newInstance();
            map.forEach((k, v) -> {
                try {
                    PropertyDescriptor pd = new PropertyDescriptor(k, clazz);
                    switch (pd.getPropertyType().getName()) {
                        case "int":
                            pd.getWriteMethod().invoke(t, ((Number) v).intValue());
                            break;
                        default:
                            pd.getWriteMethod().invoke(t, v);
                    }

                } catch (IntrospectionException ignored) {
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

            });
            return t;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
