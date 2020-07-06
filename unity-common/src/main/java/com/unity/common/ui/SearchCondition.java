
package com.unity.common.ui;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.unity.common.base.IBaseEntity;
import com.unity.common.util.FieldConvert;
import com.unity.common.util.ValueUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.assertj.core.util.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
public class SearchCondition {


    public <T extends IBaseEntity> SearchCondition addRule(SFunction<T, ?> column, Operate op, Object data){
//        SearchCondition.newInstance().groupOp(GroupOp.and.getId())
//                .rules(Lists.newArrayList()).build()
//                .addRule().addRule().addRule()

        return addRule(FieldConvert.getToField(column),op,data);
    }

    public SearchCondition addRule(String field,Operate op,Object data) {
        Rule r = Rule.newInstance()
                .field(field)
                .op(op.getId())
                .data(data).build();
        if(rules==null){
            rules = new ArrayList<Rule>();
        }
        rules.add(r);
        return this;
    }
    
    private List<Rule> rules;
    private String groupOp;
    private List<SearchCondition> groups;
    
    /**
     * 
     * excludeSpecialState:递归排除 固定值得字段（用于排除【全部】选项）
     * 
     * @author zengqingchao 
     * @param fields
     * @param value 
     * @since JDK 1.8
     */
    public void excludeSpecialState(List<String> fields,String value) {
        fields.forEach(field->{
            if(this.rules!=null) {
                if (this.rules.size()>0) {
                    for(int i=this.rules.size()-1;i>=0;i--) {
                        if(this.rules.get(i).getData()==null){
                            continue;
                        }
                        if(this.rules.get(i).getField().equals(field) && this.rules.get(i).getData().toString().equals(value)) {
                            this.rules.remove(i);
                        }
                    }
                }
            }
            if(this.getGroups()!=null) {
                if(this.getGroups().size()>0) {
                    this.getGroups().forEach(search->{
                        search.excludeSpecialState(fields,value);
                    });
                }
            }
        });
    }

    public <T extends IBaseEntity> void excludeSpecialState(String value,SFunction<T, ?>... columns) {
        List<String> fields = Lists.newArrayList();
        for(SFunction<T, ?> col: columns){
            fields.add(FieldConvert.getToField(col));
        }
        excludeSpecialState(fields,value);
    }

    public <T extends IBaseEntity> void excludeSpecialState(SFunction<T, ?>... columns) {
        List<String> fields = Lists.newArrayList();
        for(SFunction<T, ?> col: columns){
            fields.add(FieldConvert.getToField(col));
        }
        excludeSpecialState(fields);
    }

    public void excludeSpecialState(String... fields) {
        excludeSpecialState(Arrays.asList(fields));
    }

    public void excludeSpecialState(List<String> fields) {
        fields.forEach(field->{
            if(this.rules!=null) {
                if (this.rules.size()>0) {
                    for(int i=this.rules.size()-1;i>=0;i--) {
                        if(this.rules.get(i).getData()==null){
                            continue;
                        }
                        if(this.rules.get(i).getField().equals(field)) {
                            this.rules.remove(i);
                        }
                    }
                }
            }
            if(this.getGroups()!=null) {
                if(this.getGroups().size()>0) {
                    this.getGroups().forEach(search->{
                        search.excludeSpecialState(fields);
                    });
                }
            }
        });
    }

    public <T extends IBaseEntity> Rule findRuleOne(SFunction<T, ?> column) {
        return findRuleOne(this,FieldConvert.getToField(column));
    }

    /**
     * 
     * findRuleOne:递归查找一个Rule
     * 
     * @author zengqingchao 
     * @param name 要查找的字段名
     * @return 
     * @since JDK 1.8
     */
    public Rule findRuleOne(String name) {
        return findRuleOne(this,name);
    }
    
    /**
     * 
     * findRuleOne:递归查找一个Rule
     * 
     * @author zengqingchao 
     * @param cond 条件对象
     * @param name 要查找的字段名
     * @return 
     * @since JDK 1.8
     */
    private Rule findRuleOne(SearchCondition cond,String name) {
        ValueUtil<Rule> val =new ValueUtil<Rule>();
        val.set(this.rules==null ? null: this.rules.stream().filter(o->o.getField().equals(name)).findAny().orElse(null));
        if(val.get()==null) {
            if(cond.groups!=null) {
                if(cond.groups.size()>0) {
                    cond.groups.forEach(g->{
                        val.set(findRuleOne(g,name));
                        if (val.get()!=null) return;
                    });
                }
            }
            return val.get();
        }
        else {
            return val.get();
        }
    }

    public <T extends IBaseEntity> List<Rule> findRule(SFunction<T, ?> column) {
        return findRule(new ArrayList<Rule>(),this,FieldConvert.getToField(column));
    }

    /**
     * 
     * findRule: 递归查找所有的Rule
     * 
     * @author zengqingchao 
     * @param name 要查找的字段名
     * @return 
     * @since JDK 1.8
     */
    public List<Rule> findRule(String name) {
        return findRule(new ArrayList<Rule>(),this,name);
    }
    
    /**
     * 
     * findRule: 递归查找所有的Rule
     * 
     * @author zengqingchao 
     * @param ruleList 结果列表
     * @param cond 条件对象
     * @param name 要查找的字段名
     * @return 
     * @since JDK 1.8
     */
    private List<Rule> findRule(List<Rule> ruleList,SearchCondition cond,String name) {
        if(cond.rules==null){
            return null;
        }
        List<Rule> ll = cond.rules.stream().filter(o->o.getField().equals(name)).collect(Collectors.toList());
        if(ll!=null){
            ruleList.addAll(ll);
        }
        if(cond.groups!=null) {
            if(cond.groups.size()>0) {
                cond.groups.forEach(g->{
                    findRule(ruleList,g,name);
                });
            }
        }
        return ruleList;
    }
}
