//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.unity.common.util;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
public final class SuperviseNumbers {
    public SuperviseNumbers() {
    }

    public static boolean isNumberType(Class<?> type) {
        return Number.class.isAssignableFrom(type) || Integer.TYPE.isAssignableFrom(type) || Short.TYPE.isAssignableFrom(type) || Long.TYPE.isAssignableFrom(type) || Byte.TYPE.isAssignableFrom(type) || Float.TYPE.isAssignableFrom(type) || Double.TYPE.isAssignableFrom(type);
    }

    public static boolean isIntegerType(Class<?> type) {
        return Number.class.isAssignableFrom(type) || Integer.TYPE.isAssignableFrom(type) || Short.TYPE.isAssignableFrom(type) || Long.TYPE.isAssignableFrom(type) || Byte.TYPE.isAssignableFrom(type);
    }

    public static boolean isDoubleType(Class<?> type) {
        return Number.class.isAssignableFrom(type) || Float.TYPE.isAssignableFrom(type) || Double.TYPE.isAssignableFrom(type);
    }

    public static boolean between(int val, int... ranges) {
        if (ranges != null && ranges.length != 0) {
            if (ranges.length == 1) {
                return val <= ranges[0] && val >= 0;
            } else {
                int max = Arrays.stream(ranges).max().getAsInt();
                int min = Arrays.stream(ranges).min().getAsInt();
                return min <= val && val <= max;
            }
        } else {
            return true;
        }
    }

    public static int fixed(int val, int... ranges) {
        if (ranges.length == 1) {
            return Math.min(val, ranges[0]);
        } else {
            int maxTemp = Arrays.stream(ranges).max().getAsInt();
            int minTemp = Arrays.stream(ranges).min().getAsInt();
            if (val < minTemp) {
                val = minTemp;
            } else if (val > maxTemp) {
                val = maxTemp;
            }

            return val;
        }
    }

    public static double fixed(double val, Double... ranges) {
        Double min = null;
        Double max = null;
        if (ranges.length == 0) {
            return val;
        } else if (ranges.length == 1) {
            return Math.min(val, ranges[0] == null ? 0.0D : ranges[0]);
        } else {
            int var6 = ranges.length;

            for (Double item : ranges) {
                if (item != null) {
                    if (max == null) {
                        max = item;
                    }

                    if (min == null) {
                        min = item;
                    }

                    if (max != null) {
                        max = Math.max(max, item);
                    }

                    if (min != null) {
                        min = Math.min(min, item);
                    }
                }
            }

            if (max != null) {
                val = Math.min(max, val);
            }

            if (min != null) {
                val = Math.max(min, val);
            }

            return val;
        }
    }

    public static String currencyFormat(Number val) {
        return scale(val, 2, 3).toPlainString();
    }

    public static BigDecimal toCurrency(Number val) {
        return scale(val, 4, 3).setScale(2, 2);
    }

    public static BigDecimal scale(Number val, int scale, int roundingMode) {
        return val == null ? BigDecimal.ZERO : (val instanceof BigDecimal ? (BigDecimal)val : new BigDecimal(val.doubleValue())).setScale(scale, roundingMode);
    }

    public static int intValue(Number finalScore, int defaultVal) {
        return finalScore == null ? defaultVal : finalScore.intValue();
    }

    public static double doubleValue(Number finalScore, double defaultVal) {
        return finalScore == null ? defaultVal : finalScore.doubleValue();
    }

    public static long longValue(Number finalScore, long defaultVal) {
        return finalScore == null ? defaultVal : finalScore.longValue();
    }

    public static float floatValue(Number finalScore, float defaultVal) {
        return finalScore == null ? defaultVal : finalScore.floatValue();
    }

    public static long[] longValues(long defaultVal, String... vals) {
        return Arrays.stream(vals).mapToLong((val) -> {
            return NumberUtils.toLong(val, defaultVal);
        }).toArray();
    }

    public static Long toLong(Object val) {
        return toLong(val, (Long)null);
    }

    public static Long toLong(Object val, Long l) {
        if (val == null) {
            return l;
        } else if (val instanceof Number) {
            return ((Number)val).longValue();
        } else {
            BigDecimal decimal = new BigDecimal(SuperviseStrings.str(val));
            return decimal.longValue();
        }
    }

    public static Integer toInteger(Object val) {
        return toInteger(val, (Integer)null);
    }

    public static Integer toInteger(Object val, Integer dval) {
        return val == null ? dval : val instanceof Number ? ((Number)val).intValue() : (new BigDecimal(SuperviseStrings.str(val))).intValue();
    }

