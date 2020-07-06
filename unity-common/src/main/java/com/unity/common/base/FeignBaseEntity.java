package com.unity.common.base;

import lombok.Data;

import java.lang.reflect.Field;
import java.util.Date;

@Data
public class FeignBaseEntity implements  IBaseEntity  {
        private static final long serialVersionUID = 1L;


        private Long id;


        private String gmtCreate;


        private String gmtModified;


        private Integer isDeleted;


        private Long sort;


        private String notes;


        private String creator;


        private String editor;

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
