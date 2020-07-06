package com.unity.common.base;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.unity.common.exception.UnityRuntimeException;
import com.unity.common.pojos.SystemResponse;
import com.unity.common.util.FieldConvert;
import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Field;


@Data
public abstract class BaseEntity implements  IBaseEntity  {
	private static final long serialVersionUID = 1L;

	@CommentTarget("id主键")
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	@CommentTarget("创建时间")
	@TableField(value = "gmt_create",fill = FieldFill.INSERT)
	private Long gmtCreate;

	@CommentTarget("修改时间")
	@TableField(value = "gmt_modified", fill = FieldFill.INSERT_UPDATE)
	private Long gmtModified;

	@TableLogic
	@TableField(value = "is_deleted")
	private Integer isDeleted;

	@CommentTarget("排序号")
	@TableField(value = "i_sort",fill = FieldFill.INSERT)
	private Long sort;

	@CommentTarget("备注")
	@TableField(value = "notes")
	private String notes;

	@CommentTarget("创建人")
	@TableField(value = "creator",fill = FieldFill.INSERT)
	private String creator;

	@CommentTarget("修改人")
	@TableField(value = "editor", fill = FieldFill.INSERT_UPDATE)
	private String editor;


	@CommentTarget("是否启动，修改时填入当前用户及时间")
	@TableField(exist = false)
	private Boolean useUpdateFill = true;

	/**
	 * 获取字段的备注
	 * @param column
	 * @return
	 */
	public <T extends IBaseEntity> String getComment(SFunction<T, ?> column){
		Field f = getField(this, this.getClass(), FieldConvert.getToField(column));
		if(f==null) {
			UnityRuntimeException.newInstance()
					.code(SystemResponse.FormalErrorCode.DATA_DOES_NOT_EXIST)
					.build();
		}
		CommentTarget commentTarget = f.getAnnotation(CommentTarget.class);
		return commentTarget==null ? "": commentTarget.value();
	}


	private Field getField(Object obj, Class<?> c, String FieldName) {
		try {
			Field f = c.getDeclaredField(FieldName);
			return f;
		} catch (NoSuchFieldException e) {
			if (c.getSuperclass() == null){
				return null;
			}
			else{
				return getField(obj, c.getSuperclass(), FieldName);
			}
		} catch (Exception ex) {
			return null;
		}
	}
	@Override
	public Object get(String FieldName) {
		return get(this, this.getClass(), FieldName);
	}

	public <T extends IBaseEntity> Object get(SFunction<T, ?> column) {
		return this.get(this, this.getClass(), FieldConvert.getToField(column));
	}

	private Object get(Object obj, Class<?> c, String FieldName) {

		try {
			Field f = getField(obj,c,FieldName);
			f.setAccessible(true);
			return f.get(obj);
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