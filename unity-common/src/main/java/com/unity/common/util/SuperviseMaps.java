//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.unity.common.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
public final class SuperviseMaps {

    public SuperviseMaps() {
    }

    public static <K extends Comparable, V> Map<K, V> putAll(Map<K, V> map, Object... kvs) {
        if (map != null && kvs != null && kvs.length != 0) {
            for (int i = 0; i < kvs.length; i += 2) {
                map.put((K) kvs[i], i + 1 < kvs.length ? (V) kvs[i + 1] : null);
            }

            return map;
        } else {
            return map;
        }
    }

    public static <K, V> Map<K, V> merge(Map<K, V> base, Map<K, V> toMerge) {
        if (MapUtils.isEmpty(base)) {
            return toMerge;
        } else if (MapUtils.isEmpty(toMerge)) {
            return base;
        } else {
            Map<K, V> newMap = Maps.newLinkedHashMap(base);
            newMap.putAll(toMerge);
            return newMap;
        }
    }

    public static <K> Map<K, Double> incr(Map<K, Double> map, K field, Double val) {
        return doIncr(map, field, val, Double.class);
    }

    public static <K> Map<K, Integer> incr(Map<K, Integer> map, K field, Integer val) {
        return doIncr(map, field, val, Integer.class);
    }

    public static <K> Map<K, Long> incr(Map<K, Long> map, K field, Long val) {
        return doIncr(map, field, val, Long.class);
    }

    public static <K, V> Map<K, V> zip(K[] fields, V[] vals) {
        Map<K, V> result = new LinkedHashMap<K, V>();

        for (int i = 0; i < fields.length; ++i) {
            result.put(fields[i], SuperviseArrays.get(vals, i, null));
        }

        return result;
    }

    public static <K, V> Map<K, V> zip(Iterable<K> fields, Iterable<V> vals) {
        Map<K, V> result = new LinkedHashMap<K, V>();
        Iterator<K> kIter = fields.iterator();
        Iterator vIter = vals.iterator();

        while (kIter.hasNext()) {
            K k = kIter.next();
            V v = vIter.hasNext() ? (V) vIter.next() : null;
            result.put(k, v);
        }

        return result;
    }

    public static <K, V> Map<K, V> zip(List<K> fields, List<V> vals) {
        HashMap<K, V> result = new HashMap<K, V>();

        for (int i = 0; i < fields.size(); ++i) {
            result.put(fields.get(i), IterableUtils.get(vals, i));
        }

        return result;
    }

    public static <K, V> V getSet(Map<K, V> map, K key, V defaultValue) {
        if (map == null) {
            return defaultValue;
        } else {
            V val = map.getOrDefault(key, defaultValue);
            if (map.get(key) == null) {
                map.put(key, val);
            }

            return val;
        }
    }

    public static <T> T get(Map<String, Object> map, T defaultVal, Object... paths) {
        if (paths.length == 0) {
            return defaultVal;
        } else {
            Object firstPart = paths[0];
            paths = ArrayUtils.subarray(paths, 1, paths.length);
            Object data = MapUtils.getObject(map, SuperviseStrings.str(firstPart));
            if (data == null) {
                return defaultVal;
            } else {
                Object[] var5 = paths;
                int var6 = paths.length;

                for (int var7 = 0; var7 < var6; ++var7) {
                    Object path = var5[var7];
                    if (log.isTraceEnabled()) {
                        log.trace("[get([map, defaultVal, paths])] Trying to parse part -> {}", path);
                    }

                    if (data instanceof Map) {
                        data = MapUtils.getObject((Map) data, SuperviseStrings.str(path));
                    } else {
                        if (!(path instanceof Number)) {
                            return defaultVal;
                        }

                        int num = ((Number) path).intValue();
                        if (data.getClass().isArray()) {
                            data = SuperviseArrays.get((Object[]) data, num);
                        } else if (data instanceof Collection) {
                            data = SuperviseCollections.get((Collection) data, ((Number) path).intValue());
                        }
                    }

                    if (data == null) {
                        return defaultVal;
                    }
                }

                try {
                    if (log.isTraceEnabled()) {
                        log.trace("[get([map, defaultVal, paths])] Final result -> {}", data);
                    }

                    return (T) data;
                } catch (ClassCastException var10) {
                    if (log.isDebugEnabled()) {
                        log.debug("[get([map, defaultVal, paths])] Could not parse to type T due to exception");
                    }

                    return defaultVal;
                }
            }
        }
    }

    public static <K, V> List<V> getAll(Map<K, V> map, Collection<K> keys) {
        List<V> result = Lists.newArrayList();
        if (map == null) {
            return result;
        } else {
            Iterator var3 = keys.iterator();

            while (var3.hasNext()) {
                K key = (K) var3.next();
                if (map.containsKey(key)) {
                    result.add(map.get(key));
                }
            }

            return result;
        }
    }

    public static <K, V> Map<K, V> entries(Map<K, V> map, Collection<K> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            return map;
        } else {
            Map<K, V> result = Maps.newHashMap();
            Iterator var3 = keys.iterator();

            while (var3.hasNext()) {
                K k = (K) var3.next();
                if (map.containsKey(k)) {
                    result.put(k, map.get(k));
                }
            }

            return result;
        }
    }

    private static <K, V extends Number> Map<K, V> doIncr(Map<K, V> map, K field, V val, Class<V> type) {
        if (map == null) {
            return map;
        } else {
            map.put(field, SuperviseNumbers.add(type, (V[]) new Number[]{map.getOrDefault(field, SuperviseNumbers.convert(BigDecimal.ZERO, type)), val}));
            return map;
        }
    }
}
