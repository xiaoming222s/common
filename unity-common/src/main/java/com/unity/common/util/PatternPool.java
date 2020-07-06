package com.unity.common.util;

import com.unity.common.pojos.ValidField;

import java.util.regex.Pattern;

/**
 * 常用正则表达式集合
 * 
 * @author cuihq
 *
 */
public class PatternPool{
	//英文字母 、数字和下划线
	public final static ValidField GENERAL = ValidFieldFactory.newInstance(Pattern.compile("^\\w+$"),"只能是英文字母 、数字和下划线");
	//数字
	public final static ValidField NUMBERS=  ValidFieldFactory.newInstance(Pattern.compile("\\d+"), "只能是数字");
	//字母
	public final static ValidField WORD = ValidFieldFactory.newInstance(Pattern.compile("[a-zA-Z]+"), "只能是字母");
	//单个中文汉字
	public final static ValidField CHINESE = ValidFieldFactory.newInstance(Pattern.compile("[\u4E00-\u9FFF]"), "只能是单个中文汉字");
	//中文汉字
	public final static ValidField CHINESES = ValidFieldFactory.newInstance(Pattern.compile("[\u4E00-\u9FFF]+"), "只能是中文汉字");
	//IP v4
	public final static ValidField IPV4 = ValidFieldFactory.newInstance(Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b"), "不符合IPV4格式规范");
	//IP v6
	public final static ValidField IPV6 = ValidFieldFactory.newInstance(Pattern.compile("(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))"), "不符合IPV6格式规范");
	//邮箱地址
	public final static ValidField EMAIL = ValidFieldFactory.newInstance(Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", Pattern.CASE_INSENSITIVE), "不符合邮箱格式规范");
	//移动电话
	public final static ValidField MOBILE = ValidFieldFactory.newInstance(Pattern.compile("(?:0|86|\\+86)?1[3456789]\\d{9}"), "不符合移动电话格式规范");
	//18位身份证号码
	public final static ValidField CITIZEN_ID = ValidFieldFactory.newInstance(Pattern.compile("[1-9]\\d{5}[1-2]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}(\\d|X|x)"), "不符合身份证格式规范");
	//邮编
	public final static ValidField ZIP_CODE = ValidFieldFactory.newInstance(Pattern.compile("[1-9]\\d{5}(?!\\d)"), "不符合邮编格式规范");
	//URL
	public final static ValidField URL = ValidFieldFactory.newInstance(Pattern.compile("[a-zA-z]+://[^\\s]*"), "不符合URL格式规范");
	//URL_HTTP
	public final static ValidField URL_HTTP = ValidFieldFactory.newInstance(Pattern.compile("(https://|http://)?([\\w-]+\\.)+[\\w-]+(:\\d+)*(/[\\w- ./?%&=]*)?"), "不符合URL_HTTP格式规范");
	//中文字、英文字母、数字和下划线
	public final static ValidField GENERAL_WITH_CHINESE = ValidFieldFactory.newInstance(Pattern.compile("^[\u4E00-\u9FFF\\w]+$"), "只能是中文字、英文字母、数字和下划线");
	//UUID
	public final static ValidField UUID = ValidFieldFactory.newInstance(Pattern.compile("^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$"), "不符合UUID格式规范");
	//不带横线的UUID格式规范
	public final static ValidField UUID_SIMPLE = ValidFieldFactory.newInstance(Pattern.compile("^[0-9a-z]{32}$"), "不符合UUID(带横线)格式规范");
	//中国车牌号码
	public final static ValidField PLATE_NUMBER = ValidFieldFactory.newInstance(Pattern.compile("^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$"), "不符合中国车牌号码格式规范");
	//MAC地址
	public final static ValidField MAC_ADDRESS = ValidFieldFactory.newInstance(Pattern.compile("((?:[A-F0-9]{1,2}[:-]){5}[A-F0-9]{1,2})|(?:0x)(\\d{12})(?:.+ETHER)", Pattern.CASE_INSENSITIVE), "不符合MAC地址格式规范");
	//16进制字符串
	public final static ValidField HEX = ValidFieldFactory.newInstance(Pattern.compile("^[a-f0-9]+$", Pattern.CASE_INSENSITIVE), "不符合16进制字符串格式规范");
	//时间到日格式 yyyy-MM-dd
	public final static ValidField DAYTIME = ValidFieldFactory.newInstance(Pattern.compile("^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$"), "不符合时间格式(yyyy-MM-dd)规范");
	//时间到日格式 yyyy年MM月dd日
	public final static ValidField DAYTIME_CHINESE = ValidFieldFactory.newInstance(Pattern.compile("^[1-9]\\d{3}年(0?[1-9]|1[0-2])月(0?[1-9]|[1-2][0-9]|3[0-1])日$"), "不符合时间格式(yyyy年MM月dd日)规范");
	//密码必须包含大小写字母和数字的组合，不能使用特殊字符，长度在8-18之间
	public final static ValidField PASSWORD = ValidFieldFactory.newInstance(Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,18}$"), "密码必须包含大小写字母和数字的组合，不能使用特殊字符，长度在8-18之间");
	//包含空格,换行符等
	public final static ValidField BLANKLINE  = ValidFieldFactory.newInstance(Pattern.compile("^[^\\s]*$"), "不能包含空格");
	//行首,行尾包含空格
	public final static ValidField BLANKLINE_PRESUX  = ValidFieldFactory.newInstance(Pattern.compile("(^\\s*)|(\\s*$)"), "字段开始结束不能包含空格");


	// -------------------------------------------------------------------------------------------------------------------------------------------------------------------
	/** Pattern池 */
	private static final SimpleCache<RegexWithFlag, Pattern> POOL = new SimpleCache<>();

	/**
	 * 先从Pattern池中查找正则对应的{@link Pattern}，找不到则编译正则表达式并入池。
	 * 
	 * @param regex 正则表达式
	 * @return {@link Pattern}
	 */
	public static Pattern get(String regex) {
		return get(regex, 0);
	}

	/**
	 * 先从Pattern池中查找正则对应的{@link Pattern}，找不到则编译正则表达式并入池。
	 * 
	 * @param regex 正则表达式
	 * @param flags 正则标识位集合 {@link Pattern}
	 * @return {@link Pattern}
	 */
	public static Pattern get(String regex, int flags) {
		final RegexWithFlag regexWithFlag = new RegexWithFlag(regex, flags);

		Pattern pattern = POOL.get(regexWithFlag);
		if (null == pattern) {
			pattern = Pattern.compile(regex, flags);
			POOL.put(regexWithFlag, pattern);
		}
		return pattern;
	}

	/**
	 * 移除缓存
	 * 
	 * @param regex 正则
	 * @param flags 标识
	 * @return 移除的{@link Pattern}，可能为{@code null}
	 */
	public static Pattern remove(String regex, int flags) {
		return POOL.remove(new RegexWithFlag(regex, flags));
	}

	/**
	 * 清空缓存池
	 */
	public static void clear() {
		POOL.clear();
	}

	// ---------------------------------------------------------------------------------------------------------------------------------
	/**
	 * 正则表达式和正则标识位的包装
	 * 
	 * @author Looly
	 *
	 */
	private static class RegexWithFlag {
		private String regex;
		private int flag;

		/**
		 * 构造
		 * 
		 * @param regex 正则
		 * @param flag 标识
		 */
		public RegexWithFlag(String regex, int flag) {
			this.regex = regex;
			this.flag = flag;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + flag;
			result = prime * result + ((regex == null) ? 0 : regex.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			RegexWithFlag other = (RegexWithFlag) obj;
			if (flag != other.flag) {
				return false;
			}
			if (regex == null) {
				if (other.regex != null) {
					return false;
				}
			} else if (!regex.equals(other.regex)) {
				return false;
			}
			return true;
		}

	}
}
