package com.unity.common.base;

import lombok.Data;

import javax.persistence.Id;
import java.lang.reflect.Field;

@Data
public abstract class MongoEntity implements  IBaseEntity  {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private Long gmtCreate;

    private Long gmtModified;

    @Override
    public Object get(String FieldName) {
        return get(this, this.getClass(), FieldName);
    }

    private Object get(Object obj, Class<?> c, String FieldName) {

        try {
            Field f = c.getDeclaredField(FieldName);
            f.setAccessible(true);
            return f.get(obj);
        } catch (NoSuchFieldException e) {
            if (c.getSuperclass() == null)
                return null;
            else
                return get(obj, c.getSuperclass(), FieldName);
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public void set(String FieldName, Object val) {
        set(this, this.getClass(), FieldName, val);
    }

    private void set(Object obj, Class<?> c, String FieldName, Object val) {

        try {
            Field f = c.getDeclaredField(FieldName);
            f.setAccessible(true);
            f.set(obj, val);
        } catch (NoSuchFieldException e) {
            if (c.getSuperclass() != null)
                set(obj, c.getSuperclass(), FieldName, val);
        } catch (Exception ex) {
            // set(obj, c, FieldName,null);
        }
    }
}
