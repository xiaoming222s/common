package com.unity.common.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Stream;

public final class JKArrays {
    public JKArrays() {
    }

    public static void shuffle(int[] array) {
        Random rnd = ThreadLocalRandom.current();

        for (int i = array.length - 1; i > 0; --i) {
            int index = rnd.nextInt(i + 1);
            int a = array[index];
            array[index] = array[i];
            array[i] = a;
        }

    }

    public static <T> String[] strs(T[] array) {
        return transfer(array, String::valueOf, String[]::new);
    }

    public static <T> Integer[] ints(T[] array) {
        return transfer(array, (v) -> {
            if (v == null) {
                return 0;
            } else {
                return v instanceof Number ? ((Number) v).intValue() : NumberUtils.toInt(JKStrings.str(v), 0);
            }
        }, Integer[]::new);
    }

    public static <T> Long[] longs(T[] array) {
        return transfer(array, (v) -> {
            if (v == null) {
                return 0L;
            } else {
                return v instanceof Number ? ((Number) v).longValue() : NumberUtils.toLong(JKStrings.str(v), 0L);
            }
        }, Long[]::new);
    }

    public static <T, V> V[] transfer(T[] array, Function<T, V> func, IntFunction<V[]> generator) {
        return Arrays.stream(array).map(func).toArray(generator);
    }

    public static void shuffle(byte[] array) {
        Random rnd = ThreadLocalRandom.current();

        for (int i = array.length - 1; i > 0; --i) {
            int index = rnd.nextInt(i + 1);
            byte a = array[index];
            array[index] = array[i];
            array[i] = a;
        }

    }

    public static <T> void shuffle(T[] array) {
        Random rnd = ThreadLocalRandom.current();

        for (int i = array.length - 1; i > 0; --i) {
            int index = rnd.nextInt(i + 1);
            T a = array[index];
            array[index] = array[i];
            array[i] = a;
        }

    }

    @SuppressWarnings("unchecked")
    public static <T> T[] notEmpty(T[] array) {
        if (ArrayUtils.isEmpty(array)) {
            return array;
        } else {
            List<T> items = new ArrayList<>();

            for (T anArray : array) {
                if (anArray != null) {
                    items.add(anArray);
                }
            }

            return items.toArray((T[]) Array.newInstance(array.getClass().getComponentType(), items.size()));
        }
    }

    public static byte get(byte[] data, int index) {
        return data != null && data.length > index ? data[index] : -1;
    }

    public static <T> T get(T[] fields, int i) {
        return get(fields, i, null);
    }

    public static <T> T get(T[] fields, int i, T defaultVal) {
        if (ArrayUtils.isEmpty(fields)) {
            return defaultVal;
        } else {
            if (i < 0) {
                i = Math.abs(fields.length + i);
            }

            return i >= fields.length ? defaultVal : fields[i];
        }
    }

