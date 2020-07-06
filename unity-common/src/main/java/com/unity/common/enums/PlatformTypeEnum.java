package com.unity.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 平台类型
 * <p>
 * create by gengjiajia at 2018/12/05 15:40
 */
@AllArgsConstructor
public enum PlatformTypeEnum {
    WEB(1),
    ANDROID(2),
    IOS(3),
    WX(4),
    APPLETS(5),
    SYSTEM(6);
    @Getter @Setter
    private int type;
}
