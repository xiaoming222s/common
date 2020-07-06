package com.unity.common.ui;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.unity.common.base.IBaseEntity;
import com.unity.common.constants.ConstString;
import com.unity.common.util.FieldConvert;
import com.unity.common.util.Reflections;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import rx.functions.Func1;

import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
public class SearchElementGrid {
    @Builder.Default
    private SearchCondition cond = new SearchCondition();
    private Page pageable;

    public <T extends IBaseEntity> SearchElementGrid rule(SFunction<T, ?> column, Operate op, Object data){
        if(this.cond.getRules()==null){
            this.cond.setRules(Lists.newArrayList());
        }
        this.cond.addRule(FieldConvert.getToField(column),op,data);
        return this;
    }
    public <T extends IBaseEntity> SearchElementGrid groupOp(GroupOp op){
        this.cond.setGroupOp(op.getId());
        return this;
    }
    public <T extends IBaseEntity> SearchElementGrid pageable(long current, long size){
        this.setPageable(new Page(current,size));
        return this;
    }

    public <T extends IBaseEntity> LambdaQueryWrapper toEntityLambdaWrapper(Class<T> clazz){
//        SearchElementGrid.newInstance().pageable(new Page(1,10))
//                .cond()
//                .build();
        return toEntityWrapper(clazz).lambda();
    }

    public <T extends IBaseEntity> QueryWrapper toEntityWrapper(Class<T> clazz){
//        return toEntityWrapper(clazz,null);
        return toEntityWrapper(clazz,this.cond);
    }

    public <T extends IBaseEntity> Query toMongoQuery(Class<T> clazz){
        return new Query(toMongoQuery(clazz,this.cond));
    }

    /**
     * 兼容字符串，数组和List
     * @param val
     * @return
     */
    private Object adapter(Object val){
        if(val.getClass().isArray()){
            return val;
        }
        else if(val instanceof List){
            return  ((List) val).toArray();
        }
        else if(val instanceof String){
            return  val.toString().split(ConstString.SPLIT_COMMA);
        }
        else{
            return null;
        }
    }

    public <T extends IBaseEntity> Criteria toMongoQuery(Class<T> clazz, SearchCondition cond){
        Criteria criteria = new Criteria();

        if (cond != null) {

            if(cond.getRules()==null) return criteria;
            List<Criteria> list = Lists.newArrayList();

            cond.getRules().forEach(rule -> {
                if(rule==null) return;
                // 为存在字段时
//                String field = (rule.getOp().equals(Operate.ex.getId()) || rule.getOp().equals(Operate.nx.getId())) ? ""
//                        : getField(clazz, rule.getField());
                String field = rule.getField();
                switch (Operate.of(rule.getOp())) {
                    case eq:
                        list.add(Criteria.where(field).is(rule.getData()));
                        break;
                    case ne:
                        list.add(Criteria.where(field).ne(rule.getData()));
                        break;
                    case in:{
                        Object val = adapter(rule.getData());
                        list.add(Criteria.where(field).in((Object[])val));
                    }
                    break;
                    case ni:{
                        Object val = adapter(rule.getData());
                        list.add(Criteria.where(field).nin((Object[])val));
                    }
                    break;
                    case bw:{
                        Pattern pattern = Pattern.compile(String.format("^.*%s$",rule.getData().toString()), Pattern.CASE_INSENSITIVE);
                        list.add(Criteria.where(field).regex(pattern));
                    }
                        break;
                    case bn:{
                        Pattern pattern = Pattern.compile(String.format("^.*%s$",rule.getData().toString()), Pattern.CASE_INSENSITIVE);
                        list.add(Criteria.where(field).not().regex(pattern));
                    }
                        break;
                    case ew:{
                        Pattern pattern = Pattern.compile(String.format("^%s.*$",rule.getData().toString()), Pattern.CASE_INSENSITIVE);
                        list.add(Criteria.where(field).regex(pattern));
                    }
                        break;
                    case en:{
                        Pattern pattern = Pattern.compile(String.format("^%s.*$",rule.getData().toString()), Pattern.CASE_INSENSITIVE);
                        list.add(Criteria.where(field).not().regex(pattern));
                    }
                        break;

                    case cn:
                        list.add(Criteria.where(field).regex(String.format("^.*%s.*$",rule.getData().toString())));
                        break;
                    case nc:
                        list.add(Criteria.where(field).not().regex(String.format("^.*%s.*$",rule.getData().toString())));
                        break;
                    case nu:
                        list.add(Criteria.where(field).is(null));
                        break;
                    case nn:
                        list.add(Criteria.where(field).ne(null));
                        break;
                    case ex:
                        list.add(Criteria.where(field).exists(true));
                        break;
                    case nx:
                        list.add(Criteria.where(field).exists(false));
                        break;
                    case lt:
                        list.add(Criteria.where(field).lt(rule.getData()));
                        break;
                    case le:
                        list.add(Criteria.where(field).lte(rule.getData()));
                        break;
                    case gt:
                        list.add(Criteria.where(field).gt(rule.getData()));
                        break;
                    case ge:
                        list.add(Criteria.where(field).gte(rule.getData()));
                        break;
                }


            });

            if (cond.getGroups() != null && cond.getGroups().size() > 0) {
                cond.getGroups().forEach(c -> {
                    if(GroupOp.and.getId().equals(cond.getGroupOp())) criteria.andOperator(toMongoQuery(clazz,c));
                    else if (GroupOp.or.getId().equals(cond.getGroupOp())) criteria.orOperator(toMongoQuery(clazz,c));
                });
            }

            if (GroupOp.and.getId().equals(cond.getGroupOp())){
                if(list.size()>0){
                    criteria.andOperator(list.toArray(new Criteria[list.size()]));
                }
            }
            else if (GroupOp.or.getId().equals(cond.getGroupOp()))
                if(list.size()>0) {
                    criteria.orOperator(list.toArray(new Criteria[list.size()]));
                }
            }

        return criteria;
    }

//    private Func1<Object,String> replace = (o)->{
//        log.info("replace======================"+
//                o.toString().replace("_","\\_")
//                        .replace("%","\\%"));
//
//        return o.toString().replace("_","\\_")
//                .replace("%","\\%");
//    };

