package com.unity.common.log;


import com.unity.common.base.MongoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysLog extends MongoEntity implements Serializable {

    @Field
    private String username; //用户名

    @Field
    private String operation; //操作

    @Field
    private String method; //方法名

    @Field
    private String errorMsg; //错误信息

    @Field
    private String params; //参数

    @Field
    private String ip; //ip地址

    @Field
    private String createDate; //操作时间

    @Field
    private Long executeTime; // 方法执行时长 (ms)

    @Field
    private int statusCode; // 状态码；1：正常，0：异常；

//    @Field
//    private MongoPage page; // mongod 分页类

}
