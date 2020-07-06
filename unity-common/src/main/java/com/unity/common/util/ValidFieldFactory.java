package com.unity.common.util;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.unity.common.base.BaseEntity;
import com.unity.common.pojos.ValidField;
import com.unity.common.ui.ElementTree;

import java.util.regex.Pattern;

/**
 * 正则校验类工厂
 */
public class ValidFieldFactory {
	private static final String REGEX_PRE = "^.{";
	private static final String SPLIT = ",";
	private static final String REGEX_SUX = "}$";
	private static final Integer MINLENGTH = 0;
	private static final Integer MAXLENGTH= 99999;


	private ValidFieldFactory() {
	}

	/**
	 * @param value   正则表达式
	 * @param message 校验不通过提示语
	 * @return com.unity.common.pojos.ValidField
	 * @title 只为PatternPool创建公用正则对象使用
	 * @author cuihq
	 * @date 2019/8/15
	 */
	protected static ValidField newInstance(Pattern value, String message) {
		return ValidField.newInstance()
				.pattern(value)
				.message(message)
				.build();
	}

	/**
	 * @title 创建一个使用PatternPool提供的正则表达式校验指定属性的正则对象
	 * @author cuihq
	 * @date 2019/8/15
	 * @param validField 正则校验对象
	 * @param column 被校验的属性
	 * @return com.unity.common.pojos.ValidField
	 * */
	public static <T extends BaseEntity> ValidField poolReg(ValidField validField, SFunction<T, ?> column) {
		return ValidField.newInstance()
				.pattern(validField.getPattern())
				.message(validField.getMessage())
				.column(column)
				.build();
	}
	/**
	 * @title 创建一个使用PatternPool提供的正则表达式校验指定属性的正则对象（使用自定义提示语）
	 * @author cuihq
	 * @date 2019/8/16
	 * @param validField 正则校验对象
	 * @param message 提示语
	 * @param column 被校验属性
	 * @return com.unity.common.pojos.ValidField
	 * */
	public static <T extends BaseEntity> ValidField poolReg(ValidField validField,String message, SFunction<T, ?> column) {
		return ValidField.newInstance()
				.pattern(validField.getPattern())
				.msgFlag(true)
				.message(message)
				.column(column)
				.build();
	}

	/**
	 * @title 创建一个使用自定义正则表达式校验指定属性的正则对象
	 * @author cuihq
	 * @date 2019/8/15
	 * @param regex 自定义正则表达式字符串
	 * @param message 被校验住提示语
	 * @param column 被校验属性
	 * @return com.unity.common.pojos.ValidField
	 * */
	public static <T extends BaseEntity> ValidField customReg(String regex, String message, SFunction<T, ?> column) {
		return customReg(regex, message, column, false);
	}

	/**
	 * @title 创建一个使用自定义正则表达式校验指定属性的正则对象（自定义提示语）
	 * @author cuihq
	 * @date 2019/8/19
	 * @param regex 自定义正则表达式字符串
	 * @param message 被校验住提示语
	 * @param column 被校验属性
	 * @param msgFlag 是否使用自定义提示语（true-是 false-否）
	 * @return com.unity.common.pojos.ValidField
	 * */
	public static <T extends BaseEntity> ValidField customReg(String regex, String message, SFunction<T, ?> column,boolean msgFlag) {
		final Pattern pattern = PatternPool.get(regex, Pattern.DOTALL);
		return ValidField.newInstance()
				.pattern(pattern)
				.msgFlag(msgFlag)
				.message(message)
				.column(column)
				.build();
	}


	/**
	 * @param minLength 最小长度
	 * @param maxLength 最大长度
	 * @param column   被校验属性
	 * @return com.unity.common.pojos.ValidField
	 * @title 创建一个校验指定属性最小最大长度的正则对象
	 * @author cuihq
	 * @date 2019/8/15
	 */
	public static <T extends BaseEntity> ValidField lengthReg(Integer minLength, Integer maxLength, SFunction<T, ?> column) {
		Pattern pattern = analyLength(minLength, maxLength);
		return ValidField.newInstance()
				.pattern(pattern)
				.msgFlag(false)
				.message(String.format("不符合长度要求,最小长度:%s,最大长度:%s", minLength, maxLength))
				.column(column)
				.build();
	}

	/**
	 * @title  创建一个校验指定属性最小最大长度的正则对象（自定义提示语）
	 * @author cuihq
	 * @date 2019/8/16
	 * @param minLength 最小长度
	 * @param maxLength 最大长度
	 * @param message 提示语
	 * @param column   被校验属性
	 * @return com.unity.common.pojos.ValidField
	 * */
	public static <T extends BaseEntity> ValidField lengthReg(Integer minLength, Integer maxLength,String message, SFunction<T, ?> column) {
		Pattern pattern = analyLength(minLength, maxLength);
		return ValidField.newInstance()
				.pattern(pattern)
				.msgFlag(true)
				.message(message)
				.column(column)
				.build();
	}

	/**
	 * @title 解析最小最大长度，形成正则表达式
	 * @author cuihq
	 * @date 2019/8/16
	 * @param minLength 最小长度(传入null为0)
	 * @param maxLength 最大长度(传入null为99999)
	 * @return java.util.regex.Pattern
	 * */
	private static Pattern analyLength(Integer minLength, Integer maxLength) {
		String regex;
		if (minLength == null && maxLength == null) {
			regex = REGEX_PRE + MINLENGTH + SPLIT + MAXLENGTH+ REGEX_SUX;
		}else if(minLength == null){
			regex = REGEX_PRE + MINLENGTH + SPLIT + maxLength + REGEX_SUX;
		}else if(maxLength == null){
			regex = REGEX_PRE + minLength + SPLIT + MAXLENGTH + REGEX_SUX;
		}else{
			regex = REGEX_PRE + minLength + SPLIT + maxLength + REGEX_SUX;
		}
		return  PatternPool.get(regex, Pattern.DOTALL);
	}

	/**
	 * @title 创建需要进行非空校验的对象（自定义提示语）
	 * @author cuihq
	 * @date 2019/8/19
	 * @param message 提示语
	 * @param column 被校验属性
	 * @return @{link} ValidField
	 * */
	public static <T extends BaseEntity> ValidField emptyReg(String message , SFunction<T, ?> column) {
		return ValidField.newInstance()
				.pattern(null)
				.msgFlag(true)
				.message(message)
				.column(column)
				.build();
	}


}


































