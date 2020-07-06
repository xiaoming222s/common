
package com.unity.common.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.unity.common.base.IBaseEntity;
import com.unity.common.exception.UnityRuntimeException;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JsonUtil {
//    public interface afterSet {
//        void after(Map<String, Object> row, IBaseEntity baseModel);
//    }

    @FunctionalInterface
    public interface AfterSet<T extends IBaseEntity> extends Serializable {
        void after(Map<String, Object> row, T baseModel);
    }

    @FunctionalInterface
    public interface FieldSet<T extends IBaseEntity> extends Serializable {
        List<SFunction<T, ?>> fields();
    }


    public static <T extends IBaseEntity> Map<String, Object> ObjectToMap(IPage<T> page, FieldSet<T> fieldSet, AfterSet<T> set) {
        return ObjectToMap(page,set,fieldSet.fields().toArray(new SFunction[fieldSet.fields().size()]));
//        Map<String, Object> result = Maps.newHashMap();
//        if(set!=null) set.after(result,page.getRecords().get(0));
//        return result;
    }

    public static <T extends IBaseEntity> Map<String, Object> ObjectToMap(IPage<T> page, AfterSet<T> set, SFunction<T, ?>... columns) {
        try {
            List<Map<String, Object>> ll = new ArrayList<Map<String, Object>>();
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("total", page.getTotal());
            result.put("items", ll);
            for (T o : page.getRecords()) {
                Map<String, Object> row = new HashMap<String, Object>();

                for(SFunction<T, ?> col:columns){
                    String k = FieldConvert.getToField(col);
                    row.put(k, col.apply(o));
                }
                if (set != null) set.after(row, o);

                if (!row.isEmpty()) ll.add(row);

            }
            return result;
        }
        catch (Exception ex){
            ex.printStackTrace();
            throw new UnityRuntimeException(ex);
        }

    }


    /**
     * 将分页的Entry，按对应的key[],进行转换成Map对象
     *
     * @param page
     *            Entry
     * @param keys
     *            指定key
     * @param set
     *            自定义接口
     * @return
     */
    public static <T extends IBaseEntity> Map<String, Object> ObjectToMap(IPage<T> page, String[] keys, AfterSet<T> set) {
        try {
            List<Map<String, Object>> ll = new ArrayList<Map<String, Object>>();
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("total", page.getTotal());
            result.put("items", ll);
            for (T o : page.getRecords()) {
                Class<?> c = o.getClass();
                Map<String, Object> row = new HashMap<String, Object>();

                for (String k : keys) {
                    try {
                        row.put(k, getFieldValue(o, c, k));
                    } catch (Exception e) {
                        row.put(k, "");
                    }
                }
                if (set != null) set.after(row, o);

                if (!row.isEmpty()) ll.add(row);

            }
            return result;
        }
        catch (Exception ex){
            ex.printStackTrace();
            throw new UnityRuntimeException(ex);
        }
    }

    public static <T extends IBaseEntity> List<Map<String, Object>> ObjectToList(List<T> listData, FieldSet<T> fieldSet, AfterSet<T> set){
        return ObjectToList(listData,set,fieldSet.fields().toArray(new SFunction[fieldSet.fields().size()]));
    }

    public static <T extends IBaseEntity> List<Map<String, Object>> ObjectToList(List<T> listData, AfterSet<T> set, SFunction<T, ?>... columns){
        try {
            List<Map<String, Object>> ll = new ArrayList<Map<String, Object>>();
            for (T o : listData) {
                Map<String, Object> row = new HashMap<String, Object>();
                ll.add(row);
                for(SFunction<T, ?> col:columns){
                    String k = FieldConvert.getToField(col);
                    row.put(k, col.apply(o));
                }
                if (set != null)
                    set.after(row, o);
            }
            return ll;
        }
        catch (Exception ex){
            ex.printStackTrace();
            throw new UnityRuntimeException(ex);
        }

    }

    /**
     * 将List<T>的Entry，按对应的key[],进行转换成Lists对象
     *
     * @param listData
     *            List<T>
     * @param keys
     *            指定key
     * @param set
     *            自定义接口
     * @return
     */
    public static <T extends IBaseEntity> List<Map<String, Object>> ObjectToList(List<T> listData, String[] keys, AfterSet<T> set){
        try {
            List<Map<String, Object>> ll = new ArrayList<Map<String, Object>>();
//        if(listData==null || listData.size()==0) return ll;
            for (T o : listData) {
                Class<?> c = o.getClass();
                Map<String, Object> row = new HashMap<String, Object>();
                ll.add(row);
                for (String k : keys) {
                    try {
                        row.put(k, getFieldValue(o, c, k));
                    } catch (Exception e) {
                        row.put(k, "");
                    }
                }
                if (set != null)
                    set.after(row, o);
            }
            return ll;
        }
        catch (Exception ex){
            ex.printStackTrace();
            throw new UnityRuntimeException(ex);
        }

    }




    public static <T extends IBaseEntity> Map<String, Object> ObjectToGroup(List<T> listData, String[] groups, String[] keys, AfterSet<T> set) {
        Map<String, Object> result = new HashMap<String, Object>();

        try {

            //最底层row
//      if(listData==null) return ll;
            for (T o : listData) {
                Class<?> c = o.getClass();

                Map<String, Object> dataRow = result;
                List<Object> row = null;
                for (int i = 0; i < groups.length; i++) {
                    String val = getFieldValue(o, c, groups[i]).toString();

                    //最后一行为数组
                    if (groups.length == i + 1) {
                        if (!dataRow.containsKey(val)) {
                            row = new ArrayList<Object>();
                            dataRow.put(val, row);
                        } else {
                            row = (List<Object>) dataRow.get(val);
                        }
                    } else {
                        Map<String, Object> g = null;
                        if (!dataRow.containsKey(val)) {
                            g = new HashMap<String, Object>();
                            dataRow.put(val, g);
                            dataRow = g;
                        } else {
                            dataRow = (Map<String, Object>) dataRow.get(val);
                        }
                    }
                }

                //写入行
                Map<String, Object> tata = new HashMap<String, Object>();
                row.add(tata);
                for (String k : keys) {
                    try {
                        tata.put(k, getFieldValue(o, c, k));
                    } catch (Exception e) {
                        tata.put(k, "");
                    }
                }
                if (set != null)
                    set.after(dataRow, o);
            }
            return result;
        }
        catch (Exception ex){
            ex.printStackTrace();
            throw new UnityRuntimeException(ex);
        }

    }

    public static <T extends IBaseEntity> Map<String, Object> ObjectToMap(T entry, FieldSet<T> fieldSet, AfterSet<T> set){
        return ObjectToMap(entry,set,fieldSet.fields().toArray(new SFunction[fieldSet.fields().size()]));
    }
    public static <T extends IBaseEntity> Map<String, Object> ObjectToMap(T entry, AfterSet<T> set, SFunction<T, ?>... columns){
        try {

            Map<String, Object> result = new HashMap<String, Object>();
            if (entry == null) return result;
            for(SFunction<T, ?> col:columns){
                String k = FieldConvert.getToField(col);
                result.put(k, col.apply(entry));
            }
            if (set != null)
                set.after(result, entry);
            return result;
        }
        catch (Exception ex){
            ex.printStackTrace();
            throw new UnityRuntimeException(ex);
        }

    }

    /**
     * 将 T 的Entry，按对应的key[],进行转换成Map对象
     *
     * @param entry
     *            Entry
     * @param keys
     *            指定key
     * @param set
     *            自定义接口
     * @return
     */
    public static <T extends IBaseEntity> Map<String, Object> ObjectToMap(T entry, String[] keys, AfterSet<T> set){
        try {

            Map<String, Object> result = new HashMap<String, Object>();
            if (entry == null) return result;
            Class<?> c = entry.getClass();
            for (String k : keys) {
                try {
                    result.put(k, getFieldValue(entry, c, k));
                } catch (Exception e) {
                    result.put(k, "");
                }
            }
            if (set != null)
                set.after(result, entry);
            return result;
        }
        catch (Exception ex){
            ex.printStackTrace();
            throw new UnityRuntimeException(ex);
        }

    }

    public static <T> Map<String, Object> ObjectToMap(T obj){
        try {

            if(obj == null)
                return null;

            Map<String, Object> result = new HashMap<String, Object>();

            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (key.compareToIgnoreCase("class") == 0) {
                    continue;
                }
                Method getter = property.getReadMethod();
                Object value = getter!=null ? getter.invoke(obj) : null;
                result.put(key, value);
            }

            return result;
        }
        catch (Exception ex){
            ex.printStackTrace();
            throw new UnityRuntimeException(ex);
        }

    }

    /**
     * 获取Field的Val
     *
     * @param obj
     *            Object对象
     * @param c
     *            Class对象
     * @param FieldName
     *            Field的名字
     * @return
     */
    private static <T extends IBaseEntity> Object getFieldValue(T obj, Class<?> c, String FieldName) throws Exception {

        try {
            Field f = c.getDeclaredField(FieldName);
            f.setAccessible(true);
            return f.get(obj);
        } catch (NoSuchFieldException e) {
            if (c.getSuperclass() == null)
                return null;

            return getFieldValue(obj, c.getSuperclass(), FieldName);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage(), ex);
        }
    }
}
