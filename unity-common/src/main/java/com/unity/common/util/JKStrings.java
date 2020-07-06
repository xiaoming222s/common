//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.unity.common.util;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public final class JKStrings {
    private static final Logger LOG = LoggerFactory.getLogger(JKStrings.class);
    public static final String HYPHEN = "-";
    public static final String UNDER_SCORE = "_";
    public static final String SLASH = "/";
    public static final String BACKSLASH = "\\";
    public static final String COMMA = ",";
    public static final String SEMICOLON = ";";
    public static final String EQ = "=";
    public static final String COLON = ":";
    public static final String AMP = "&";
    public static final String ASTERISK = "*";
    public static final String LBRACE = "(";
    public static final String RBRACE = ")";
    public static final String NULL = "null";
    public static final String QUESTION_MARK = "?";
    public static final String FALSE = "false";
    public static final String EMPTY_JSON = "{}";
    public static final String QUOTE = "'";
    public static final String QUOTE_UNICODE = "\\u0027";
    public static final String DOUBLE_QUOTE = "\"";
    public static final String DOT = ".";

    //转换字符串用
    public static final String SERVER_PLACEHOLDER = "SERVER";
    public static final String REASON_PLACEHOLDER = "REASON";
    public static final String ERROR_HANDLER_TEMPLATE = "[Message Dispatcher: ${" + SERVER_PLACEHOLDER + "}]${" + REASON_PLACEHOLDER + "}";
    public static final String PLACEHOLDER_PATTERN = "\\$\\{%s\\}";

    /**
     * 根据键值对填充字符串，如("hello ${name}",{name:"xiaoming"})
     *
     * @param content 需要转换的内容
     * @param map     需要设置的参数
     * @return 转换完的参数
     * @author Jung
     * @since 2017年12月15日14:31:00
     */
    public static String renderString(final String content, Map<String, Object> map) {
        String after = content;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            after = after.replaceAll(String.format(PLACEHOLDER_PATTERN, entry.getKey()), str(entry.getValue()));
        }
        return after;
    }

    public JKStrings() {
    }

    public static String str(Object obj) {
        return str(obj, (String) null);
    }

    public static String str(Object obj, String defaultVal) {
        if (obj == null) {
            return defaultVal;
        } else {
            String result = null;
            if (obj instanceof String) {
                result = (String) obj;
            } else if (obj instanceof Number) {
                result = obj.toString();
            } else if (obj instanceof Date) {
                result = JKDates.format((Date) obj);
            } else if (obj.getClass().isEnum()) {
                result = obj.toString();
            } else {
                result = GsonUtils.format(obj);
            }

            return result;
        }
    }

    public static String joinWith(Object... objs) {
        if (ArrayUtils.isEmpty(objs)) {
            return "";
        } else if (objs.length == 1) {
            return str(objs[0]);
        } else {
            String seperator = str(objs[objs.length - 1]);
            objs = ArrayUtils.remove(objs, objs.length - 1);
            List<String> items = (List<String>) Arrays.stream(objs).filter((item) -> {
                return item != null;
            }).map((item) -> {
                if (item.getClass().isArray()) {
                    Object[] newObjs = Arrays.copyOf((Object[]) ((Object[]) item), ArrayUtils.getLength(item) + 1);
                    newObjs[newObjs.length - 1] = seperator;
                    return joinWith(newObjs);
                } else {
                    return str(item);
                }
            }).filter(StringUtils::isNotEmpty).collect(Collectors.toList());
            return join(items, seperator);
        }
    }

    public static String join(List<?> list, char separator) {
        return join(list, String.valueOf(separator));
    }

    public static String join(List<?> list, String separator) {
        if (separator == null) {
            throw new IllegalArgumentException("Missing separator!");
        } else {
            String output = "";
            if (list != null && list.size() > 0) {
                for (int i = 1; i <= list.size(); ++i) {
                    output = output + list.get(i - 1);
                    if (i < list.size()) {
                        output = output + separator;
                    }
                }
            }

            return output;
        }
    }

    public static String join(Set<?> list, String separator) {
        if (separator != null && !separator.toString().equalsIgnoreCase("")) {
            String output = "";
            if (list != null && list.size() > 0) {
                boolean joined = false;

                for (Iterator var4 = list.iterator(); var4.hasNext(); joined = true) {
                    Object item = var4.next();
                    output = output + str(item);
                    output = output + separator;
                }

                if (joined) {
                    output = output.substring(0, output.length() - 1);
                }
            }

            return output;
        } else {
            throw new IllegalArgumentException("Missing separator!");
        }
    }

    public static byte[] utf8(String method) {
        try {
            return method == null ? null : method.getBytes(StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException var2) {
            LOG.warn("[utf8] Could not parse to utf8 due to exception", var2);
            return null;
        }
    }

    public static byte[] utf16(String method) {
        try {
            return method == null ? null : method.getBytes(StandardCharsets.UTF_16.name());
        } catch (UnsupportedEncodingException var2) {
            LOG.warn("[utf16] Could not parse to utf8 due to exception", var2);
            return null;
        }
    }

    public static String utf8(byte[] data) {
        return (new String(data, StandardCharsets.UTF_8)).replaceAll("\\u0027", "'");
    }

    public static String utf16(byte[] data) {
        return new String(data, StandardCharsets.UTF_16);
    }

    public static String[] trim(String[] strs) {
        return ArrayUtils.isEmpty(strs) ? strs : (String[]) Arrays.stream(strs).map((item) -> {
            return StringUtils.trim(item);
        }).toArray((x$0) -> {
            return new String[x$0];
        });
    }

    public static String[] split(String str, String separator) {
        return StringUtils.isNotEmpty(str) ? str.split(separator) : null;
    }

    public static String snake(String str) {
        return snake(str, false);
    }

    public static String snake(String str, boolean upper) {
        return CaseFormat.UPPER_CAMEL.to(upper ? CaseFormat.UPPER_UNDERSCORE : CaseFormat.LOWER_UNDERSCORE, str);
    }

    public static String camel(String str) {
        return camel(str, false);
    }

    public static String camel(String str, boolean upperCapital) {
        return CaseFormat.LOWER_UNDERSCORE.to(upperCapital ? CaseFormat.UPPER_CAMEL : CaseFormat.LOWER_CAMEL, str);
    }

    public static String[] strs(Iterable<?> vals) {
        List<String> strs = Lists.newArrayList();
        Iterator var2 = vals.iterator();

        while (var2.hasNext()) {
            Object val = var2.next();
            String s = str(val, (String) null);
            if (s != null) {
                strs.add(s);
            }
        }

        return (String[]) strs.toArray(new String[0]);
    }

    public static String[] strs(Object[] vals) {
        List<String> strs = Lists.newArrayList();

        for (int i = 0; i < vals.length; ++i) {
            if (vals[i] != null) {
                Object val = str(vals[i], (String) null);
                strs.add((String) val);
            }
        }

        return (String[]) strs.toArray(new String[0]);
    }

    public static List<String> wrapAll(Collection<String> strs, String wrap) {
        if (!CollectionUtils.isEmpty(strs) && !StringUtils.isEmpty(wrap)) {
            return (List<String>) strs.stream().map((str) -> {
                return StringUtils.wrap(str, wrap);
            }).collect(Collectors.toList());
        } else {
            return strs == null ? null : Lists.newArrayList(strs);
        }
    }

    public static List<String> wrapAll(Collection<String> strs, char wrap) {
        return wrapAll(strs, String.valueOf(wrap));
    }

    public static boolean isEmptyJson(String json) {
        return StringUtils.isEmpty(json) || "{}".equals(json);
    }

    public static boolean isEmpty(String json) {
        return StringUtils.isEmpty(json);
    }

    public static byte[] bytes(Object key) {
        String str = str(key);
        return StringUtils.isBlank(str) ? null : str.getBytes();
    }

    public static byte[][] byteses(Charset charset, Object... keys) {
        if (ArrayUtils.isEmpty(keys)) {
            return (byte[][]) null;
        } else {
            byte[][] bytes = new byte[keys.length][];

            for (int i = 0; i < keys.length; ++i) {
                bytes[i] = bytes(keys[i], charset);
            }

            return bytes;
        }
    }

    public static byte[] bytes(Object key, String charset) {
        if (key == null) {
            return null;
        } else {
            try {
                String str = str(key);
                return StringUtils.isEmpty(charset) ? str.getBytes() : str.getBytes(charset);
            } catch (UnsupportedEncodingException var3) {
                LOG.warn("[bytes] Unsupported charset -> {}", charset);
                return null;
            }
        }
    }

    public static byte[] bytes(Object key, Charset charset) {
        if (key == null) {
            return null;
        } else {
            String str = str(key);
            return charset == null ? str.getBytes() : str.getBytes(charset);
        }
    }

    public static String join(List<String> strings) {
        return join(strings, "");
    }
}
