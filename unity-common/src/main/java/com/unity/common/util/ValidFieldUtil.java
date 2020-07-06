package com.unity.common.util;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.unity.common.base.BaseEntity;
import com.unity.common.exception.UnityRuntimeException;
import com.unity.common.pojos.SystemResponse;
import com.unity.common.pojos.ValidField;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.unity.common.pojos.SystemResponse.FormalErrorCode.DATA_NO_REQUIRE;

public class ValidFieldUtil {
	/**
	 * 校验对象是否为空，为空抛出异常，使用于接口必填项校验
	 *
	 * @param obj
	 * @param columns
	 * @param <T>
	 */
	public static <T extends BaseEntity> void checkEmpty(T obj, SFunction<T, ?>... columns) {
		if (null == obj) {
			throw UnityRuntimeException.newInstance()
					.code(SystemResponse.FormalErrorCode.LACK_REQUIRED_PARAM)
					.build();
		}

		String msg = validEmpty(obj, columns);
		if (!isAnyoneEmpty(msg)) {
			throw UnityRuntimeException.newInstance()
					.code(SystemResponse.FormalErrorCode.LACK_REQUIRED_PARAM)
					.message(msg)
					.build();
		}
	}

	/**
	 * 校验对象是否为空，返回string，适用于导入文件时校验
	 *
	 * @param obj
	 * @param columns
	 * @return
	 */
	public static <T extends BaseEntity> String checkEmptyStr(T obj, SFunction<T, ?>... columns) {
		if (null == obj) {
			return "被校验内容全部为空";
		}

		return validEmpty(obj, columns);
	}

	/**
	 * @param obj     被校验对象
	 * @param columns 被校验属性
	 * @return
	 * @title 校验对象属性是否为空核心方法（默认提示语）
	 */
	private static <T extends BaseEntity> String validEmpty(T obj, SFunction<T, ?>[] columns) {
		return Stream.of(columns)
				.filter(col -> isAnyoneEmpty(obj.get(col)))
				.map(col -> String.format("【%s】不能为空;", obj.getComment(col)))
				.collect(Collectors.joining("\n"));
	}


	/**
	 * @param obj         被校验对象
	 * @param validFields 被校验属性与提示语
	 * @return
	 * @title 校验对象字段属性是否为空（自定义提示语）
	 */
	public static <T extends BaseEntity> String checkEmptyStr(T obj, ValidField<T>... validFields) {
		if (null == obj) {
			return "被校验内容全部为空";
		}
		return validEmpty(obj, validFields);
	}

	/**
	 * @param obj     被校验对象
	 * @param validFields 被校验对象（包含被校验属性和提示语）
	 * @return
	 * @title 校验对象属性是否为空核心方法（自定义提示语）
	 */
	private static <T extends BaseEntity> String validEmpty(T obj, ValidField<T>[] validFields) {
		String msg =
				Stream.of(validFields)
						.filter(validField -> isAnyoneEmpty(obj.get(validField.getColumn())))
						.map(validField -> validField.getMessage())
						.collect(Collectors.joining());
		return msg;
	}

	/**
	 * @param obj         被校验对象
	 * @param validFields 校验正则对象
	 * @return void
	 * @title 使用正则表达式校验对象属性（直接抛出异常）
	 * @author cuihq
	 * @date 2019/8/15
	 */
	public static <T extends BaseEntity> void checkReg(T obj, ValidField<T>... validFields) {
		String msg = validReg(obj, validFields);
		if (!isAnyoneEmpty(msg)) {
			throw UnityRuntimeException.newInstance()
					.code(SystemResponse.FormalErrorCode.DATA_NO_REQUIRE)
					.message(msg)
					.build();
		}
	}

	/**
	 * @param obj         被校验对象
	 * @param validFields 校验正则对象
	 * @return java.lang.String
	 * @title 使用正则表达式校验对象属性（返回String）
	 * @author cuihq
	 * @date 2019/8/16
	 */
	public static <T extends BaseEntity> String checkRegStr(T obj, ValidField<T>... validFields) {
		return validReg(obj, validFields);
	}

	/**
	 * @param obj         被校验对象
	 * @param validFields 校验正则对象
	 * @return java.lang.String
	 * @title 对传入的bean对象进行正则校验
	 * @author cuihq
	 * @date 2019/8/16
	 */
	private static <T extends BaseEntity> String validReg(T obj, ValidField<T>[] validFields) {
		StringBuilder msg = new StringBuilder();
		for (ValidField<T> validField : validFields) {
			//获取需要被校验的属性
			SFunction col = validField.getColumn();
			//获取字段属性值
			String fieldValue = obj.get(col) == null?null:String.valueOf(obj.get(col));
			//字段为空直接返回
			if (isAnyoneEmpty(fieldValue)) {
				continue;
			}
			//字段是否满足正则表达式
			if (!isMatch(validField.getPattern(), fieldValue)) {
				//获取提示语
				String message = validField.getMessage();
				//获取是否自定义提示语
				if (validField.getMsgFlag()) {
					msg.append(message);
				} else {
					//获取字段注释名称
					String comment = obj.getComment(col);
					msg.append(String.format("【%s】%s;\n", comment, message));
				}
			}
		}
		return msg.toString();
	}




	private static boolean isAnyoneEmpty(Object obj) {
		if (obj == null) {
			return obj == null;
		} else if (obj instanceof Collection<?>) {
			return ((Collection<?>) obj).isEmpty();
		} else if (obj instanceof String) {
			return obj.toString().length() == 0;
		} else if (obj.getClass().isArray()) {
			return ((Object[]) obj).length == 0;
		} else if (obj instanceof Map) {
			return ((Map<?, ?>) obj).isEmpty();
		} else if (obj instanceof StringBuffer) {
			return ((StringBuffer) obj).length() == 0;
		} else if (obj instanceof StringBuilder) {
			return ((StringBuilder) obj).length() == 0;
		}
		return false;
	}

	/**
	 * 给定内容是否匹配正则
	 *
	 * @param pattern 模式
	 * @param content 内容
	 * @return 正则为null或者""则不检查，返回true，内容为null返回false
	 */
	private static boolean isMatch(Pattern pattern, CharSequence content) {
		if (content == null || pattern == null) {
			// 提供null的字符串为不匹配
			return false;
		}
		return pattern.matcher(content).matches();
	}


}






























