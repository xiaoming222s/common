package com.unity.common.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum FlagEnum {
    YES(1, "是"),
    NO(0, "否");

    public static FlagEnum of(Integer id) {
        if (id.equals(YES.getId())) {
            return YES;
        }
        if (id.equals(NO.getId())) {
            return NO;
        }
        return null;
    }

    /**
     * 判断值是否在枚举中存在
     * @param id
     * @return
     */
    public static boolean exist(int id){
        boolean flag = false;
        for (FlagEnum e: FlagEnum.values()){
            if(e.getId()==id){
                flag = true;
                break;
            }
        }
        return flag;
    }

    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
