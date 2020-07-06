package com.unity.common.ui;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * element树形列表载体类
 *
 * <p>
 * create by gengjiajia at 2018/12/11 17:20
 */
@Data
@Builder(builderMethodName = "newInstance")
@AllArgsConstructor
public class ElementTree {

    private Long id;

    private String label;

    private List<ElementTree> children;

    @Builder.Default
    private boolean isLeaf = true;//是否叶子节点

    public ElementTree(){}
}
