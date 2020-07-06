//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.unity.common.util;

import com.google.common.collect.BoundType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Range;
import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.*;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unchecked")
public final class GsonUtils {
    private static final Logger LOG = LoggerFactory.getLogger(GsonUtils.class);
    private static final TypeToken<Map<String, Object>> MAP_TYPE = new TypeToken<Map<String, Object>>() {
    };
    private static Map<Class<?>, Field<?>[]> REGISTERED_FIELDS = new HashMap();

    private static final Gson CUSTOMIZED;//定制的
    private static final Gson DEFAULT;

    public GsonUtils() {
    }

    public static final Gson inst() {
        return CUSTOMIZED;
    }

    private static final GsonBuilder createBuilder() {
        return (new GsonBuilder()).setDateFormat("yyyy-MM-dd HH:mm:ss").disableHtmlEscaping().excludeFieldsWithModifiers(new int[]{8, 128});
    }

    public static String format(Object obj) {
        return obj == null ? null : (obj instanceof String ? (String)obj : CUSTOMIZED.toJson(obj));
    }
    public static <T> T parse(String str, Class<T> _class) {
        try {
            return StringUtils.isEmpty(str) ? null : CUSTOMIZED.fromJson(str, _class);
        } catch (Throwable var3) {
            return null;
        }
    }

    public static <T> T parse(String str, TypeToken<T> type) {
        if (StringUtils.isEmpty(str)) {
            return null;
        } else {
            try {
                if (str.startsWith("\"")) {
                    str = str.substring(1, str.length() - 1);
                    str = StringEscapeUtils.unescapeJson(str);
                }

                return StringUtils.isEmpty(str) ? null : CUSTOMIZED.fromJson(str, type.getType());
            } catch (Throwable var3) {
                if (LOG.isInfoEnabled()) {
                    LOG.info("[parse] Could not parse string -> {}", str);
                }

                return null;
            }
        }
    }

    public static Map<String, Object> map(String data) {
        Map<String, Object> map = (Map)parse(data, MAP_TYPE);
        return (Map)(map == null ? Maps.newHashMap() : map);
    }

