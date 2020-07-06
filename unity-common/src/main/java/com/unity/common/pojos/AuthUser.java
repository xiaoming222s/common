package com.unity.common.pojos;

import lombok.Data;
import lombok.EqualsAndHashCode;

//import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper=false)
public abstract class AuthUser implements Serializable {
//    @Id
    private Long id;
    private String loginName;
    private String pwd;
    private String phone;
    private String email;
    private String name;


    private Long idEntity;//用户当前身份
    private Integer os;//1 web,2 android,3 ios,4 微信,5 小程序
    private String headPic;
    private String nickName;
    private Long idRbacDepartment;
    private String nameRbacDepartment;
    private String gradationCodeRbacDepartment;
    private List<String> auth;

}