    public static Long[] toLongs(Iterable<?> vals) {
        List<Long> ls = Lists.newArrayList();

        for (Object val : vals) {
            Long l = toLong(val, 0L);
            if (l != null) {
                ls.add(l);
            }
        }

        return ls.toArray(new Long[0]);
    }

    public static Integer[] toIntegers(Iterable<?> vals) {
        List<Integer> nums = Lists.newArrayList();

        for (Object val : vals) {
            Integer i = toInteger(val, 0);
            if (i != null) {
                nums.add(i);
            }
        }

        return nums.toArray(new Integer[0]);
    }

    public static Integer[] toIntegers(Object[] vals) {
        List<Integer> nums = Lists.newArrayList();

        for (Object val1 : vals) {
            Object val = toInteger(val1, 0);
            if (val != null) {
                nums.add((Integer) val);
            }
        }

        return (Integer[])nums.toArray(new Integer[0]);
    }

    public static Long[] toLongs(Object[] vals) {
        List<Long> longs = Lists.newArrayList();

        for (Object val1 : vals) {
            Object val = toLong(val1, 0L);
            if (val != null) {
                longs.add((Long) val);
            }
        }

        return longs.toArray(new Long[0]);
    }

    public static Double toDouble(Object val, double defaultVal) {
        Double result = defaultVal;
        if (val == null) {
            return result;
        } else {
            if (val instanceof Number) {
                result = ((Number)val).doubleValue();
            } else if (val instanceof String) {
                result = NumberUtils.toDouble((String)val, defaultVal);
            } else {
                result = NumberUtils.toDouble(val.toString(), defaultVal);
            }

            return result;
        }
    }

    public static int sum(Integer... vals) {
        int result = 0;
        int var3 = vals.length;

        for (Integer val : vals) {
            result += toInteger(val, 0);
        }

        return result;
    }

    public static int multi(Integer... vals) {
        int result = 1;
        int var3 = vals.length;

        for (Integer val : vals) {
            result *= toInteger(val, 1);
        }

        return result;
    }

    public static <V extends Number> V convert(BigDecimal val, Class<V> clazz) {
        if (!clazz.equals(Integer.class) && !clazz.equals(Integer.TYPE)) {
            if (!clazz.equals(Long.class) && !clazz.equals(Long.TYPE)) {
                if (!clazz.equals(Double.class) && !clazz.equals(Double.TYPE)) {
                    if (!clazz.equals(Float.class) && !clazz.equals(Float.TYPE)) {
                        return !clazz.equals(Short.class) && !clazz.equals(Short.TYPE) ? null : (V) (Number)val.shortValue();
                    } else {
                        return  (V) (Number)val.floatValue();
                    }
                } else {
                    return  (V) (Number)val.doubleValue();
                }
            } else {
                return  (V) (Number)val.longValue();
            }
        } else {
            return  (V) (Number)val.intValue();
        }
    }

    public static <V extends Number> V add(Class<V> type, V... vals) {
        BigDecimal sum = BigDecimal.ZERO;
        int var4 = vals.length;

        for (V val1 : vals) {
            V val = (V) val1;
            if (val != null) {
                sum = sum.add(new BigDecimal(val.doubleValue()));
            }
        }

        return convert(sum, type);
    }

    public static boolean hasFlags(int val, int... flags) {
        int var3 = flags.length;

        for (int flag : flags) {
            if ((val & flag) != flag) {
                return false;
            }
        }

        return true;
    }

    public static boolean anyFlags(int val, int... flags) {
        int var3 = flags.length;

        for (int flag : flags) {
            if ((val & flag) == flag) {
                return true;
            }
        }

        return false;
    }

    public static int setFlags(int val, int... flags) {
        int var3 = flags.length;

        for (int flag : flags) {
            val |= flag;
        }

        return val;
    }

    public static int removeFlags(int val, int... flags) {
        int var3 = flags.length;

        for (int flag : flags) {
            val &= ~flag;
        }

        return val;
    }

    public static int nextInt(int fromInclusive, int toInclusive) {
        int result;
        if (fromInclusive < 0) {
            if (toInclusive < 0) {
                result = -RandomUtils.nextInt(Math.abs(toInclusive), Math.abs(fromInclusive) + 1);
            } else {
                result = fromInclusive + RandomUtils.nextInt(0, toInclusive + Math.abs(fromInclusive) + 1);
            }
        } else {
            result = RandomUtils.nextInt(fromInclusive, toInclusive + 1);
        }

        return result;
    }

    public static String generateDeviceTag(String deviceTag, int length, int num, int newLength) {
        if (length > newLength) {
            StringBuilder str = new StringBuilder();
            for (int i = 0; i < length - newLength; i++) {
                str.append("0");
            }
            deviceTag = str + String.valueOf(num);
        }
        return deviceTag;
    }
}