    public static String[] serializeFieldNames(Class<?> clazz) {
        if (!REGISTERED_FIELDS.containsKey(clazz)) {
            serializeFields(clazz);
            return serializeFieldNames(clazz);
        } else {
            List<String> fieldNames = Lists.newArrayList();
            Field[] var2 = (Field[])REGISTERED_FIELDS.get(clazz);
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                Field field = var2[var4];
                fieldNames.add(field.name);
            }

            return (String[])fieldNames.toArray(new String[0]);
        }
    }

    public static Field<?>[] serializeFields(Class<?> clazz) {
        if (REGISTERED_FIELDS.containsKey(clazz)) {
            return (Field[])REGISTERED_FIELDS.get(clazz);
        } else {
            java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
            List<Field<?>> fs = Lists.newArrayList();
            java.lang.reflect.Field[] var4 = fields;
            int var5 = fields.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                java.lang.reflect.Field field = var4[var6];
                if (field != null && !Modifier.isTransient(field.getModifiers())) {
                    SerializedName serializedName = (SerializedName)field.getAnnotation(SerializedName.class);
                    String name = null;
                    if (serializedName != null) {
                        name = serializedName.value();
                    } else {
                        name = Modifier.isStatic(field.getModifiers()) ? null : field.getName();
                    }

                    if (name != null) {
                        Default defVal = (Default)field.getDeclaredAnnotation(Default.class);
                        fs.add(new Field(name, field.getName(), field.getType(), defVal == null ? null : defVal.value()));
                    }
                }
            }

            Field<?>[] fsArray = (Field[])fs.toArray(new Field[0]);
            REGISTERED_FIELDS.put(clazz, fsArray);
            return fsArray;
        }
    }

    public static <T> T parse(String val, Type type) {
        return StringUtils.isEmpty(val) ? null : CUSTOMIZED.fromJson(val, type);
    }

    static {
        CUSTOMIZED = createBuilder().registerTypeAdapter(Boolean.class, new BooleanDeserializer()).registerTypeAdapter((new TypeToken<Map<String, Object>>() {
        }).getType(), new MapDeserializerDoubleAsIntFix()).registerTypeAdapter(Boolean.TYPE, new BooleanDeserializer()).registerTypeAdapter(Date.class, new DateDeserializer()).registerTypeAdapter(Range.class, new RangeCodec()).registerTypeAdapter(Type.class, new TypeCodec()).registerTypeAdapter(Class.class, new TypeCodec()).registerTypeAdapter(String.class, new StringDeserializer()).registerTypeAdapter(BigDecimal.class, new BigDecimalSerializer()).registerTypeAdapter(Double.class, new DoubleTypeAdapter()).registerTypeAdapter(Double.TYPE, new DoubleTypeAdapter()).registerTypeAdapter(Long.class, new LongTypeAdapter()).registerTypeAdapter(Long.TYPE, new LongTypeAdapter()).setExclusionStrategies(new ExclusionStrategy[]{new AnnotationExclusionStrategy()}).serializeSpecialFloatingPointValues().create();
        DEFAULT = createBuilder().create();
    }

    private static class TypeCodec implements JsonSerializer<Type>, JsonDeserializer<Type> {
        private TypeCodec() {
        }

        public Type deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            String typeName = jsonElement.getAsString();

            try {
                return Class.forName(typeName);
            } catch (ClassNotFoundException var6) {
                GsonUtils.LOG.warn("[deserialize([jsonElement, type, jsonDeserializationContext])] No class found to type -> {}", typeName);
                return null;
            }
        }

        public JsonElement serialize(Type type, Type type2, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(type.getTypeName());
        }
    }

    private static class AnnotationExclusionStrategy implements ExclusionStrategy {
        private AnnotationExclusionStrategy() {
        }

        public boolean shouldSkipField(FieldAttributes f) {
            return false;
        }

        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }

    private static class MapDeserializerDoubleAsIntFix implements JsonDeserializer<Map<String, Object>> {
        private MapDeserializerDoubleAsIntFix() {
        }

        public Map<String, Object> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return (Map)this.read(json);
        }

        public Object read(JsonElement in) {
            if (in.isJsonArray()) {
                List<Object> list = new ArrayList();
                JsonArray arr = in.getAsJsonArray();
                Iterator var11 = arr.iterator();

                while(var11.hasNext()) {
                    JsonElement anArr = (JsonElement)var11.next();
                    list.add(this.read(anArr));
                }

                return list;
            } else if (!in.isJsonObject()) {
                if (in.isJsonPrimitive()) {
                    JsonPrimitive prim = in.getAsJsonPrimitive();
                    if (prim.isBoolean()) {
                        return prim.getAsBoolean();
                    }

                    if (prim.isString()) {
                        return prim.getAsString();
                    }

                    if (prim.isNumber()) {
                        Number num = prim.getAsNumber();
                        if (Math.ceil(num.doubleValue()) == (double)num.longValue()) {
                            return num.longValue();
                        }

                        return num.doubleValue();
                    }
                }

                return null;
            } else {
                Map<String, Object> map = new LinkedTreeMap();
                JsonObject obj = in.getAsJsonObject();
                Set<Entry<String, JsonElement>> entitySet = obj.entrySet();
                Iterator var5 = entitySet.iterator();

                while(var5.hasNext()) {
                    Entry<String, JsonElement> entry = (Entry)var5.next();
                    map.put(entry.getKey(), this.read((JsonElement)entry.getValue()));
                }

                return map;
            }
        }
    }

    private static class BigDecimalSerializer implements JsonSerializer<BigDecimal> {
        private BigDecimalSerializer() {
        }

        public JsonElement serialize(BigDecimal src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.setScale(2, 3).toString());
        }
    }

    private static class RangeCodec implements JsonDeserializer<Range> {
        private RangeCodec() {
        }

        public Range deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String value = null;
            if (json.isJsonArray()) {
                JsonArray array = json.getAsJsonArray();
                StringBuilder sb = new StringBuilder();
                sb.append('[').append(array.get(0));
                if (array.size() > 1) {
                    sb.append(',').append(array.get(1));
                }

                sb.append(']');
                value = sb.toString();
            } else {
                value = json.getAsString();
            }

            boolean leftOpen = value.startsWith("(");
            boolean rightOpen = value.startsWith(")");
            Pattern pattern = Pattern.compile("(?:\\[|\\()([\\d.]+)[,:]([\\d.]+)(?:\\]|\\))");
            Matcher m = pattern.matcher(value);
            if (!m.find()) {
                return null;
            } else if (m.groupCount() == 0) {
                GsonUtils.LOG.warn("[RangeDeserializer.deserialize] Illegal format for range -> {}", value);
                return null;
            } else {
                Comparable start = NumberUtils.toDouble(m.group(1), 0.0D);
                Comparable end = m.groupCount() > 1 ? NumberUtils.toDouble(m.group(2), 0.0D) : start;
                return Range.range(start, leftOpen ? BoundType.OPEN : BoundType.CLOSED, end, rightOpen ? BoundType.OPEN : BoundType.CLOSED);
            }
        }
    }

    private static class LongTypeAdapter implements JsonSerializer<Long>, JsonDeserializer<Long> {
        private LongTypeAdapter() {
        }

        public JsonElement serialize(Long src, Type typeOfSrc, JsonSerializationContext context) {
            return src == null ? null : new JsonPrimitive(src);
        }

        public Long deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return json.isJsonPrimitive() ? json.getAsLong() : 0L;
        }
    }

    private static class DoubleTypeAdapter implements JsonSerializer<Double>, JsonDeserializer<Double> {
        private DoubleTypeAdapter() {
        }

        public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
            return src == null ? null : new JsonPrimitive(src);
        }

        public Double deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return json.isJsonPrimitive() ? json.getAsDouble() : 0.0D;
        }
    }

    private static class StringDeserializer implements JsonDeserializer<String> {
        private StringDeserializer() {
        }

        public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return json.isJsonPrimitive() ? json.getAsJsonPrimitive().getAsString() : json.toString();
        }
    }

    private static class DateDeserializer implements JsonDeserializer<Date>, JsonSerializer<Date> {
        private static final String EMPTY_ZERO_DATE = "0000-00-00";
        private static final String[] COMMON_DATE_PATTERNS = new String[]{"yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy/MM/dd HH:mm", "yyyy-MM-dd", "yyyy/MM/dd", "HH:mm:ss", "HH:mm"};

        private DateDeserializer() {
        }

        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String value = json.getAsJsonPrimitive().getAsString();
            if (StringUtils.isNumeric(value)) {
                long ts = Long.parseLong(value);
                if (ts > 0L) {
                    return new Date(value.length() == 10 ? ts * 1000L : ts);
                }
            }

            if (value != null && value.startsWith("0000-00-00")) {
                return null;
            } else {
                try {
                    return DateUtils.parseDate(value, COMMON_DATE_PATTERNS);
                } catch (ParseException var7) {
                    if (GsonUtils.LOG.isInfoEnabled()) {
                        GsonUtils.LOG.info("[DateDeserializer.deserialize] Deserialize date string [{}] failed with exception", value, var7);
                    }

                    return null;
                }
            }
        }

        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(DateFormatUtils.format(src, COMMON_DATE_PATTERNS[0]));
        }
    }

    private static class BooleanDeserializer implements JsonDeserializer<Boolean> {
        private BooleanDeserializer() {
        }

        public Boolean deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String value = json.getAsString();
            Boolean result = Boolean.valueOf(value);
            if (StringUtils.isNumeric(value)) {
                result = Integer.parseInt(value) != 0;
            }

            return result;
        }
    }

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    public @interface Default {
        String value();
    }

    public static class Field<T> {
        public String name;
        public String originName;
        public Class<T> type;
        public String defaultValue;

        public Field(String name, String originName, Class<T> type, String defaultValue) {
            this.name = name;
            this.originName = originName;
            this.type = type;
            this.defaultValue = defaultValue;
        }

        public boolean equals(Object obj) {
            return obj != null && Field.class.isInstance(obj) && this.name != null && this.name.equals(((Field)obj).name) || this.originName != null && this.name.equals(((Field)obj).originName);
        }

        public String toString() {
            return StringUtils.wrap(this.name, '`');
        }
    }
}