    public static boolean isEmpty(Object[] vals) {
        if (ArrayUtils.isEmpty(vals)) {
            return true;
        } else {
            boolean result = true;
            int var3 = vals.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                Object val = vals[var4];
                if (val instanceof Collection) {
                    result &= CollectionUtils.isEmpty((Collection) val);
                } else {
                    result &= val == null;
                }
            }

            return result;
        }
    }

    public static Long getLong(Object[] result, int i, Long l) {
        return NumberUtils.toLong(JKStrings.str(get(result, i)), l);
    }

    public static String getString(Object[] result, int i) {
        return JKStrings.str(get(result, i));
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> extract(Object... kvs) {
        Map<K, V> result = Maps.newHashMap();

        for (int i = 0; i < kvs.length; ++i) {
            result.put((K) kvs[i], i + 1 >= kvs.length ? null : (V) kvs[i + 1]);
        }

        return result;
    }

    public static <T> List<T> asList(T[] objs) {
        List<T> list = Lists.newArrayList();
        int var3 = objs.length;

        list.addAll(Arrays.asList(objs).subList(0, var3));

        return list;
    }

    public static long[] flatten(long[][] vals) {
        int length = 0;

        for (long[] val : vals) {
            length += val.length;
        }

        long[] result = new long[length];
        int index = 0;

        for (long[] vs : vals) {
            System.arraycopy(vs, 0, result, index, vs.length);
            index += vs.length;
        }

        return result;
    }

    public static int[] generate(int from, int to) {
        int[] result = new int[to - from + 1];

        for (int i = from; i <= to; result[i - from] = i++) {

        }

        return result;
    }

    public static long[] generate(long from, long to) {
        long[] result = new long[(int) (to - from + 1L)];

        for (long i = from; i <= to; result[(int) (i - from)] = i++) {

        }

        return result;
    }

    public static long[][] split(long[] vals, int columns, int rows) {
        long[][] result = new long[rows][columns];
        int index = 0;

        for (int i = 0; i < rows; ++i) {
            int rowLength = Math.min(vals.length, index + columns);
            result[i] = new long[columns];
            System.arraycopy(result[i], 0, ArrayUtils.subarray(vals, index, rowLength), 0, rowLength - index);
            index += columns;
        }

        return result;
    }

    public static Object[] expand(Object[] vals) {
        if (vals == null) {
            return null;
        } else {
            int length = vals.length;
            Object[] newVals = vals;
            int index = vals.length;

            int i;
            Object val;
            for (i = 0; i < index; ++i) {
                val = newVals[i];
                if (val != null) {
                    if (val instanceof Collection) {
                        length += CollectionUtils.size(val) - 1;
                    } else if (val.getClass().isArray()) {
                        length += ArrayUtils.getLength(val) - 1;
                    }
                }
            }

            newVals = new Object[length];
            index = 0;

            for (i = 0; i < vals.length; ++i) {
                val = vals[i];
                Object[] tmp;
                int j;
                if (val instanceof Collection) {
                    tmp = ((Collection) val).toArray();

                    for (j = 0; j < tmp.length; ++j) {
                        newVals[index++] = tmp[j];
                    }
                } else if (val.getClass().isArray()) {
                    tmp = (Object[]) val;

                    for (j = 0; j < tmp.length; ++j) {
                        newVals[index++] = tmp[j];
                    }
                } else {
                    newVals[index++] = val;
                }
            }

            return newVals;
        }
    }

    /**
     * 初始化数组
     *
     * @param count 需要初始化的数量
     * @return 得到的init数据
     * @author Jung
     * @since 2018-03-07 11:44:32
     */
    public static int[] initArr(int count) {
        return new int[count];
    }

    /**
     * 初始化数组并赋值0
     *
     * @param count 需要赋值的数量
     * @return 赋值之后的数组
     * @author Jung
     * @since 2018年04月18日13:42:53
     */
    public static BigDecimal[] bigDecimalsWithInitialZero(int count) {
        BigDecimal[] bigArray = new BigDecimal[count];
        Stream.iterate(0, i -> i + 1)
                .limit(count)
                .forEach(i -> bigArray[i] = BigDecimal.ZERO);
        return bigArray;
    }

    /**
     * 初始化AtomicLong数组并赋值0
     *
     * @param count 需要赋值的数量
     * @return 赋值之后的数组
     * @author Jung
     * @since 2018年04月18日13:42:53
     */
    public static AtomicLongArray atomicLongsWithInitialZero(int count) {
        AtomicLongArray array = new AtomicLongArray(count);
        Stream.iterate(0, i -> i + 1)
                .limit(count)
                .forEach(i -> array.set(i,0L));
        return array;
    }


    /**
     * 设置数组
     *
     * @param placeholder 需要改变的index
     * @param number      需要改变的value
     * @author Jung
     * @since 2018-03-07 11:44:32
     */
    public static void setArray(int[] arr, Object placeholder, Object number) {
        int placeHolder2int = Integer.parseInt(String.valueOf(placeholder).trim());
        int number2int = ((Number) number).intValue();
        arr[placeHolder2int] = number2int;
    }

    public static String[] initStringArr(int count) {
        return new String[count];
    }

    public static void setString(String[] str, Object index, Object value) {
        int lastIndex = Integer.parseInt(String.valueOf(index).trim());
        String lastvalue = String.valueOf(value);
        str[lastIndex] = lastvalue;
    }


}
