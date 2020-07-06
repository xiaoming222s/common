package com.unity.common.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class mongoBaseDao<T> {
    @Autowired
    protected MongoTemplate mongoTemplate;

    public void save(T entity) {
        mongoTemplate.save(entity);
    }

    public void insert(T entity){
        mongoTemplate.insert(entity);
    }

    public T findById(String id,Class<T> clazz) {
        return mongoTemplate.findById(id,clazz);
    }

    public Long delById(String id,Class<T> clazz) {
        return delByQuery(new Query(Criteria.where("id").is(id)),clazz);
    }
    public Long delByQuery(Query query,Class<T> clazz) {
        return mongoTemplate.remove(query,clazz).getDeletedCount();
    }


    public Page<T> listByPage(Page<T> pageable, Query query,Class<T> clazz){

        //查询总记录数
        long count = mongoTemplate.count(query, clazz);
        query.skip((pageable.getCurrent()-1)*pageable.getSize());
        query.limit((int)pageable.getSize());
        Page<T> page=new Page<>();
        page.setRecords(mongoTemplate.find(query,clazz));
        page.setTotal(count);
        page.setPages(count%pageable.getSize()==0?1:count/pageable.getSize()+1);
        return  page;
    }

}
