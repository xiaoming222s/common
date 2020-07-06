package com.unity.common.pojos;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.unity.common.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.regex.Pattern;

/**
 * @author cuihq
 * @title 正则校验类
 * @date 2019/8/15
 */
@Data
@Builder(builderMethodName = "newInstance")
@AllArgsConstructor
public class ValidField <T extends BaseEntity> {
	/**
	 * 正则表达式
	 */
	private Pattern pattern;
	/**
	 * 提示语是否自定义 true-自定义 false-采用默认的
	 */
	@Builder.Default
	private Boolean msgFlag = false;
	/**
	 * 不满足正则表达式时提示语
	 */
	private String message;
	/**
	 *  对象属性
	 */
	private SFunction column;

	public ValidField() {
	}


}
