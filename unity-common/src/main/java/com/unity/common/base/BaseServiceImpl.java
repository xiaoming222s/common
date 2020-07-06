package com.unity.common.base;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public class BaseServiceImpl<N extends BaseDao<T>,T> extends ServiceImpl<N,T> {

    public int count2(Wrapper<T> queryWrapper) {
        return SqlHelper.retCount(baseMapper.selectCount2(queryWrapper));
    }

    /**
     * 根据 ID 删除
     *
     * @param id 主键ID
     */
    public int physicalDeleteById(Serializable id){
        return SqlHelper.retCount(baseMapper.physicalDeleteById(id));
    }

    /**
     * 根据 columnMap 条件，删除记录
     *
     * @param columnMap 表字段 map 对象
     */
    public int physicalDeleteByMap(Map<String, Object> columnMap){
        return SqlHelper.retCount(baseMapper.physicalDeleteByMap(columnMap));
    }

    /**
     * 根据 entity 条件，删除记录
     *
     * @param wrapper 实体对象封装操作类（可以为 null）
     */
    public int physicalDelete(Wrapper<T> wrapper){
        return SqlHelper.retCount(baseMapper.physicalDelete(wrapper));
    }

    /**
     * 删除（根据ID 批量删除）
     *
     * @param idList 主键ID列表(不能为 null 以及 empty)
     */
    public int physicalDeleteBatchIds(Collection<? extends Serializable> idList){
        return SqlHelper.retCount(baseMapper.physicalDeleteBatchIds(idList));
    }

//    @Override
//    public IPage<T> page(IPage<T> page, Wrapper<T> wrapper) {
//        IPage<T> obj =  super.page(page, wrapper);
//        obj.setTotal(super.count(wrapper));
//        return obj;
//    }
}
