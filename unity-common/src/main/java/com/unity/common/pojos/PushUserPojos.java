package com.unity.common.pojos;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * User推送类
 * <p>
 * create by zhangwei at 2018年05月31日 18:24:52
 */
@Builder(builderMethodName = "newInstance")
@AllArgsConstructor
public class PushUserPojos {
    private Map<String,Object> name;
    private Map<String,Object> employeeNo;
    private Map<String,Object> status;
    private Map<String,Object> source;
    private Map<String,Object> info;
    private Map<String,Object> loginName;
    private Date pushDate;

    public Map<String, Object> getName() {
        return name;
    }

    public void setName(String original,String news) {
        this.name = new HashMap<>();
        this.name.put("original",original);
        this.name.put("new",news);
    }

    public Map<String, Object> getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String original,String news) {
        this.employeeNo = new HashMap<>();
        this.employeeNo.put("original",original);
        this.employeeNo.put("new",news);
    }

    public Map<String, Object> getStatus() {
        return status;
    }

    public void setStatus(Integer original,Integer news) {
        this.status = new HashMap<>();
        this.status.put("original",original);
        this.status.put("new",news);
    }

    public Map<String, Object> getSource() {
        return source;
    }

    public void setSource(String original,String news) {
        this.source = new HashMap<>();
        this.source.put("original",original);
        this.source.put("new",news);
    }

    public Map<String, Object> getInfo() {
        return info;
    }

    public void setInfo(String original,String news) {
        this.info = new HashMap<>();
        this.info.put("original",original);
        this.info.put("new",news);
    }

    public Map<String, Object> getLoginName() {
        return loginName;
    }

    public void setLoginName(String original,String news) {
        this.loginName = new HashMap<>();
        this.loginName.put("original",original);
        this.loginName.put("new",news);
    }



    public PushUserPojos(){

    }

    public Date getPushDate() {
        return pushDate;
    }

    public void setPushDate(Date pushDate) {
        this.pushDate = pushDate;
    }
}
