package com.weixin.request.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author weiyao_tian 2025/3/17
 */
@Data
@ToString
public class User {

    private String id;
    private String name;
    private String phone;
    private String password;
    private Integer userEntityId;
    private Date createTime;
    private String code;
    private String unionId;

}
