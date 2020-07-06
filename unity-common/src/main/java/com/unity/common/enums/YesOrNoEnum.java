package com.unity.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 是或否枚举
 * <p>
 * create by Jung at 2018年06月25日21:53:40
 */
@AllArgsConstructor
public enum YesOrNoEnum {
    YES(1),
    NO(0);
    @Getter @Setter
    private int type;
}