    private <T extends IBaseEntity> QueryWrapper toEntityWrapper(Class<T> clazz, SearchCondition cond){



        QueryWrapper<String> wrapper =  new QueryWrapper<String>();
//        wrapper.isWhere(false);
        if (cond != null) {
            if (GroupOp.and.getId().equals(cond.getGroupOp())){
//                wrapper.and();
            }
            else if (GroupOp.or.getId().equals(cond.getGroupOp())){
                wrapper.or();
            }


            if(cond.getRules()==null){
                return wrapper;
            }

            cond.getRules().forEach(rule -> {
                if(rule==null){
                    return;
                }
                // 为存在字段时
                String field = (rule.getOp().equals(Operate.ex.getId()) || rule.getOp().equals(Operate.nx.getId())) ? ""
                        : getField(clazz, rule.getField());

                switch (Operate.of(rule.getOp())) {
                    case eq:
                        wrapper.eq(field, rule.getData());
                        break;
                    case ne:
                        wrapper.ne(field, rule.getData());
                        break;
                    case in:{
                        Object val = adapter(rule.getData());
                        if(val!=null){
                            wrapper.in(field, (Object[])val);
                        }
                    }
                        break;
                    case ni:{
                        Object val = adapter(rule.getData());
                        if(val!=null){
                            wrapper.notIn(field, (Object[])val);
                        }
                    }
                        break;
                    case bw:
                        wrapper.likeRight(true,field, rule.getData());
                        break;
                    case bn:
//                        wrapper.notLike(field, rule.getData().toString(), SqlLike.RIGHT);
                        break;
                    case ew:
                        wrapper.likeLeft(true,field, rule.getData());
                        break;
                    case en:
//                        wrapper.notLike(field, rule.getData().toString(), SqlLike.LEFT);
                        break;

                    case cn:
                        wrapper.like(field, rule.getData());
                        break;
                    case nc:
                        wrapper.notLike(field, rule.getData());
                        break;
                    case nu:
                        wrapper.isNull(field);
                        break;
                    case nn:
                        wrapper.isNotNull(field);
                        break;
                    case ex:
                        wrapper.exists(rule.getData().toString());
                        break;
                    case nx:
                        wrapper.notExists(rule.getData().toString());
                        break;
                    case lt:
                        wrapper.lt(field, rule.getData().toString());
                        break;
                    case le:
                        wrapper.le(field, rule.getData().toString());
                        break;
                    case gt:
                        wrapper.gt(field, rule.getData().toString());
                        break;
                    case ge:
                        wrapper.ge(field, rule.getData().toString());
                        break;
                }
            });

            if (cond.getGroups() != null && cond.getGroups().size() > 0) {
                cond.getGroups().forEach(c -> {
                    AbstractWrapper wr = toEntityWrapper(clazz,c);
//                    if (GroupOp.and.getId().equals(cond.getGroupOp()))
//                        wrapper.and(wr.getSqlSelect(), wr.getParamNameValuePairs());
//                    else if (GroupOp.or.getId().equals(cond.getGroupOp()))
//                        wrapper.or(wr.getSqlSelect(), wr.getParamNameValuePairs());

                });
            }

        }

        return wrapper;
    }

    private String getField(Class<?> clazz, String Filed) {
        Field field;
        try {
            field = Reflections.getAccessibleMethodByClass(clazz, Filed);
            boolean fieldHasAnno = field.isAnnotationPresent(TableField.class);
            if (fieldHasAnno) {
                TableField tableField = field.getAnnotation(TableField.class);
                return tableField.value();
            }
            else if(field.isAnnotationPresent(TableId.class)){
                TableId id = field.getAnnotation(TableId.class);
                return id.value();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
        return "";
    }

}
