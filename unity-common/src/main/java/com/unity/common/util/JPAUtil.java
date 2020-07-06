package com.unity.common.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;

/**
 * JPAUtil  处理JPA 增删改查的一些公共方法
 *
 * <p>
 * create by jiaww at 2018/9/29 上午10:20
 */
public class JPAUtil {

    /**
     * @param defaultSortProperty 默认排序字段
     * @param sortProperty  实际排序字段
     * @param sortType  默认排序方式
     * @return Sort 对象，可直接用来排序
     */
    public static Sort getSort(String defaultSortProperty,String sortProperty,String sortType) {

        String order = StringUtils.isEmpty(defaultSortProperty) ? "id" : defaultSortProperty;
        Sort sort = null;
        if (!StringUtils.isEmpty(sortProperty)) {
            order = sortProperty;
            if (StringUtils.isNotEmpty(sortType)) {
                if ("asc".equals(sortType)){
                    sort = new Sort(Sort.Direction.ASC, order);
                }else {
                    sort = new Sort(Sort.Direction.DESC, order);
                }
            } else {
                sort = new Sort(Sort.Direction.DESC, order);
            }
        }else{
            sort = new Sort(Sort.Direction.DESC, order);
        }
        return sort;
    }
}
